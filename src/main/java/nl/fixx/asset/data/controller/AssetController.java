package nl.fixx.asset.data.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.domain.AssetField;
import nl.fixx.asset.data.domain.AssetFieldDetail;
import nl.fixx.asset.data.domain.AssetLink;
import nl.fixx.asset.data.info.AssetResponse;
import nl.fixx.asset.data.repository.AssetFieldDetailRepository;
import nl.fixx.asset.data.repository.AssetLinkRepository;
import nl.fixx.asset.data.repository.AssetRepository;
import nl.fixx.asset.data.security.OAuth2SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/asset")
public class AssetController {

    @Autowired
    private AssetRepository rep;// main asset Repository
    @Autowired
    private AssetFieldDetailRepository fieldRep; // Asset Field Detail Repository
    @Autowired
    private AssetLinkRepository auditRep; // Asset Audit Repository

    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    public AssetResponse add(@PathVariable String id,
            @RequestBody Asset saveAsset,
            @RequestParam String access_token) {
        AssetResponse response = new AssetResponse();
        try {
            if (id != null) {
                saveAsset.setTypeId(id);

                // checks if the original object differs from new saved object
                // stop the unique filter form check for duplicates on asset
                // which has not changed from the db version...
                String flag = null;
                if (saveAsset.getId() != null) {
                    Asset dbAsset = rep.findOne(saveAsset.getId());
                    if (saveAsset.equals(dbAsset)) {
                        flag = "no_changes";
                    } else {
                        flag = "has_changes";
                    }
                }

                /**
                 * Find unique fields for asset and check if the current list of
                 * assets is unique for the field...
                 */
                Map<String, String> uniqueFields = new HashMap<>();
                Map<String, Boolean> unifieldIndicator = new HashMap<>();
                Map<String, List<String>> uniqueValues = new HashMap<>();

                // Get all the unique field ids
                List<AssetField> newAssetFields = saveAsset.getDetails();
                newAssetFields.stream().forEach((field) -> {
                    AssetFieldDetail detail = fieldRep.findOne(field.getId());
                    if (detail != null && detail.isUnique()) {
                        uniqueFields.put(field.getId(), detail.getName());
                    }
                });

                // Create a list of all the values for the unique assets
                for (Asset asset : rep.getAllByTypeId(id)) {
                    // if statement below checks that if update asset does not check
                    // it self to flag for duplication
                    if (!asset.getId().equals(saveAsset.getId())
                            && !"no_changes".equals(flag)) {
                        List<AssetField> details = asset.getDetails();
                        details.stream().filter((field) -> (uniqueFields.keySet().contains(field.getId()))).map((field) -> {
                            if (uniqueValues.get(field.getId()) == null) {
                                uniqueValues.put(field.getId(), new ArrayList<>());
                            }
                            return field;
                        }).filter((field) -> (!uniqueValues.get(field.getId()).contains(field.getValue()))).forEach((field) -> {
                            uniqueValues.get(field.getId()).add(field.getValue());
                        });
                    }
                }

                // check if fields to be saved for asset has duplicates
                if (!uniqueValues.isEmpty()) {
                    newAssetFields.stream().filter((field) -> (uniqueValues.containsKey(field.getId()))).filter((field) -> (uniqueValues.get(field.getId()).contains(field.getValue()))).forEach((field) -> {
                        unifieldIndicator.put(field.getId(), true);
                    });
                }

                // generate duplication message
                if (!unifieldIndicator.isEmpty()) {
                    String message = unifieldIndicator.size() > 1
                            ? "Non unique values for fields ["
                            : "Non unique value for field ";
                    message = unifieldIndicator.keySet().stream().map((typeId) -> uniqueFields.get(typeId)).map((fieldName) -> fieldName + ",").reduce(message, String::concat);
                    if (message.endsWith(",")) {
                        message = message.substring(0, message.length() - 1);
                    }

                    message += unifieldIndicator.size() > 1
                            ? "]. Please check values"
                            : ". Please input new value";
                    response.setSuccess(false);
                    response.setMessage(message);
                    return response;
                }

                saveAsset.setLastModifiedBy(OAuth2SecurityConfig.getUserForToken(access_token));
                saveAsset.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                Asset savedAsset = rep.save(saveAsset);
                response.setSuccess(true);
                response.setAsset(saveAsset);
                response.setMessage("saved asset[" + savedAsset.getId() + "]");
            } else {
                response.setSuccess(false);
                response.setMessage("No asset type id provided.");
            }
        } catch (IllegalArgumentException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    @RequestMapping(value = "/get/all/{id}", method = RequestMethod.POST)
    public AssetResponse getAllAssets(@PathVariable String id) {
        AssetResponse response = new AssetResponse();
        List<Asset> assets = new ArrayList<>();
        rep.getAllByTypeId(id).stream().filter((asset)
                -> (!asset.isHidden())).forEach((asset)
                -> {
            assets.add(asset);
        });
        response.setAssets(assets);
        return response;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public AssetResponse get(@PathVariable String id) {
        AssetResponse response = new AssetResponse();
        response.setAsset(rep.findOne(id));
        return response;
    }

    @RequestMapping(value = "/delete/", method = RequestMethod.POST)
    public AssetResponse delete(@RequestBody Asset asset) {
        // to insure that the below fields have no influence on find all.
        AssetResponse response = new AssetResponse();
        Asset result = rep.findOne(asset.getId());
        List<AssetLink> assets = auditRep.getAllByAssetId(result.getId());
        try {
            // todo needs a check for linked resources ...
            if (result != null) {
                if (assets.size() > 0) {
                    // hide asset by updating hidden field=
                    result.setHidden(true);
                    rep.save(result);
                    response.setSuccess(true);
                    response.setMessage("Hid asset "
                            + "[" + asset.getId() + "] successfully.");
                } else {
                    // delete asset from the asset list.
                    rep.delete(result);
                    response.setSuccess(true);
                    response.setMessage("Removed asset "
                            + "[" + asset.getId() + "] successfully.");
                }
            } else {
                response.setSuccess(false);
                response.setMessage("Could not remove asset "
                        + "[" + asset.getId() + "] not found in db");
            }
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public AssetResponse all() {
        AssetResponse response = new AssetResponse();
        ArrayList assets = new ArrayList<>();
        assets.addAll(rep.findAll());
        response.setAssets(assets);
        return response;
    }

}
