package nl.it.fixx.moknj.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.repository.AssetLinkRepository;
import nl.it.fixx.moknj.repository.AssetRepository;
import nl.it.fixx.moknj.repository.FieldDetailRepository;
import nl.it.fixx.moknj.repository.MenuRepository;
import nl.it.fixx.moknj.repository.UserRepository;
import nl.it.fixx.moknj.response.AssetResponse;
import nl.it.fixx.moknj.security.OAuth2SecurityConfig;
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
    private AssetRepository assetRep;// main asset Repository
    @Autowired
    private FieldDetailRepository fieldRep; // Asset FieldValue Detail Repository
    @Autowired
    private AssetLinkRepository auditRep; // Asset Audit Repository
    @Autowired
    private UserRepository resourceRep;
    @Autowired
    private MenuRepository menuRep;

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
                    Asset dbAsset = assetRep.findOne(saveAsset.getId());
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
                List<FieldValue> newAssetFields = saveAsset.getDetails();
                newAssetFields.stream().forEach((field) -> {
                    FieldDetail detail = fieldRep.findOne(field.getId());
                    if (detail != null && detail.isUnique()) {
                        uniqueFields.put(field.getId(), detail.getName());
                    }
                });

                // Create a list of all the values for the unique assets
                for (Asset asset : assetRep.getAllByTypeId(id)) {
                    // if statement below checks that if update asset does not check
                    // it self to flag for duplication
                    if (!asset.getId().equals(saveAsset.getId())
                            && !"no_changes".equals(flag)) {
                        List<FieldValue> details = asset.getDetails();
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

                // Get user details who logged this asset using the token.
                User user = resourceRep.findByUserName(OAuth2SecurityConfig.getUserForToken(access_token));
                if (user != null && user.isSystemUser()) {
                    String fullname = user.getFirstName() + " " + user.getSurname();
                    if (!fullname.trim().isEmpty()) {
                        saveAsset.setLastModifiedBy(fullname);
                    } else {
                        saveAsset.setLastModifiedBy(user.getUserName());
                    }
                } else {
                    response.setSuccess(false);
                    response.setMessage("Asset save error, could not find system"
                            + " user for this token");
                    return response;
                }
                // Save asset
                saveAsset.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                Asset savedAsset = assetRep.save(saveAsset);
                response.setSuccess(true);
                response.setAsset(saveAsset);
                response.setMessage("saved asset[" + savedAsset.getId() + "]");
                return response;
            } else {
                response.setSuccess(false);
                response.setMessage("No asset type id provided.");
                return response;
            }
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
            return response;
        }
    }

    /**
     * Gets a asset by template id and menu id
     * @param templateId
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/get/all/{templateId}/{menuId}", method = RequestMethod.POST)
    public AssetResponse getAllAssets(@PathVariable String templateId, @PathVariable String menuId) {
        AssetResponse response = new AssetResponse();
        List<Asset> assets = new ArrayList<>();
        List<Asset> records = assetRep.getAllByTypeId(templateId);
        // Gets custom template settings saved to menu.
        Menu menu = menuRep.findOne(menuId);
        menu.getTemplates().stream().filter((template)
                -> (template.getId().equals(templateId))).forEach((Template template)
                -> {
            records.stream().forEach((Asset record) -> {
                // checks if scope check is required for this asset.
                boolean inScope = false;
                if (template.isAllowScopeChallenge()) {
                    inScope = record.getMenuScopeIds().contains(menuId);
                } else {
                    inScope = true;
                }
                // if asset is inscope allow adding of asset.
                if (inScope) {
                    // checks if the asset is hidden.
                    if (!record.isHidden()) {
                        assets.add(record);
                    }
                }
            });
        });

        response.setAssets(assets);
        return response;
    }

    /**
     * Gets the asset by id
     *
     * @param id
     * @return Asset
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public AssetResponse get(@PathVariable String id) {
        AssetResponse response = new AssetResponse();
        response.setAsset(assetRep.findOne(id));
        return response;
    }

    /**
     * Deletes or hides asset depending on if it is linked to audit trail
     *
     * @param asset
     * @return
     */
    @RequestMapping(value = "/delete/", method = RequestMethod.POST)
    public AssetResponse delete(@RequestBody Asset asset) {
        // to insure that the below fields have no influence on find all.
        AssetResponse response = new AssetResponse();
        Asset result = assetRep.findOne(asset.getId());
        List<AssetLink> assets = auditRep.getAllByAssetId(result.getId());
        try {
            // todo needs a check for linked resources ...
            if (result != null) {
                if (assets.size() > 0) {
                    // hide asset by updating hidden field=
                    result.setHidden(true);
                    assetRep.save(result);
                    response.setSuccess(true);
                    response.setMessage("Hid asset "
                            + "[" + asset.getId() + "] successfully.");
                } else {
                    // delete asset from the asset list.
                    assetRep.delete(result);
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

    /**
     * Gets all assets
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public AssetResponse all() {
        AssetResponse response = new AssetResponse();
        ArrayList assets = new ArrayList<>();
        assets.addAll(assetRep.findAll());
        response.setAssets(assets);
        return response;
    }

}
