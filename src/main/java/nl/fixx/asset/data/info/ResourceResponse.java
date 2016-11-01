package nl.fixx.asset.data.info;

import java.util.ArrayList;
import java.util.List;

import nl.fixx.asset.data.domain.Resource;

public class ResourceResponse extends Response {
    private Resource resource;
    private List<Resource> resources = new ArrayList<>();

    public Resource getResource() {
	return resource;
    }

    public void setResource(Resource resource) {
	this.resource = resource;
    }

    public List<Resource> getResources() {
	return resources;
    }

    public void setResources(List<Resource> resources) {
	this.resources = resources;
    }

    @Override
    public String toString() {
	return "ResourceResponse [resource=" + resource + ", resourceList=" + resources + "]";
    }
}
