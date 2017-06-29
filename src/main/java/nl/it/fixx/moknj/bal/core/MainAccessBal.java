package nl.it.fixx.moknj.bal.core;

import nl.it.fixx.moknj.bal.module.asset.AssetBal;
import nl.it.fixx.moknj.bal.module.employee.EmployeeBal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.global.GlobalTemplateType;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.exception.AccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.NullSafeComparator;
import nl.it.fixx.moknj.bal.BAL;

/**
 * This is the main access bal used to access for any action related to template
 * and menu.
 *
 * @author adriaan
 */
@Service
public class MainAccessBal implements BAL {

    private static final Logger LOG = LoggerFactory.getLogger(MainAccessBal.class);

    private final AccessBal accessBal;
    private final UserBal userBal;
    private final MenuBal menuBal;
    private final TemplateBal tempBal;
    private final AssetBal assetBal;
    private final EmployeeBal employeeBal;

    @Autowired
    public MainAccessBal(AccessBal accessBal, UserBal userBal, MenuBal menuBal,
            TemplateBal tempBal, AssetBal assetBal, EmployeeBal employeeBal) {
        this.accessBal = accessBal;
        this.userBal = userBal;
        this.menuBal = menuBal;
        this.tempBal = tempBal;
        this.assetBal = assetBal;
        this.employeeBal = employeeBal;
    }

    /**
     * Saves menu but only if the user is a admin.
     *
     * @param payload
     * @param token
     * @return
     * @throws Exception
     */
    public Menu saveMenu(Menu payload, String token) throws Exception {
        try {
            User user = userBal.getUserByToken(token);
            if (user.getAuthorities().contains(ALL_ACCESS.toString())) {
                return menuBal.saveMenu(payload);
            }
            throw new AccessException("This user does not have " + ALL_ACCESS.toString());
        } catch (AccessException e) {
            LOG.error("Error on saving menu", e);
            throw e;
        }
    }

    /**
     * Saves template but only if the user is a admin.
     *
     * @param payload
     * @param token
     * @return
     * @throws Exception
     */
    public Template saveTemplate(Template payload, String token) throws Exception {
        try {
            User user = userBal.getUserByToken(token);
            if (user.getAuthorities().contains(ALL_ACCESS.toString())) {
                return tempBal.saveTemplate(payload);
            }
            throw new AccessException("This user does not have " + ALL_ACCESS.toString());
        } catch (Exception e) {
            LOG.error("Error on saving template", e);
            throw e;
        }
    }

    /**
     * Gets the menu and templates which user has access rights to.
     *
     * @param menu
     * @param user
     * @return list of templates
     * @throws Exception
     */
    private List<Template> getMenuTemplates(Menu menu, User user, boolean bypassHidden) {
        LOG.debug("========================================");
        LOG.debug("Menu [" + menu.getName() + "] ");
        List<Template> templates = new ArrayList<>();
        for (Template temp : menu.getTemplates()) {
            LOG.debug("Temp [" + temp.getName() + "] ");
            if (tempBal.exists(temp.getId())) {
                Template globalTemplate = tempBal.getTemplateById(temp.getId());
                if (globalTemplate != null) {
                    // sets teh template menu rules down to template passed back
                    globalTemplate.setAllowScopeChallenge(temp.isAllowScopeChallenge());
                    // check if template is hidden or has access
                    boolean access = accessBal.hasAccess(user,
                            menu.getId(), globalTemplate.getId(), GlobalAccessRights.VIEW);

                    LOG.debug("User [" + user.getUserName() + "] has access : " + access);
                    // check if template is hidden
                    if ((!bypassHidden && globalTemplate.isHidden()) || !access) {
                        LOG.debug("skipping template [" + globalTemplate.getName() + "] for menu [" + menu.getName() + "]");
                    } else {
                        // update template with db version
                        templates.add(Template.copy(globalTemplate));
                    }
                }
            }
        }
        return templates;
    }

    /**
     * Gets all the templates with no menu or access check
     *
     * @param menu
     * @param user
     * @return list of templates
     * @throws Exception
     */
    private List<Template> getAllTemplates() throws Exception {
        List<Template> templates = new ArrayList<>();
        for (Template temp : tempBal.getAllTemplates()) {
//            LOG.info("found Temp [" + temp.getName() + "] with no menu");
            if (tempBal.exists(temp.getId())) {
                Template globalTemplate = tempBal.getTemplateById(temp.getId());
                if (globalTemplate != null) {
                    // sets teh template menu rules down to template passed back
                    globalTemplate.setAllowScopeChallenge(temp.isAllowScopeChallenge());
                    // check if template is hidden
                    if (globalTemplate.isHidden()) {
//                        LOG.info("skipping template [" + globalTemplate.getName() + "]");
                    } else {
                        // update template with db version
                        templates.add(Template.copy(globalTemplate));
                    }
                }
            }
        }
        return templates;
    }

    /**
     *
     * @param templateId
     * @param token
     * @return
     * @throws Exception
     */
    public Template getTemplate(String templateId, String token) throws Exception {
        try {
            User user = userBal.getUserByToken(token);
            return !(user.getAuthorities().contains(ALL_ACCESS.toString()))
                    ? findTemplateByTokenAndId(templateId, user)
                    : tempBal.getTemplateById(templateId);
        } catch (Exception e) {
            LOG.error("Error on saving template", e);
            throw e;
        }
    }

