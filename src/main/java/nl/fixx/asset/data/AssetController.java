package nl.fixx.asset.data;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.repository.AssetRepository;

@RestController()
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    @RequestMapping("/addAsset")
    public void testMongo(@RequestParam(value = "name", defaultValue = "") String name) {
	assetRepository.save(new Asset("Laptop", name));
    }
    
    @RequestMapping("/showassets")
    public List<Asset> showAssets() {
	return assetRepository.findAll();
    }
}
