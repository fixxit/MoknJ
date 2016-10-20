package nl.fixx.asset.data.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nl.fixx.asset.data.domain.Resource;
import nl.fixx.asset.data.info.ResourceResponse;
import nl.fixx.asset.data.repository.ResourceRepository;

/**
 * Created by Colin on 10/13/2016 4:51 PM
 */

@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/resource")
public class ResourceController {
    @Autowired
    ResourceRepository repository;
    ResourceResponse resourceResponse = new ResourceResponse();

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public ArrayList<Resource> getAllResources() {
	ArrayList<Resource> resourceList = new ArrayList<>();
	resourceList.addAll(repository.findAll());
	return resourceList;
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public ResourceResponse addResource(@RequestBody Resource resource) {
	Resource resourceRet = this.repository.save(resource);
	this.resourceResponse.setResource(resourceRet);
	return resourceResponse;
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public ResourceResponse findById(@RequestBody String id) {
	Resource resourceRet = this.repository.findById(id);
	this.resourceResponse.setResource(resourceRet);
	return resourceResponse;
    }
}