    /**
     * Retrieves the menu and its templates based on the access level, if the
     * user has access it will return the menu.
     *
     * @param menuId
     * @param token
     * @return menu
     */
    public Menu getMenu(String menuId, String token) {
        User user = userBal.getUserByToken(token);
        Menu menu = menuBal.getMenuById(menuId);
        if (menu != null) {
            menu.setTemplates(getMenuTemplates(menu, user, false));
            return menu;
        } else {
            throw new AccessException("Menu not found");
        }
    }

    /**
     * Retrieves the all menus the the user has view access or admin access to.
     *
     * @param access_token
     * @return list of menus
     */
    public List<Menu> getUserMenus(String access_token) {
        User user = userBal.getUserByToken(access_token);
        List<Menu> array = menuBal.getAllMenus();
        List<Menu> menus = new ArrayList<>();

        array.forEach((menu) -> {
            List<Template> templates = getMenuTemplates(menu, user, false);
            if (!templates.isEmpty()) {
                menu.setTemplates(templates);
                menus.add(menu);
            }
        });

        Collections.sort(menus, (Menu a1, Menu a2) -> {
            return new NullSafeComparator<>(String::compareTo,
                    true).compare(a1.getIndex(), a2.getIndex());
        });

        return menus;
    }

    /**
     * Delete menu record
     *
     * @param id
     * @param token
     * @throws java.lang.Exception
     */
    public void deleteMenu(String id, String token) throws Exception {
        try {
            User user = userBal.getUserByToken(token);
            if (user.getAuthorities().contains(ALL_ACCESS.toString())) {
                menuBal.deleteMenu(id);
            }
            throw new AccessException("Unable to delete. "
                    + "This user does not have " + ALL_ACCESS.toString());
        } catch (AccessException e) {
            LOG.error("Error while get all menus for user token");
            throw e;
        }
    }

    /**
     * Returns all Templates user has access to.
     *
     * @param token
     * @return
     * @throws java.lang.Exception
     */
    public List<Template> getAllTemplatesForToken(String token) throws Exception {
        try {
            User user = userBal.getUserByToken(token);
            Set<Template> values = new HashSet<>();
            List<Menu> menus = menuBal.getAllMenus();
            for (Menu menu : menus) {
                List<Template> tempates = getMenuTemplates(menu, user, false);
                tempates.stream().forEach((template) -> {
                    values.add(template);
                });
            }

            if (user.getAuthorities().contains(ALL_ACCESS.toString())) {
                getAllTemplates().stream().forEach((template) -> {
                    values.add(template);
                });
            }

            return values.stream().collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Error while all templates for user token[" + token + "]");
            throw e;
        }
    }

    /**
     * Returns all Templates user has access to. Loops through all menu records
     * and templates the user has access to and returns the 1st instance of
     * template which shares same template Id
     *
     * @param templateId
     * @param user
     * @return
     */
    public Template findTemplateByTokenAndId(String templateId, User user) {
        List<Menu> menus = menuBal.getAllMenus();
        if (tempBal.exists(templateId)) {
            for (Menu menu : menus) {
                List<Template> templates = getMenuTemplates(menu, user, false);
                for (Template template : templates) {
                    if (template.getId().equals(templateId)) {
                        return template;
                    }
                }
            }
        }
        LOG.debug("This template[" + templateId + "] seems to be deleted");
        return null;
    }

    /**
     *
     * @param id
     * @param cascade
     * @param token
     * @throws java.lang.Exception
     */
    public void deleteTemplate(String id, boolean cascade, String token) throws Exception {
        try {
            User user = userBal.getUserByToken(token);
            if (!user.getAuthorities().contains(ALL_ACCESS.toString())) {
                throw new AccessException("Unable to delete template. "
                        + "This user does not have " + ALL_ACCESS.toString());
            }

            if (cascade) {
                List<Menu> menus = menuBal.getMenusForTemplateId(id);
                for (Menu menu : menus) {
                    for (Iterator<Template> iterator = menu.getTemplates().iterator(); iterator.hasNext();) {
                        Template template = iterator.next();
                        if (template.getId().equals(id)) {
                            if (GlobalTemplateType.GBL_TT_ASSET.equals(template.getTemplateType())) {
                                List<Asset> assets = assetBal.getAll(template.getId(), menu.getId(), token);
                                for (Asset asset : assets) {
                                    assetBal.delete(asset, menu.getId(), token, true);
                                    iterator.remove();
                                    menuBal.saveMenu(menu);
                                }
                            } else if (GlobalTemplateType.GBL_TT_EMPLOYEE.equals(template.getTemplateType())) {
                                List<Employee> employees = employeeBal.getAll(template.getId(), menu.getId(), token);
                                for (Employee emp : employees) {
                                    employeeBal.delete(emp, menu.getId(), token, true);
                                    iterator.remove();
                                    menuBal.saveMenu(menu);
                                }
                            }
                        }
                    }
                }
            }
//            LOG.info("deleting template[" + id + "]");
            tempBal.deleteTemplate(id, cascade);
        } catch (Exception e) {
            LOG.error("Error while trying to delete template[" + id + "]", e);
            throw e;
        }
    }

}
