package nl.fixx.asset.data.controller;

import java.util.ArrayList;
import java.util.List;
import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.domain.AssetField;
import nl.fixx.asset.data.info.AssetResponse;
import nl.fixx.asset.data.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
    private AssetRepository resp;

    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    public AssetResponse add(@PathVariable String id, @RequestBody Asset asset) {
        AssetResponse response = new AssetResponse();
        try {
            if (id != null) {
                asset.setTypeId(id);
                Asset savedAsset = resp.save(asset);
                response.setSuccess(true);
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

    @GetMapping("/asset")
    public AssetResponse asset() throws Exception {
        AssetResponse response = new AssetResponse();
        Asset asset = new Asset();
        asset.setDetails(new ArrayList<AssetField>());

        AssetField field = new AssetField();
        field.setValue("1231231232333");

        asset.getDetails().add(field);
        response.setAsset(asset);
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

        assets.addAll(resp.findAll(example));
        response.setAssets(assets);
        return response;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public AssetResponse all() {
        AssetResponse response = new AssetResponse();
        ArrayList assets = new ArrayList<>();
        assets.addAll(resp.findAll());
        response.setAssets(assets);
        return response;
    }

}
