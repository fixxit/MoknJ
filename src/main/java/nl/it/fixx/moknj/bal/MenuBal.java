package nl.it.fixx.moknj.bal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.repository.MenuRepository;
import nl.it.fixx.moknj.service.SystemContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.comparator.NullSafeComparator;

/**
 *
 * @author adriaan
 */
public class MenuBal implements BusinessAccessLayer {

    private static final Logger LOG = LoggerFactory.getLogger(MenuBal.class);
    private final MenuRepository menuRep;

    public MenuBal(SystemContext factory) {
        this.menuRep = factory.getRepository(MenuRepository.class);
    }

    public Menu saveMenu(Menu payload) throws Exception {
        try {
            if (payload == null) {
                throw new Exception("No menu recieved to update/insert");
            }

            if (payload.getTemplates() == null
                    && payload.getTemplates().isEmpty()) {
                throw new Exception("No templates recieved to save. Aborting insert "
                        + "due to empty template list!");
            }

            // For updates if the type has a id then bypass the exists
            boolean bypassExists = false;
            if (payload.getId() != null) {
                bypassExists = true;
            }

            boolean exists = menuRep.existsByName(payload.getName());
            if (!exists || bypassExists) {
                Menu menu = menuRep.save(payload);
                return menu;
            } else {
                throw new Exception("Menu by name " + payload.getName() + " exists");
            }
        } catch (Exception e) {
            LOG.error("Error on saving menu", e);
            throw e;
        }
    }

    /**
     * Gets menu by UUID.
     *
     * @param id
     * @return menu.
     * @throws Exception
     */
    public Menu getMenuById(String id) throws Exception {
        try {
            if (id == null || id.isEmpty()) {
                LOG.debug("No menu id recieved to find menu");
                throw new Exception("No menu found, no menu id provided!");
            }

            Menu menu = menuRep.findOne(id);
            if (menu == null) {
                LOG.debug("no menu found for id[" + id + "]");
                throw new Exception("No menu found by this id[" + id + "]");
            }
            return menu;
        } catch (Exception e) {
            LOG.error("Error on Menu Bal", e);
            throw e;
        }
    }

    /**
     * Gets all the menu's
     *
     * @return list of menus.
     * @throws Exception
     */
    public List<Menu> getAllMenus() throws Exception {
        try {
            return menuRep.findAll();
        } catch (Exception e) {
            LOG.error("error getting all menus", e);
            throw e;
        }
    }

    /**
     * In the link table we have only the record id because of this and scope
     * challenge we need to find all menu which has templates that match the
     * template id. This just means that, that view can see all records across
     * menu which share the same template.
     *
     * @param templateId
     * @return list of menus
     * @throws Exception
     */
    public List<Menu> getMenusForTemplateId(String templateId) throws Exception {
        try {
            List<Menu> array = getAllMenus();
            List<Menu> menus = new ArrayList<>();

            array.forEach((menu) -> {
                List<Template> templates = new ArrayList<>();
                for (Template temp : menu.getTemplates()) {
                    if (templateId.equals(temp.getId())) {
                        templates.add(temp);
                    }
                }
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
        } catch (Exception e) {
            LOG.error("Error while get all menus for user token");
            throw e;
        }
    }

    /**
     * Returns the Display name for menu
     *
     * @param menu
     * @return
     * @throws Exception
     */
    public String getDispayName(Menu menu) throws Exception {
        if (menu == null) {
            throw new Exception("No menu object provided");
        }

        return menu.getName();
    }

    /**
     * Returns the display name for menu by menu UUID
     *
     * @param menuId
     * @return
     * @throws Exception
     */
    public String getDispayName(String menuId) throws Exception {
        return getDispayName(getMenuById(menuId));
    }

    /**
     * deletes the menu
     *
     * @param id
     * @throws Exception
     */
    public void deleteMenu(String id) throws Exception {
        try {
            Menu menu = getMenuById(id);
            if (menu == null) {
                throw new Exception("Menu does not exists in the db");
            }
            menuRep.delete(menu);
        } catch (Exception e) {
            LOG.error("error deleting menu[" + id + "]", e);
            throw e;
        }
    }
}
