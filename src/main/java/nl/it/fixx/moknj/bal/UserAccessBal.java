package nl.it.fixx.moknj.bal;

import java.util.List;
import nl.it.fixx.moknj.domain.core.access.Access;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.repository.AccessRepository;
import nl.it.fixx.moknj.repository.MenuRepository;
import nl.it.fixx.moknj.repository.TemplateRepository;
import nl.it.fixx.moknj.repository.UserRepository;
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
public class UserAccessBal implements AccessBal {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccessBal.class);
    // Main Repository
    private final AccessRepository accessRep;
    // business access layers
    private final UserBal userBal;
    private final TemplateBal templateBal;
    private final MenuBal menuBal;

    public UserAccessBal(AccessRepository accessRep, UserRepository userRep, MenuRepository menuRep, TemplateRepository templateRep) {
        this.accessRep = accessRep;
        this.userBal = new UserBal(userRep);
        this.templateBal = new TemplateBal(templateRep);
        this.menuBal = new MenuBal(menuRep);
    }

    /**
     * Add whole list of access rights for a user. This will automatically
     * remove any access records db which is not present in this list.
     *
     * @param userId
     * @param newList
     * @throws Exception
     */
    @Override
    public void addAccess(String userId, List<Access> newList) throws Exception {
        try {
            List<Access> currentList = getAccessList(userId);

            // Update and delete record which exist in db
            for (Access access : currentList) {
                if (!newList.contains(access)) {
                    deleteAccess(access.getId());
                } else {
                    updateAccess(access);
                }
            }
            // find record which are new and not in the db
            for (Access access : newList) {
                if (!currentList.contains(access)) {
                    addAccess(access);
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
     * @throws Exception on db related issues.
     */
    @Override
    public void addAccess(Access access) throws Exception {
        if (!accessRep.hasAccess(access.getUserId(), access.getMenuId(), access.getTemplateId())) {
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
            updateAccess(access);
        }
    }

    /**
     * Deletes user access by Access UUID
     *
     * @param id
     * @throws Exception
     */
    @Override
    public void deleteAccess(String id) throws Exception {
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
     * @throws Exception
     */
    @Override
    public void updateAccess(Access access) throws Exception {
        try {
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
            LOG.info("Error while updating user access rules");
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
    @Override
    public void hasAccess(String userId, String menuId, String templateId) throws Exception {
        if (!accessRep.hasAccess(userId, menuId, templateId)) {
            User user = userBal.getUserById(userId);
            if (user != null) {
                throw new Exception("Unknown user access denied!");
            } else {
                throw new Exception("Unknown user [" + userBal.getFullName(user) + "] access denied!");
            }
        }
    }

    /**
     * Gets all the Access rights for the user saved to his user Id.
     *
     * @param userId
     * @return list of user access.
     * @throws Exception
     */
    @Override
    public List<Access> getAccessList(String userId) throws Exception {
        List<Access> accessList = accessRep.getAccessList(userId);
        if (accessList == null || accessList.isEmpty()) {
            LOG.info("This user[" + userBal.getFullName(userId) + "] seems to "
                    + "have no access rules assigned to him...");
        }
        return accessList;
    }

}
