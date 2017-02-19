package nl.it.fixx.moknj.controller;

import nl.it.fixx.moknj.bal.AssetBal;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.repository.RepositoryContext;
import nl.it.fixx.moknj.response.AssetResponse;
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
    private RepositoryContext context;

    @RequestMapping(value = "/add/{menuId}/{id}", method = RequestMethod.POST)
    public AssetResponse add(@PathVariable String id,
            @PathVariable String menuId,
            @RequestBody Asset asset,
            @RequestParam String access_token) {
        AssetResponse response = new AssetResponse();
        try {
            AssetBal bal = new AssetBal(context);
            if (asset == null || asset.getMenuScopeIds().isEmpty()) {
                throw new Exception("No menu id provided for the asset record");
            }

            Asset savedAsset = bal.save(id, menuId, asset, access_token);
            response.setSuccess(true);
            response.setAsset(savedAsset);
            response.setMessage("Successfully saved asset");
            return response;
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
            return response;
        }
    }

    /**
     * Gets a asset by template id and menu id
     *
     * @param templateId
     * @param menuId
     * @param access_token
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/get/all/{templateId}/{menuId}", method = RequestMethod.POST)
    public AssetResponse getAllAssets(@PathVariable String templateId,
            @PathVariable String menuId, @RequestParam String access_token)
            throws Exception {
        AssetResponse response = new AssetResponse();
        try {
            response.setAssets(new AssetBal(context).getAll(templateId, menuId, access_token));
            return response;
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
            return response;
        }
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
        try {
            response.setAsset(new AssetBal(context).get(id));
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
            return response;
        }
        return response;
    }

    /**
     * Deletes or hides asset depending on if it is linked to audit trail
     *
     * @param asset
     * @param menuId
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/delete/{menuId}", method = RequestMethod.POST)
    public AssetResponse delete(@RequestBody Asset asset, @PathVariable String menuId,
            @RequestParam String access_token) {
        AssetResponse response = new AssetResponse();
        try {
            new AssetBal(context).delete(asset, menuId, access_token, false);
            response.setSuccess(true);
            response.setMessage("Asset record was deleted successfully.");
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

}
