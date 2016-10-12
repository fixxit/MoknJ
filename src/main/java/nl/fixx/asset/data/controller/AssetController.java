package nl.fixx.asset.data.controller;

import java.util.ArrayList;
import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.domain.AssetField;
import nl.fixx.asset.data.info.Response;
import nl.fixx.asset.data.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/asset")
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response addAsset(@RequestBody Asset asset) {
        System.out.println(asset.toString());
        Response assetResponse = new Response();
        assetResponse.setAction("POST");
        assetResponse.setMethod("/add");

        try {
            Asset savedAsset = this.assetRepository.save(asset);
            assetResponse.setSuccess(true);
            assetResponse.setMessage("saved asset[" + savedAsset.getId() + "]");
        } catch (IllegalArgumentException ex) {
            assetResponse.setSuccess(false);
            assetResponse.setMessage(ex.getMessage());
        }

        return assetResponse;
    }

    @GetMapping("/asset")
    public Asset asset() throws Exception {
        Asset asset = new Asset();
        asset.setValues(new ArrayList<AssetField>());

        AssetField field = new AssetField();
        field.setFieldDetailId(1);
        field.setValue("1231231232333");

        asset.getValues().add(field);
        return asset;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public ArrayList<Asset> getAllAssets() {
        ArrayList list = new ArrayList<>();
        list.addAll(assetRepository.findAll());
        return list;
    }

}
