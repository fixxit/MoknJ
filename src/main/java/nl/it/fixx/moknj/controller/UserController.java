package nl.it.fixx.moknj.controller;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.bal.AccessBal;
import nl.it.fixx.moknj.bal.UserBal;
import nl.it.fixx.moknj.domain.core.access.Access;
import nl.it.fixx.moknj.domain.core.access.AccessRight;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.core.user.UserAuthority;
import nl.it.fixx.moknj.repository.SystemContext;
import nl.it.fixx.moknj.repository.UserRepository;
import nl.it.fixx.moknj.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/resource")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private SystemContext context;

    @RequestMapping(value = "/get/all", method = RequestMethod.POST)
    public UserResponse all(@RequestParam String access_token) throws Exception {
        UserResponse userResponse = new UserResponse();
        try {
            UserBal userBal = new UserBal(context);
            userResponse.setResources(userBal.getAll(true, access_token));
        } catch (Exception ex) {
            userResponse.setSuccess(false);
            userResponse.setMessage(ex.getMessage());
            LOG.error("Error while geting all user", ex);
        }
        return userResponse;
    }

    @RequestMapping(value = "/get/employee/all", method = RequestMethod.POST)
    public UserResponse getAllUsersForEmployee() throws Exception {
        UserResponse userResponse = new UserResponse();
        try {
            UserBal userBal = new UserBal(context);
            userResponse.setResources(userBal.getAll());
        } catch (Exception ex) {
            userResponse.setSuccess(false);
            userResponse.setMessage(ex.getMessage());
            LOG.error("Error while geting all user", ex);
        }
        return userResponse;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public UserResponse add(@RequestBody User payload,
            @RequestParam String access_token) throws Exception {
        UserResponse userResponse = new UserResponse();
        try {
            UserBal userBal = new UserBal(context);
            User user = userBal.save(payload, access_token);
            userResponse.setSuccess(user != null);
            userResponse.setMessage("Saved user[" + user.getId() + "]");
            userResponse.setResource(user);
        } catch (Exception ex) {
            userResponse.setSuccess(false);
            userResponse.setMessage(ex.getMessage());
            LOG.error("Error while saving resource", ex);
        }

        return userResponse;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public UserResponse get(@PathVariable String id, @RequestParam String access_token) throws Exception {
        UserResponse userResponse = new UserResponse();
        User resourceRet = context.getRepository(UserRepository.class).findById(id);
        if (resourceRet != null) {
            userResponse.setResource(resourceRet);
        }
        return userResponse;
    }

    @RequestMapping(value = "/authorities", method = RequestMethod.POST)
    public UserResponse authorities() {
        UserResponse userResponse = new UserResponse();
        userResponse.setAuthorities(new ArrayList<>());
        UserAuthority[] auths = UserAuthority.values();
        for (UserAuthority auth : auths) {
            userResponse.getAuthorities().add(auth.toString());
        }

        return userResponse;
    }

    /**
     * Gets the global access rights list.
     *
     * @return
     */
    @RequestMapping(value = "/all/rights", method = RequestMethod.POST)
    public UserResponse getAccessRights() {
        UserResponse userResponse = new UserResponse();
        List<AccessRight> rights = new ArrayList<>();
        GlobalAccessRights[] gblrights = GlobalAccessRights.values();
        for (GlobalAccessRights right : gblrights) {
            rights.add(new AccessRight(right.name(), right.getDisplayValue()));
        }
        userResponse.setRights(rights);

        return userResponse;
    }

    // DELETE SHOULD DELETE THE RESOUCRE IF NO ASSET OR AUDIT IS linked else hide
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public UserResponse delete(@PathVariable String id, @RequestParam String access_token) {
        UserResponse userResponse = new UserResponse();
        try {
            UserBal userBal = new UserBal(context);
            userBal.delete(id, access_token);
            userResponse.setSuccess(true);
            userResponse.setMessage("User deleted");
        } catch (Exception ex) {
            userResponse.setSuccess(false);
            userResponse.setMessage(ex.getMessage());
            LOG.error("Error while deleting resource", ex);
        }
        return userResponse;
    }

    // ACCESS SAVING
    @RequestMapping(value = "/add/{id}/accesss", method = RequestMethod.POST)
    public UserResponse addAccess(@PathVariable String id, @
            RequestBody List<Access> access,
            @RequestParam String access_token) {
        final UserResponse userResponse = new UserResponse();
        try {
            AccessBal userAccessBal = new AccessBal(context);
            userAccessBal.addAccess(id, access, access_token);
            userResponse.setSuccess(true);
            userResponse.setMessage("Added user access");
        } catch (Exception ex) {
            userResponse.setSuccess(false);
            userResponse.setMessage(ex.getMessage());
            LOG.error("Error while adding user access rights");
            LOG.error("Exception", ex);
        }
        return userResponse;
    }

    // GET ACCESS LIST
    @RequestMapping(value = "/get/{id}/accesss", method = RequestMethod.POST)
    public UserResponse getAccessList(@PathVariable String id,
            @RequestParam String access_token) {
        final UserResponse userResponse = new UserResponse();
        try {
            AccessBal userAccessBal = new AccessBal(context);
            userResponse.setAccessRules(userAccessBal.getAccessList(id));
            userResponse.setSuccess(true);
        } catch (Exception ex) {
            userResponse.setSuccess(false);
            userResponse.setMessage(ex.getMessage());
            LOG.error("Error while adding user access rights");
            LOG.error("Exception", ex);
        }
        return userResponse;
    }

}
