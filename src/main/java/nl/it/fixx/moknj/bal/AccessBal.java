package nl.it.fixx.moknj.bal;

import java.util.List;
import nl.it.fixx.moknj.domain.core.access.Access;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.repository.AccessRepository;
import nl.it.fixx.moknj.repository.RepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This BAL is used to handle all user access related methods. Ideally all
 * business access layers should only have its main repository initialized as
 * class variable and only initialize the business layers it requires. The
 * reasoning behind this is if you change individual ball it will reflect on all
 * business access layers which are implemented across the board.
 *
 * @author adriaan
 */
public class AccessBal {

    private static final Logger LOG = LoggerFactory.getLogger(AccessBal.class);

    private final AccessRepository accessRep;
    private final UserBal userBal;
    private final TemplateBal templateBal;
    private final MenuBal menuBal;

    public AccessBal(RepositoryFactory factory) {
        this.accessRep = factory.getAccessRep();
        this.userBal = new UserBal(factory);
        this.menuBal = new MenuBal(factory);
        this.templateBal = new TemplateBal(factory);
    }

    public AccessBal(RepositoryFactory factory, UserBal userBal,
            TemplateBal templateBal, MenuBal menuBal) {
        this.accessRep = factory.getAccessRep();
        this.userBal = userBal;
        this.templateBal = templateBal;
        this.menuBal = menuBal;
    }

    /**
     * Add whole list of access rights for a user. This will automatically
     * remove any access records db which is not present in this list.
     *
     * @param userId
     * @param newList
     * @param token
     * @throws Exception
     */
    public void addAccess(String userId, List<Access> newList, String token) throws Exception {
        try {
            List<Access> currentList = getAccessList(userId);

            // Update and delete record which exist in db
            for (Access access : currentList) {
                if (!newList.contains(access)) {
                    deleteAccess(access.getId(), token);
                } else {
                    updateAccess(access, token);
                }
            }
            // find record which are new and not in the db
            for (Access access : newList) {
                if (!currentList.contains(access)) {
                    addAccess(access, token);
                }
            }

        } catch (Exception e) {
            LOG.info("Error while trying to insert user access rules");
            throw e;
        }
    }

    /**
     * Add access for menu template for a user.
     *
     * @param access
     * @param token
     * @throws Exception on db related issues.
     */
    public void addAccess(Access access, String token) throws Exception {
        if (!accessRep.hasAccess(access.getUserId(), access.getMenuId(), access.getTemplateId())) {
            User loginUser = userBal.getUserByToken(token);
            if (!loginUser.getAuthorities().contains(ALL_ACCESS.toString())) {
                throw new Exception("This user does not have " + ALL_ACCESS.toString());
            }

            LOG.info("Adding access for user "
                    + "[" + userBal.getFullName(access.getUserId()) + "] on "
                    + "menu [" + menuBal.getDispayName(access.getMenuId()) + "] "
                    + "template [" + templateBal.getDispayName(access.getTemplateId()) + "]"
                    + "access [" + access.getRights() + "]");
            accessRep.save(access);
        } else {
            LOG.info("Access for user "
                    + "[" + userBal.getFullName(access.getUserId()) + "] on "
                    + "menu [" + menuBal.getDispayName(access.getMenuId()) + "] "
                    + "template [" + templateBal.getDispayName(access.getTemplateId()) + "] "
                    + "access [" + access.getRights() + "]"
                    + "exists, updating access");
            updateAccess(access, token);
        }
    }

    /**
     * Deletes user access by Access UUID
     *
     * @param id
     * @param token
     * @throws Exception
     */
    public void deleteAccess(String id, String token) throws Exception {
        User loginUser = userBal.getUserByToken(token);
        if (!loginUser.getAuthorities().contains(ALL_ACCESS.toString())) {
            throw new Exception("This user does not have " + ALL_ACCESS.toString());
        }

        Access access = accessRep.findOne(id);
        if (access != null) {
            LOG.info("removing access for user "
                    + "[" + userBal.getFullName(access.getUserId()) + "] on "
                    + "menu [" + menuBal.getDispayName(access.getMenuId()) + "] "
                    + "template [" + templateBal.getDispayName(access.getTemplateId()) + "]");
        }

        accessRep.delete(id);
    }

