package nl.it.fixx.moknj.bal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.repository.RepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

/**
 *
 * @author adriaan
 */
public class LinkBal implements BusinessAccessLayer {

    private static final Logger LOG = LoggerFactory.getLogger(LinkBal.class);
    private final RepositoryFactory factory;
    private final UserBal userBal;
    private final AssetBal assetBal;
    private final AccessBal accessBal;
    private final EmployeeBal employeeBal;

    public LinkBal(RepositoryFactory factory) {
        this.factory = factory;
        this.userBal = new UserBal(factory);
        this.assetBal = new AssetBal(factory);
        this.accessBal = new AccessBal(factory);
        this.employeeBal = new EmployeeBal(factory);
    }

    /**
     * Saves the link for a asset, this is used as audit in and out history
     *
     * @param payload
     * @param token
     * @return
     * @throws Exception
     */
    public AssetLink linkAssetToUser(AssetLink payload, String token) throws Exception {
        try {
            User user = userBal.getUserByToken(token);

            payload.setCreatedBy(user.getUserName());
            payload.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));

            // addAssetLink resource id to asset depending on isChecked
            if (payload.getAssetId() != null) {
                Asset asset = assetBal.get(payload.getAssetId());
                if (asset != null) {
                    String templateId = asset.getTypeId();
                    String menuId = asset.getMenuScopeIds().get(0);

                    // check if user has any access asigned to him.
                    if (!accessBal.hasAccess(user, menuId, templateId, GlobalAccessRights.EDIT)) {
                        throw new Exception("Unable to check out/in this asset. "
                                + "This user does not have "
                                + "" + GlobalAccessRights.EDIT.toString()
                                + " rights");
                    }

                    if (payload.isChecked()) {
                        asset.setResourceId(payload.getResourceId());
                    } else {
                        asset.setResourceId(null);
                    }

                    asset.setLastModifiedBy(user.getUserName());
                    asset.setLastModifiedDate(
                            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                    factory.getAssetRep().save(asset);
                }
            }
            return factory.getAssetLinkRep().save(payload);
        } catch (Exception e) {
            LOG.error("Error while trying to link user to asset", e);
            throw e;
        }
    }

    /**
     * Gets all the employee audits for the current user token (access check
     * included), this is only used as audit log.
     *
     * @param token
     * @return
     * @throws Exception
     */
    public List<EmployeeLink> getAllEmployeeLinks(String token) throws Exception {
        try {
            return checkEmployeeRecordAccess(factory.getEmployeeLinkRep().findAll(),
                    userBal.getUserByToken(token));
        } catch (Exception e) {
            LOG.error("Error while trying to find all employee links", e);
            throw e;
        }
    }

    /**
     * Gets all the audit for specific employee id and for the current user
     * token (access check included).
     *
     * @param employeeId
     * @param token
     * @return
     * @throws Exception
     */
    public List<EmployeeLink> getAllEmployeeLinksForEmployee(String employeeId, String token) throws Exception {
        try {
            return checkEmployeeRecordAccess(factory.getEmployeeLinkRep().getAllByEmployeeId(employeeId),
                    userBal.getUserByToken(token));
        } catch (Exception e) {
            LOG.error("Error while trying to find all employee links", e);
            throw e;
        }

    }

    /**
     * This not one of the the proudest pieces of code, but this code works,
     * this check the audit list which is passed for scope challenge and access
     * rights. Scope challenge basically adds records from the same template
     * used over different menu to the list and bypasses the access rights.
     *
     * @param empLinks
     * @param user
     * @return
     * @throws Exception
     */
    public List<EmployeeLink> checkEmployeeRecordAccess(List<EmployeeLink> empLinks, User user) throws Exception {
        try {
            Set<EmployeeLink> links = new HashSet<>();
            for (EmployeeLink link : empLinks) {
                if (factory.getEmployeeRep().exists(link.getEmployeeId())) {
                    if (!user.getAuthorities().contains(ALL_ACCESS.toString())) {
                        Employee employee = employeeBal.get(link.getEmployeeId());
                        String templateId = employee.getTypeId();
                        String menuId = employee.getMenuScopeIds().get(0);
                        // Get template from menu item
                        Template memTemplate = getMenuTemplate(menuId, templateId);
                        if (memTemplate == null) {
                            continue;
                        }
                        // if scope challenge then get all menus ids with this template
                        if (!memTemplate.isAllowScopeChallenge()) {
                            List<Menu> menus = new MenuBal(factory).getMenusForTemplateId(templateId);
                            for (Menu menu : menus) {
                                List<EmployeeLink> menuLinks = factory.getEmployeeLinkRep().findAll(new Sort(Sort.Direction.DESC, "createdDate"));
                                for (EmployeeLink menuLink : menuLinks) {
                                    if (factory.getEmployeeRep().exists(menuLink.getEmployeeId())) {
                                        Employee memEmployee = employeeBal.get(menuLink.getEmployeeId());
                                        String challengeTempId = memEmployee.getTypeId();
                                        if (challengeTempId.equals(templateId)) {
                                            if (accessBal.hasAccess(user, menu.getId(),
                                                    templateId, GlobalAccessRights.VIEW)) {
                                                // Sets the user full name for display purpesus
                                                User linkedUser = factory.getUserRep().findById(memEmployee.getResourceId());
                                                String fullname = linkedUser.getFirstName() + " " + linkedUser.getSurname();
                                                menuLink.setUser(fullname);
                                                menuLink.setActionValue(menuLink.getAction().getDisplayValue());
                                                links.add(menuLink);
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (accessBal.hasAccess(user, menuId,
                                templateId, GlobalAccessRights.VIEW)) {
                            // Sets the user full name for display purpesus
                            User linkedUser = factory.getUserRep().findById(employee.getResourceId());
                            String fullname = linkedUser.getFirstName() + " " + linkedUser.getSurname();
                            link.setUser(fullname);
                            link.setActionValue(link.getAction().getDisplayValue());
                            links.add(link);
                        }
                    } else {
                        Employee employee = employeeBal.get(link.getEmployeeId());
                        User linkedUser = factory.getUserRep().findById(employee.getResourceId());
                        String fullname = linkedUser.getFirstName() + " " + linkedUser.getSurname();
                        link.setUser(fullname);
                        link.setActionValue(link.getAction().getDisplayValue());
                        links.add(link);
                    }
                }
            }
            return links.stream().collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Error while trying to check user access to employee", e);
            throw e;
        }
    }

    /**
     * This not one of the the proudest pieces of code, but this code works,
     * this check the audit list which is passed for scope challenge and access
     * rights. Scope challenge basically adds records from the same template
     * used over different menu to the list and bypasses the access rights.
     *
     * @param assetLinks
     * @param user
     * @return
     * @throws Exception
     */
    public List<AssetLink> checkAssetRecordAccess(List<AssetLink> assetLinks, User user) throws Exception {
        try {
            Set<AssetLink> links = new HashSet<>();
            for (AssetLink link : assetLinks) {
                if (!user.getAuthorities().contains(ALL_ACCESS.toString())) {
                    Asset asset = assetBal.get(link.getAssetId());
                    String templateId = asset.getTypeId();
                    String menuId = asset.getMenuScopeIds().get(0);
                    // Get template from menu item
                    Template memTemplate = getMenuTemplate(menuId, templateId);
                    if (memTemplate == null) {
                        continue;
                    }
                    // if scope challenge then get all menus ids with this template
                    if (!memTemplate.isAllowScopeChallenge()) {
                        List<Menu> menus = new MenuBal(factory).getMenusForTemplateId(templateId);
                        for (Menu menu : menus) {
                            List<AssetLink> menuLinks = factory.getAssetLinkRep().findAll(new Sort(Sort.Direction.DESC, "createdDate"));
                            for (AssetLink menuLink : menuLinks) {
                                String challengeTempId = assetBal.get(menuLink.getAssetId()).getTypeId();
                                if (challengeTempId.equals(templateId)) {
                                    if (accessBal.hasAccess(user, menu.getId(),
                                            templateId, GlobalAccessRights.VIEW)) {
                                        links.add(menuLink);
                                    }
                                }
                            }
                        }
                    } else if (accessBal.hasAccess(user, menuId,
                            templateId, GlobalAccessRights.VIEW)) {
                        links.add(link);
                    }
                } else {
                    links.add(link);
                }
            }
            return links.stream().collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Error while trying to check user access to employee", e);
            throw e;
        }
    }

    /**
     * Gets Template for the menu and template id, this is to get the saved
     * settings to the template as menu template can differ to plain template as
     * it has scope settings included.
     *
     * @param menuId
     * @param templateId
     * @return
     * @throws Exception
     */
    public Template getMenuTemplate(String menuId, String templateId) throws Exception {
        Menu menu = new MenuBal(factory).getMenuById(menuId);
        List<Template> menuTemplates = menu.getTemplates();
        for (Template menuTemplate : menuTemplates) {
            if (templateId.equals(menuTemplate.getId())) {
                return menuTemplate;
            }
        }
        return null;
    }

    /**
     * Gets all asset in and out audit links for user token.
     *
     * @param token
     * @return
     * @throws Exception
     */
    public List<AssetLink> getAllAssetLinks(String token) throws Exception {
        try {
            return checkAssetRecordAccess(factory.getAssetLinkRep().findAll(
                    new Sort(Sort.Direction.DESC, "createdDate")),
                    userBal.getUserByToken(token));
        } catch (Exception e) {
            LOG.error("Error while trying to find all asset links", e);
            throw e;
        }
    }

    /**
     * Gets all in out audit logs for asset id and token (access check).
     *
     * @param assetId
     * @param token
     * @return
     * @throws Exception
     */
    public List<AssetLink> getAllAssetLinksByAssetId(String assetId, String token) throws Exception {
        try {
            return checkAssetRecordAccess(factory.getAssetLinkRep().getAllByAssetId(assetId),
                    userBal.getUserByToken(token));
        } catch (Exception e) {
            LOG.error("Error while trying to find all asset links", e);
            throw e;
        }
    }

    /**
     * Gets all audit logs for user id and token.
     *
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    public List<AssetLink> getAllAssetLinksByResourceId(String userId, String token) throws Exception {
        try {
            return checkAssetRecordAccess(factory.getAssetLinkRep().getAllByResourceId(userId),
                    userBal.getUserByToken(token));
        } catch (Exception e) {
            LOG.error("Error while trying to find all asset links", e);
            throw e;
        }
    }

}
