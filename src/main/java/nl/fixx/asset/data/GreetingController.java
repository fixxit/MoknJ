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
public class GreetingController {

    private static final String TEMPLATE = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private AssetRepository assetRepository;

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World?") String name) {
	return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }

    @RequestMapping("/testmongo")
    public void testMongo(@RequestParam(value = "name", defaultValue = "") String name) {
	assetRepository.deleteAll();
	assetRepository.save(new Asset("Laptop", name));

	for (Asset asset : assetRepository.findAll()) {
	    System.out.println(asset.toString());
	}
    }
    
    @RequestMapping("/showassets")
    public List<Asset> showAssets() {
	return assetRepository.findAll();
    }
}
