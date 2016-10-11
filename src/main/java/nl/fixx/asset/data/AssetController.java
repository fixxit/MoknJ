package nl.fixx.asset.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.info.AssetResponse;
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
    public AssetResponse addAsset(@RequestBody Asset asset) {
        System.out.println(asset.toString());
        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setAction("POST");
        assetResponse.setMethod("/add");

        try {
            Asset savedAsset = this.assetRepository.save(asset);
            assetResponse.setSuccess(true);
            assetResponse.setMessage(savedAsset.getId());
        } catch (IllegalArgumentException ex) {
            assetResponse.setSuccess(false);
            assetResponse.setMessage(ex.getMessage());
        }

        return assetResponse;
    }

    @GetMapping("/asset")
    public Asset asset() throws Exception {
        Asset asset = new Asset();
        asset.setDescription("description");
        asset.setName("name");
        asset.setPrice(BigDecimal.ONE);
        asset.setPurchaseDate(new Date());
        return asset;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public ArrayList<Asset> getAllAssets() {
        ArrayList list = new ArrayList<Asset>();
        list.addAll(assetRepository.findAll());
        return list;
    }

}
