package nl.fixx.asset.data.info;

import java.util.ArrayList;
import java.util.List;

import nl.fixx.asset.data.domain.Resource;

public class ResourceResponse extends Response {
    private Resource resource;
    private List<Resource> resourceList = new ArrayList<>();

    public Resource getResource() {
	return resource;
    }

    public void setResource(Resource resource) {
	this.resource = resource;
    }

    public List<Resource> getResourceList() {
	return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
	this.resourceList = resourceList;
    }

    @Override
    public String toString() {
	return "ResourceResponse [resource=" + resource + ", resourceList=" + resourceList + "]";
    }
}
