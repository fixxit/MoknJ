package nl.fixx.asset.data;

import java.util.List;

import nl.fixx.asset.data.info.AssetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.repository.AssetRepository;

@CrossOrigin // added for cors, allow access from another web server
@RestController()
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    @RequestMapping(value = "/addAsset", method = RequestMethod.POST)
    public AssetResponse addAsset(@RequestBody Asset asset) {
        System.out.println(asset.toString());
        AssetResponse assetResponse = new AssetResponse();

        try {
            Asset savedAsset = this.assetRepository.save(asset);
            assetResponse.setSuccess(true);
            assetResponse.setAction("POST");
            assetResponse.setMethod("/addAsset");
            assetResponse.setMessage(savedAsset.getId());
        } catch (IllegalArgumentException ex) {
            assetResponse.setSuccess(false);
            assetResponse.setAction("POST");
            assetResponse.setMethod("/addAsset");
            assetResponse.setMessage(ex.getMessage());
        }

        return assetResponse;
    }

    @RequestMapping("/getAllAssets")
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

}
