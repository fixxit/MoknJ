package nl.fixx.asset.data.controller;

import nl.fixx.asset.data.domain.Resource;
import nl.fixx.asset.data.info.ResourceResponse;
import nl.fixx.asset.data.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Colin on 10/13/2016 4:51 PM
 */
@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/asset/resource")
public class ResourceController {

    @Autowired
    private ResourceRepository repository;
    private ResourceResponse resourceResponse = new ResourceResponse();

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public ResourceResponse getAllResources() {
        this.resourceResponse.setResources(repository.findAll());
        return this.resourceResponse;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResourceResponse addResource(@RequestBody Resource payload) throws Exception {

        try {
            // For updates if the type has a id then bypass the exists
            boolean bypassExists = false;
            if (payload.getId() != null) {
                bypassExists = true;
            }

            ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().
                    withMatcher("firstName", GenericPropertyMatchers.ignoreCase()).
                    withMatcher("surname", GenericPropertyMatchers.ignoreCase());
            Example<Resource> example = Example.<Resource>of(payload, NAME_MATCHER);

            boolean exists = repository.exists(example);
            if (!exists || bypassExists) {
                Resource resource = this.repository.save(payload);
                this.resourceResponse.setSuccess(resource != null);
                this.resourceResponse.setMessage("Saved Resource[" + resource.getId() + "]");
                this.resourceResponse.setResource(resource);
            } else {
                this.resourceResponse.setSuccess(false);
                this.resourceResponse.setMessage("Resource by name " + payload.getFirstName() + "exists");
            }
        } catch (IllegalArgumentException ex) {
            this.resourceResponse.setSuccess(false);
            this.resourceResponse.setMessage(ex.getMessage());
        }

        return resourceResponse;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public ResourceResponse get(@PathVariable String id) {
        Resource resourceRet = this.repository.findById(id);
        this.resourceResponse.setResource(resourceRet);
        return resourceResponse;
    }
}
