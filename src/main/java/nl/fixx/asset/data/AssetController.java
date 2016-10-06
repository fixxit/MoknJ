package nl.fixx.asset.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.repository.AssetRepository;

@CrossOrigin //added for cors, allow access from another web server
@RestController()
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    @RequestMapping("/addAsset")
    public void testMongo(@RequestParam(value = "name", defaultValue = "") String name, BigDecimal price, Date purchaseDate) {
	assetRepository.save(new Asset("Laptop", name, price, purchaseDate));
    }

    @RequestMapping("/showassets")
    public List<Asset> showAssets() {
	return assetRepository.findAll();
    }
    
}
