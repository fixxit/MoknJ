package nl.it.fixx.moknj.bal;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.domain.core.access.Access;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.repository.RepositoryFactory;
import nl.it.fixx.moknj.repository.UserRepository;
import nl.it.fixx.moknj.security.OAuth2SecurityConfig;
import static nl.it.fixx.moknj.security.OAuth2SecurityConfig.PSW_ENCODER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class should handle all logic which is related to business. Most of
 * controllers are running logic which should be moved to here.
 *
 * @author adriaan
 */
public class UserBal implements BusinessAccessLayer {

    private final BCryptPasswordEncoder passwordEncoder;
    private static final Logger LOG = LoggerFactory.getLogger(UserBal.class);

    public static String ADMIN_NAME = "fixxit";

    private final UserRepository userRep;
    private final RepositoryFactory factory;

    public UserBal(RepositoryFactory factory) {
        this.userRep = factory.getUserRep();
        this.passwordEncoder = PSW_ENCODER;
        this.factory = factory;
    }

    /**
     * Gets all users. Skips the admin user fixxit
     *
     * @return @throws Exception
     */
    public List<User> getAll() throws Exception {
        try {
            List<User> users = new ArrayList<>();
            if (userRep != null) {
                for (User user : userRep.findAll()) {
                    // if user has all access get all else only return his user
                    if (!user.isHidden()) {
                        if (!ADMIN_NAME.equals(user.getUserName())) {
                            users.add(user);
                        }
                    }
                }
            } else {
                throw new Exception("UserRepository is null");
            }
            return users;
        } catch (Exception ex) {
            LOG.error("Error while getting all users", ex);
            throw ex;
        }
    }

    /**
     * Gets all user the current user has access to.
     *
     * @param token
     * @return @throws Exception
     */
    public List<User> getAll(String token) throws Exception {
        try {
            return getAll(false, token);
        } catch (Exception ex) {
            LOG.error("Error while getting all users", ex);
            throw ex;
        }
    }

    /**
     * Get all user including admin indicated with isAdmin if true then
     * included. This will full users list or only the current user from the db.
     *
     * @param isAdmin
     * @param token
     * @return @throws Exception
     */
    public List<User> getAll(boolean isAdmin, String token) throws Exception {
        try {
            User loginUser = getUserByToken(token);
            List<User> users = new ArrayList<>();
            if (userRep != null) {
                for (User user : userRep.findAll()) {
                    // if user has all access get all else only return his user
                    if (loginUser.getAuthorities().contains(ALL_ACCESS.toString())) {
                        if (!user.isHidden()) {
                            if (isAdmin) {
                                if (!ADMIN_NAME.equals(user.getUserName())) {
                                    users.add(user);
                                }
                            } else {
                                users.add(user);
                            }
                        }
                    } else if (user.getId().equals(loginUser.getId())) {
                        users.add(user);
                        break;
                    }
                }
            } else {
                throw new Exception("UserRepository is null");
            }
            return users;
        } catch (Exception ex) {
            LOG.error("Error while getting all users", ex);
            throw ex;
        }
    }

    /**
     * Deletes the user. Only admin can execute this method.
     *
     * @param userId
     * @param token
     * @throws Exception
     */
    public void delete(String userId, String token) throws Exception {
        try {
            User loginUser = getUserByToken(token);
            if (!loginUser.getAuthorities().contains(ALL_ACCESS.toString())) {
                throw new Exception("This user does not have " + ALL_ACCESS.toString());
            }

            User resource = factory.getUserRep().findById(userId);
            List<Asset> assets = factory.getAssetRep().getAllByResourceId(userId);
            List<AssetLink> links = factory.getAssetLinkRep().getAllByResourceId(userId);

            if (!assets.isEmpty() || !links.isEmpty()) {
                resource.setHidden(true);
                save(resource, token);
            } else {
                // Delete all access rules relating to this user.
                List<Access> accessRules = factory.getAccessRep().getAccessList(userId);
                accessRules.stream().forEach((access) -> {
                    factory.getAccessRep().delete(access);
                });

                factory.getUserRep().delete(resource);
            }
        } catch (Exception e) {
            LOG.error("Error while deleting user [" + userId + "]", e);
            throw e;
        }
    }

    /**
     *
     * @param payload
     * @param token
     * @return
     * @throws Exception
     */
    public User save(User payload, String token) throws Exception {
        try {
            User loginUser = getUserByToken(token);
            if (!loginUser.getAuthorities().contains(ALL_ACCESS.toString())) {
                throw new Exception("This user does not have " + ALL_ACCESS.toString());
            }

            User dbResource = null;
            if (payload.getId() != null) {
                dbResource = userRep.findById(payload.getId());
            }

            List<User> results = userRep.findByFullname(
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
                        User indb = userRep.findByUserName(newUsername);
                        if (indb != null && indb.getId() != null) {
                            throw new Exception("Employee with a user name "
                                    + "" + newUsername + " already exists");
                        }
                    }
                }

                return userRep.save(payload);
            } else {
                throw new Exception("Employee by name "
                        + "" + payload.getFirstName() + " "
                        + "" + payload.getSurname() + " exists");
            }
        } catch (Exception ex) {
            LOG.error("Error while saving user", ex);
            throw ex;
        }
    }

    /**
     * Gets user by UUID.
     *
     * @param id
     * @return user.
     * @throws Exception
     */
    public User getUserById(String id) throws Exception {
        try {
            if (id == null || id.isEmpty()) {
                LOG.info("No user id recieved to find user");
                throw new Exception("No user found, no user id provided!");
            }

            User user = userRep.findOne(id);
            if (user == null) {
                LOG.info("no user found for id[" + id + "]");
                throw new Exception("No user found by this id[" + id + "]");
            }
            return user;
        } catch (Exception e) {
            LOG.error("Error on User Bal", e);
            throw e;
        }
    }

    /**
     * Returns a user object by his token which is assign to him on login.
     *
     * @param token
     * @return user
     * @throws Exception
     */
    public User getUserByToken(String token) throws Exception {
        try {
            if (token == null || token.isEmpty()) {
                LOG.info("No token recieved to find user");
                throw new Exception("No user found, no token provided!");
            }
            // Get user details who logged this employee using the token.
            User user = userRep.findByUserName(OAuth2SecurityConfig.getUserForToken(token));
            if (user == null) {
                LOG.info("no user found for token[" + token + "]");
                throw new Exception("No user found by this token[" + token + "]");
            }

            return user;
        } catch (Exception e) {
            LOG.error("Error on User Bal", e);
            throw e;
        }
    }

    /**
     *
     * @param user
     * @return
     * @throws Exception
     */
    public String getFullName(User user) throws Exception {
        if (user == null) {
            throw new Exception("No user object provided");
        }

        String fullname = "";
        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            fullname += user.getFirstName();
        }

        if (user.getSurname() != null && !user.getSurname().isEmpty()) {
            fullname += " " + user.getSurname();
        }

        return fullname;
    }

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public String getFullName(String userId) throws Exception {
        return getFullName(getUserById(userId));
    }

}
