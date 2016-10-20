package nl.fixx.asset.data.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.domain.AssetField;
import nl.fixx.asset.data.info.AssetResponse;
import nl.fixx.asset.data.repository.AssetRepository;

@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/asset")
public class AssetController {

    @Autowired
    private AssetRepository resp;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public AssetResponse addAsset(@RequestBody Asset asset) {
	AssetResponse response = new AssetResponse();
	response.setAction("POST");
	response.setMethod("/add");

	try {
	    Asset savedAsset = resp.save(asset);
	    response.setSuccess(true);
	    response.setMessage("saved asset[" + savedAsset.getId() + "]");
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
	asset.setValues(new ArrayList<AssetField>());

	AssetField field = new AssetField();
	field.setFieldDetailId(1);
	field.setValue("1231231232333");

	asset.getValues().add(field);
	response.setAsset(asset);
	return response;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public AssetResponse getAllAssets() {
	AssetResponse response = new AssetResponse();
	ArrayList assets = new ArrayList<>();
	assets.addAll(resp.findAll());
	response.setAssets(assets);
	return response;
    }

}
