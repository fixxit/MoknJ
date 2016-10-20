package nl.fixx.asset.data.builders;

import java.util.List;

import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.domain.Resource;

public class ResourceBuilder {
    private Resource resource = new Resource();
    
    public ResourceBuilder firstName(String firstName){
	resource.setFirstName(firstName);
	return this;
    }
    
    public ResourceBuilder surname(String surname){
	resource.setSurname(surname);
	return this;
    }
    
    public ResourceBuilder email(String email){
	resource.setEmail(email);
	return this;
    }
    
    public ResourceBuilder contactNumber(String contactNumber){
	resource.setContactNumber(contactNumber);
	return this;
    }
    
    public ResourceBuilder placedAtClient(String placedAtClient){
	resource.setPlacedAtClient(placedAtClient);
	return this;
    }
    
    public ResourceBuilder assetList(List<Asset> assetList){
	resource.setAssetList(assetList);
	return this;
    }
    
    public Resource build(){
	return resource;
    }
}
