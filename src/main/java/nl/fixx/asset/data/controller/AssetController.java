package nl.fixx.asset.data.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.domain.AssetField;
import nl.fixx.asset.data.domain.AssetFieldDetail;
import nl.fixx.asset.data.info.AssetResponse;
import nl.fixx.asset.data.repository.AssetFieldDetailRepository;
import nl.fixx.asset.data.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/asset")
public class AssetController {

    @Autowired
    private AssetRepository rep;// main asset Repository
    @Autowired
    private AssetFieldDetailRepository fieldRep; // Asset Field Detail Repository

    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    public AssetResponse add(@PathVariable String id, @RequestBody Asset saveAsset) {
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

                Asset search = new Asset();
                search.setTypeId(id);
                ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().withMatcher("typeId",
                        ExampleMatcher.GenericPropertyMatchers.ignoreCase());
                Example<Asset> allByTypeIDExample = Example.<Asset>of(search, NAME_MATCHER);
                /**
                 * Find unique fields for asset and check if the current list of
                 * assets is unique for the field...
                 */
                Map<String, String> uniqueFields = new HashMap<String, String>();
                Map<String, Boolean> unifieldIndicator = new HashMap<String, Boolean>();
                Map<String, List<String>> uniqueValues = new HashMap<String, List<String>>();

                // Get all the unique field ids
                List<AssetField> newAssetFields = saveAsset.getDetails();
                for (AssetField field : newAssetFields) {
                    AssetFieldDetail detail = fieldRep.findOne(field.getId());
                    if (detail.isUnique()) {
                        uniqueFields.put(field.getId(), detail.getName());
                    }
                }

                // Create a list of all the values for the unique assets
                for (Asset asset : rep.findAll(allByTypeIDExample)) {
                    // if statement below checks that if update asset does not check
                    // it self to flag for duplication
                    if (!asset.getId().equals(saveAsset.getId())
                            && !"no_changes".equals(flag)) {
                        List<AssetField> details = asset.getDetails();
                        for (AssetField field : details) {
                            if (uniqueFields.keySet().contains(field.getId())) {
                                if (uniqueValues.get(field.getId()) == null) {
                                    uniqueValues.put(field.getId(), new ArrayList<String>());
                                }
                                if (!uniqueValues.get(field.getId()).contains(field.getValue())) {
                                    uniqueValues.get(field.getId()).add(field.getValue());
                                }
                            }
                        }
                    }
                }

                // check if fields to be saved for asset has duplicates
                if (!uniqueValues.isEmpty()) {
                    for (AssetField field : newAssetFields) {
                        if (uniqueValues.containsKey(field.getId())) {
                            if (uniqueValues.get(field.getId()).contains(field.getValue())) {
                                unifieldIndicator.put(field.getId(), true);
                            }
                        }
                    }
                }

                // generate duplication message
                if (!unifieldIndicator.isEmpty()) {
                    String message = unifieldIndicator.size() > 1
                            ? "Non unique values for fields ["
                            : "Non unique value for field ";
                    for (String typeId : unifieldIndicator.keySet()) {
                        String fieldName = uniqueFields.get(typeId);
                        message += fieldName + ",";
                    }
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
        Asset search = new Asset();
        search.setTypeId(id);

        ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().withMatcher("typeId",
                ExampleMatcher.GenericPropertyMatchers.ignoreCase());
        Example<Asset> example = Example.<Asset>of(search, NAME_MATCHER);

        assets.addAll(rep.findAll(example));
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
    public AssetResponse delete(@RequestBody Asset search) {
        // to insure that the below fields have no influence on find all.
        search.setDetails(null);
        search.setTypeId(null);

        AssetResponse response = new AssetResponse();
        ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().withMatcher("id",
                ExampleMatcher.GenericPropertyMatchers.ignoreCase());
        Example<Asset> example = Example.<Asset>of(search, NAME_MATCHER);
        // todo needs a check for linked resources ...
        List<Asset> assets = rep.findAll(example);
        if (assets.size() > 0) {
            rep.delete(search.getId());
            response.setSuccess(true);
            response.setMessage("Removed asset [" + search.getId() + "] successfully.");
            return response;
        } else {
            response.setSuccess(false);
            response.setMessage("Could not find any asset matching id[" + search.getId() + "]");
            return response;
        }
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
