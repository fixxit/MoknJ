package nl.it.fixx.moknj.controller;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.core.user.UserAuthority;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.response.UserResponse;
import nl.it.fixx.moknj.repository.AssetLinkRepository;
import nl.it.fixx.moknj.repository.AssetRepository;
import nl.it.fixx.moknj.repository.UserRepository;
import static nl.it.fixx.moknj.security.OAuth2SecurityConfig.PSW_ENCODER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/resource")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository resourceRep;
    @Autowired
    private AssetRepository assetRep;
    @Autowired
    private AssetLinkRepository auditRep;

    public static String ADMIN_NAME = "fixxit";

    private final BCryptPasswordEncoder passwordEncoder;

    public UserController() {
        this.passwordEncoder = PSW_ENCODER;
    }

    @RequestMapping(value = "/get/all", method = RequestMethod.POST)
    public UserResponse all() {
        final UserResponse userResponse = new UserResponse();
        List<User> resources = new ArrayList<>();
        resourceRep.findAll().stream()
                .filter((resource) -> (!resource.isHidden()
                        && !ADMIN_NAME.equals(resource.getUserName())))
                .forEach((resource) -> {
                    resources.add(resource);
                });
        userResponse.setResources(resources);
        return userResponse;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public UserResponse add(@RequestBody User payload) throws Exception {
        final UserResponse userResponse = new UserResponse();
        try {
            // For updates if the type has a id then bypass the exists
            User dbResource = null;
            if (payload.getId() != null) {
                dbResource = resourceRep.findById(payload.getId());
            }

            List<User> results = resourceRep.findByFullname(
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

                // checks if user name exists
                if (payload.isSystemUser()
                        && payload.getUserName() != null
                        && !payload.getUserName().trim().isEmpty()) {
                    String newUsername = payload.getUserName();
                    String dbUsername = (dbResource != null
                            && dbResource.isSystemUser())
                                    ? dbResource.getUserName() : "";
                    // this should execute for new users too
                    // as the dbUsername should then be empty string
                    if (!newUsername.equals(dbUsername)) {
                        User indb = resourceRep.findByUserName(newUsername);
                        if (indb != null && indb.getId() != null) {
                            userResponse.setSuccess(false);
                            userResponse.setMessage("Employee with a user name "
                                    + "" + newUsername + " already exists");
                            return userResponse;
                        }
                    }
                }

                User resource = resourceRep.save(payload);
                userResponse.setSuccess(resource != null);
                userResponse.setMessage("Saved employee[" + resource.getId() + "]");
                userResponse.setResource(resource);
            } else {
                userResponse.setSuccess(false);
                userResponse.setMessage("Employee by name "
                        + "" + payload.getFirstName() + " "
                        + "" + payload.getSurname() + " exists");
            }
        } catch (Exception ex) {
            userResponse.setSuccess(false);
            userResponse.setMessage(ex.getMessage());
            LOG.error("Error while saving resource", ex);
        }

        return userResponse;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public UserResponse get(@PathVariable String id) {
        final UserResponse userResponse = new UserResponse();
        User resourceRet = resourceRep.findById(id);
        if (resourceRet != null) {
            userResponse.setResource(resourceRet);
        }
        return userResponse;
    }

    @RequestMapping(value = "/authorities", method = RequestMethod.POST)
    public UserResponse authorities() {
        final UserResponse userResponse = new UserResponse();
        userResponse.setAuthorities(new ArrayList<>());

        UserAuthority[] auths = UserAuthority.values();
        for (UserAuthority auth : auths) {
            userResponse.getAuthorities().add(auth.toString());
        }

        return userResponse;
    }

    // DELETE SHOULD DELETE THE RESOUCRE IF NO ASSET OR AUDIT IS linked else hide
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public UserResponse delete(@PathVariable String id) {
        final UserResponse userResponse = new UserResponse();
        try {
            User resource = resourceRep.findById(id);
            List<Asset> assets = assetRep.getAllByResourceId(id);
            List<AssetLink> links = auditRep.getAllByResourceId(id);
            if (!assets.isEmpty() || !links.isEmpty()) {
                resource.setHidden(true);
                resourceRep.save(resource);
            } else {
                resourceRep.delete(resource);
            }
        } catch (Exception ex) {
            userResponse.setSuccess(false);
            userResponse.setMessage(ex.getMessage());
            LOG.error("Error while deleting resource", ex);
        }
        return userResponse;
    }
}
