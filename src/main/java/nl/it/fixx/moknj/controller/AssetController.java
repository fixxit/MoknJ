package nl.it.fixx.moknj.controller;

import nl.it.fixx.moknj.bal.module.ModuleBal;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.response.AssetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final ModuleBal<Asset> assetBal;

    @Autowired
    public AssetController(@Qualifier("assetBal") ModuleBal<Asset> bal) {
        this.assetBal = bal;
    }

    @RequestMapping(value = "/add/{menuId}/{id}", method = RequestMethod.POST)
    public AssetResponse add(@PathVariable String id,
            @PathVariable String menuId,
            @RequestBody Asset asset,
            @RequestParam String access_token) {
        AssetResponse response = new AssetResponse();
        if (asset == null || asset.getMenuScopeIds().isEmpty()) {
            throw new BalException("No menu id provided for the asset record");
        }
        response.setSuccess(true);
        response.setAsset(assetBal.save(id, menuId, asset, access_token));
        response.setMessage("Successfully saved asset");
        return response;
    }

    /**
     * Gets a asset by template id and menu id
     *
     * @param templateId
     * @param menuId
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/get/all/{templateId}/{menuId}", method = RequestMethod.POST)
    public AssetResponse getAllAssets(@PathVariable String templateId,
            @PathVariable String menuId, @RequestParam String access_token) {
        AssetResponse response = new AssetResponse();
        response.setAssets(assetBal.getAll(templateId, menuId, access_token));
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
        response.setAsset(assetBal.get(id));
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
        assetBal.delete(asset, menuId, access_token, false);
        response.setSuccess(true);
        response.setMessage("Asset record was deleted successfully.");
        return response;
    }

}