    /**
     * Updates access for specific access rule. This is usually used when adding
     * more access as EDIT, READ, WRITE, DELETE
     *
     * @param access rule to add.
     * @param token
     * @throws Exception
     */
    public void updateAccess(Access access, String token) throws Exception {
        try {
            User loginUser = userBal.getUserByToken(token);
            if (!loginUser.getAuthorities().contains(ALL_ACCESS.toString())) {
                throw new Exception("This user does not have " + ALL_ACCESS.toString());
            }

            if (access == null || access.getId() == null || access.getId().isEmpty()) {
                LOG.info("No access id provided to update access rules with");
                throw new Exception("Invalid access [unknown] provided for "
                        + "access rule update!");
            }

            Access dbAcces = accessRep.findOne(access.getId());

            if (dbAcces == null) {
                LOG.info("Access rule not found in db");
                throw new Exception("Invalid access [" + access.getId() + "]s provided, this "
                        + "access does not exist in the db!");
            }

            dbAcces.setMenuId(access.getMenuId());
            dbAcces.setRights(access.getRights());
            dbAcces.setTemplateId(access.getTemplateId());
            dbAcces.setUserId(access.getUserId());

            accessRep.save(dbAcces);
        } catch (Exception e) {
            LOG.error("Error while updating user access rules");
            throw e;
        }
    }

    /**
     * Checks if user has access to the menu and template being accessed.
     *
     * @param userId
     * @param menuId
     * @param templateId
     * @throws Exception if user does not have access.
     */
    public void checkAccess(String userId, String menuId, String templateId) throws Exception {
        if (!accessRep.hasAccess(userId, menuId, templateId)) {
            User user = userBal.getUserById(userId);
            if (user != null) {
                throw new Exception("Unknown user access denied!");
            } else {
                throw new Exception(userBal.getFullName(user) + " access denied!");
            }
        }
    }

    /**
     * Checks if user has any access access to the menu and template being
     * accessed.
     *
     * @param user
     * @param menuId
     * @param templateId
     * @return true if user has access
     * @throws java.lang.Exception
     */
    public boolean hasAccess(User user, String menuId, String templateId) throws Exception {
        return accessRep.hasAccess(user.getId(), menuId, templateId)
                || user.getAuthorities().contains(ALL_ACCESS.toString());
    }

    /**
     * Checks if user has the specific access available for the menu and
     * template he is accessing.
     *
     * @param user
     * @param menuId
     * @param templateId
     * @param right
     * @return true if user has access
     * @throws java.lang.Exception
     */
    public boolean hasAccess(User user, String menuId, String templateId, GlobalAccessRights right) throws Exception {
        try {
            // If user is admin
            if (user.getAuthorities().contains(ALL_ACCESS.toString())) {
                return true;
            }
            // plain access check...
            if (accessRep.hasAccess(user.getId(), menuId, templateId)) {
                Access access = accessRep.getAccess(user.getId(), menuId, templateId);
                // empty check
                if (access.getRights() == null || access.getRights().isEmpty()) {
                    return false;
                }
                return access.getRights().contains(right);
            }

            // always return false if logic above is fails.
            return false;
        } catch (Exception e) {
            LOG.error("Error while updating user access rules");
            throw e;
        }
    }

    /**
     * Gets all the Access rights for the user saved to his user Id.
     *
     * @param userId
     * @return list of user access.
     * @throws Exception
     */
    public List<Access> getAccessList(String userId) throws Exception {
        List<Access> accessList = accessRep.getAccessList(userId);
        if (accessList == null || accessList.isEmpty()) {
            LOG.info("This user[" + userBal.getFullName(userId) + "] seems to "
                    + "have no access rules assigned to him...");
        }
        return accessList;
    }

}
