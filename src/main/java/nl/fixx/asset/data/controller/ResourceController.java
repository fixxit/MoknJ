package nl.fixx.asset.data.controller;

import java.util.ArrayList;
import java.util.List;
import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.domain.AssetLink;
import nl.fixx.asset.data.domain.Resource;
import nl.fixx.asset.data.info.ResourceResponse;
import nl.fixx.asset.data.repository.AssetLinkRepository;
import nl.fixx.asset.data.repository.AssetRepository;
import nl.fixx.asset.data.repository.ResourceRepository;
import static nl.fixx.asset.data.security.OAuth2SecurityConfig.PSW_ENCODER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private ResourceRepository resourceRep;
    @Autowired
    private AssetRepository assetRep;
    @Autowired
    private AssetLinkRepository auditRep;

    private final BCryptPasswordEncoder passwordEncoder;

    public ResourceController() {
        this.passwordEncoder = PSW_ENCODER;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public ResourceResponse all() {
        final ResourceResponse resourceResponse = new ResourceResponse();
        List<Resource> resources = new ArrayList<>();
        resourceRep.findAll().stream()
                .filter((resource) -> (!resource.isHidden()))
                .forEach((resource) -> {
                    resources.add(resource);
                });
        resourceResponse.setResources(resources);
        return resourceResponse;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResourceResponse add(@RequestBody Resource payload) throws Exception {
        final ResourceResponse resourceResponse = new ResourceResponse();
        try {
            // For updates if the type has a id then bypass the exists

            Resource dbResource = null;
            if (payload.getId() != null) {
                dbResource = resourceRep.findById(payload.getId());
            }

            List<Resource> results = resourceRep.findByFullname(
                    payload.getFirstName(), payload.getSurname());

            boolean exists = results.size() > 0;

            if (!exists || dbResource != null) {
                // SET PASSWORD TO A SALT...
                if (dbResource != null
                        && payload.isSystemUser()
                        && payload.getPassword() != null) {
                    // if psw is not equal set new password else nothing
                    // hass password but assigned new password.
                    if (dbResource.getPassword() != null
                            && !dbResource.getPassword().equals(payload.getPassword())) {
                        payload.setPassword(passwordEncoder.encode(payload.getPassword()));
                    // emmpty password but did recieve value for password field...
                    } else if (dbResource.getPassword() == null && payload.getPassword() != null) {
                        payload.setPassword(passwordEncoder.encode(payload.getPassword()));
                    }
                } else if (payload.isSystemUser() && payload.getPassword() != null) {
                    // new with new password
                    payload.setPassword(passwordEncoder.encode(payload.getPassword()));
                }

                Resource resource = resourceRep.save(payload);
                resourceResponse.setSuccess(resource != null);
                resourceResponse.setMessage("Saved employee[" + resource.getId() + "]");
                resourceResponse.setResource(resource);
            } else {
                resourceResponse.setSuccess(false);
                resourceResponse.setMessage("Employee by name "
                        + "" + payload.getFirstName() + " "
                        + "" + payload.getSurname() + " exists");
            }
        } catch (IllegalArgumentException ex) {
            resourceResponse.setSuccess(false);
            resourceResponse.setMessage(ex.getMessage());
        }

        return resourceResponse;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public ResourceResponse get(@PathVariable String id) {
        final ResourceResponse resourceResponse = new ResourceResponse();
        Resource resourceRet = resourceRep.findById(id);
        if (resourceRet != null) {
            resourceResponse.setResource(resourceRet);
        }
        return resourceResponse;
    }

    // DELETE SHOULD DELET THE RESOUCRE IF NO ASSET OR AUDIT IS linked else hide
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public ResourceResponse delete(@PathVariable String id) {
        final ResourceResponse resourceResponse = new ResourceResponse();
        try {
            Resource resource = resourceRep.findById(id);
            List<Asset> assets = assetRep.getAllByResourceId(id);
            List<AssetLink> links = auditRep.getAllByResourceId(id);
            if (!assets.isEmpty() || !links.isEmpty()) {
                resource.setHidden(true);
                resourceRep.save(resource);
            } else {
                resourceRep.delete(resource);
            }
        } catch (IllegalArgumentException ex) {
            resourceResponse.setSuccess(false);
            resourceResponse.setMessage(ex.getMessage());
        }
        return resourceResponse;
    }
}
