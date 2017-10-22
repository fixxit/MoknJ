package nl.it.fixx.moknj.bal.core.access;

import java.util.List;
import nl.it.fixx.moknj.bal.BalBase;
import nl.it.fixx.moknj.bal.core.menu.MenuCoreBal;
import nl.it.fixx.moknj.bal.core.template.TemplateCoreBal;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.domain.core.access.Access;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.exception.AccessException;
import nl.it.fixx.moknj.repository.AccessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This BalBase is used to handle all user access related methods. Ideally all
 * business access layers should only have its main repository initialized as
 * class variable and only initialize the business layers it requires. The
 * reasoning behind this is if you change individual ball it will reflect on all
 * business access layers which are implemented across the board.
 *
 * @author adriaan
 */
@Service
public class AccessCoreBalImpl extends BalBase<AccessRepository> implements AccessCoreBal {

    private static final Logger LOG = LoggerFactory.getLogger(AccessCoreBalImpl.class);

    private final UserCoreBal userBal;
    private final TemplateCoreBal templateBal;
    private final MenuCoreBal menuBal;

    @Autowired
    public AccessCoreBalImpl(AccessRepository accessRepo, UserCoreBal userBal,
            TemplateCoreBal templateBal, MenuCoreBal menuBal) {
        super(accessRepo);
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
     */
    @Override
    public void addAccess(String userId, List<Access> newList, String token) {
        try {
            List<Access> currentList = getAccessList(userId);

            // Update and delete record which exist in db
            currentList.forEach((access) -> {
                if (!newList.contains(access)) {
                    deleteAccess(access.getId(), token);
                } else {
                    updateAccess(access, token);
                }
            });
            // find record which are new and not in the db
            for (Access access : newList) {
                if (!currentList.contains(access)) {
                    addAccess(access, token);
                }
            }

        } catch (Exception e) {
            LOG.error("Error while trying to insert user access rules");
            throw e;
        }
    }

    /**
     * Add access for menu template for a user.
     *
     * @param access
     * @param token
     */
    @Override
    public void addAccess(Access access, String token) {
        if (!repository.hasAccess(access.getUserId(), access.getMenuId(), access.getTemplateId())) {
            User loginUser = userBal.getUserByToken(token);
            if (!loginUser.getAuthorities().contains(ALL_ACCESS.toString())) {
                throw new AccessException("This user does not have " + ALL_ACCESS.toString());
            }

            LOG.debug("Adding access for user "
                    + "[" + userBal.getFullName(access.getUserId()) + "] on "
                    + "menu [" + menuBal.getDispayName(access.getMenuId()) + "] "
                    + "template [" + templateBal.getDispayName(access.getTemplateId()) + "]"
                    + "access [" + access.getRights() + "]");
            repository.save(access);
        } else {
            LOG.debug("Access for user "
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
     */
    @Override
    public void deleteAccess(String id, String token) {
        User loginUser = userBal.getUserByToken(token);
        if (!loginUser.getAuthorities().contains(ALL_ACCESS.toString())) {
            throw new AccessException("This user does not have " + ALL_ACCESS.toString());
        }

        Access access = repository.findOne(id);
        if (access != null) {
            LOG.debug("removing access for user "
                    + "[" + userBal.getFullName(access.getUserId()) + "] on "
                    + "menu [" + menuBal.getDispayName(access.getMenuId()) + "] "
                    + "template [" + templateBal.getDispayName(access.getTemplateId()) + "]");
        }

        repository.delete(id);
    }

    /**
     * Updates access for specific access rule. This is usually used when adding
     * more access as EDIT, READ, WRITE, DELETE
     *
     * @param access rule to add.
     * @param token
     */
    @Override
    public void updateAccess(Access access, String token) {
        User loginUser = userBal.getUserByToken(token);
        if (!loginUser.getAuthorities().contains(ALL_ACCESS.toString())) {
            throw new AccessException("This user does not have " + ALL_ACCESS.toString());
        }

        if (access == null || access.getId() == null || access.getId().isEmpty()) {
            LOG.debug("No access id provided to update access rules with");
            throw new AccessException("Invalid access [unknown] provided for "
                    + "access rule update!");
        }

        Access dbAcces = repository.findOne(access.getId());

        if (dbAcces == null) {
            LOG.debug("Access rule not found in db");
            throw new AccessException("Invalid access [" + access.getId() + "]s provided, this "
                    + "access does not exist in the db!");
        }

        dbAcces.setMenuId(access.getMenuId());
        dbAcces.setRights(access.getRights());
        dbAcces.setTemplateId(access.getTemplateId());
        dbAcces.setUserId(access.getUserId());

        repository.save(dbAcces);
    }

    /**
     * Checks if user has access to the menu and template being accessed.
     *
     * @param userId
     * @param menuId
     * @param templateId
     */
    @Override
    public void checkAccess(String userId, String menuId, String templateId) {
        if (!repository.hasAccess(userId, menuId, templateId)) {
            User user = userBal.getUserById(userId);
            if (user != null) {
                throw new AccessException("Unknown user access denied!");
            } else {
                throw new AccessException(userBal.getFullName(user) + " access denied!");
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
     */
    @Override
    public boolean hasAccess(User user, String menuId, String templateId) {
        return repository.hasAccess(user.getId(), menuId, templateId)
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
     */
    @Override
    public boolean hasAccess(User user, String menuId, String templateId, GlobalAccessRights right) {
        // If user is admin
        if (user.getAuthorities().contains(ALL_ACCESS.toString())) {
            return true;
        }
        // plain access check...
        if (repository.hasAccess(user.getId(), menuId, templateId)) {
            Access access = repository.getAccess(user.getId(), menuId, templateId);
            // empty check
            if (access.getRights() == null || access.getRights().isEmpty()) {
                return false;
            }
            return access.getRights().contains(right);
        }

        // always return false if logic above is fails.
        return false;
    }

    /**
     * Gets all the Access rights for the user saved to his user Id.
     *
     * @param userId
     * @return list of user access.
     */
    @Override
    public List<Access> getAccessList(String userId) {
        List<Access> accessList = repository.getAccessList(userId);
        if (accessList == null || accessList.isEmpty()) {
            LOG.debug("This user[" + userBal.getFullName(userId) + "] seems to "
                    + "have no access rules assigned to him...");
        }
        return accessList;
    }

}
