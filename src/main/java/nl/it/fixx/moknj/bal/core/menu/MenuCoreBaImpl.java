package nl.it.fixx.moknj.bal.core.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.it.fixx.moknj.bal.BalBase;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.NullSafeComparator;

@Service
public class MenuCoreBaImpl extends BalBase<MenuRepository> implements MenuCoreBal {

    private static final Logger LOG = LoggerFactory.getLogger(MenuCoreBaImpl.class);

    @Autowired
    public MenuCoreBaImpl(MenuRepository menuRepo) {
        super(menuRepo);
    }

    @Override
    public Menu saveMenu(Menu payload) {
        try {
            if (payload == null) {
                throw new BalException("No menu recieved to update/insert");
            }

            if (payload.getTemplates() == null
                    && payload.getTemplates().isEmpty()) {
                throw new BalException("No templates recieved to save. Aborting insert "
                        + "due to empty template list!");
            }

            // For updates if the type has a id then bypass the exists
            boolean bypassExists = false;
            if (payload.getId() != null) {
                bypassExists = true;
            }

            boolean exists = repository.existsByName(payload.getName());
            if (!exists || bypassExists) {
                Menu menu = repository.save(payload);
                return menu;
            } else {
                throw new BalException("Menu by name " + payload.getName() + " exists");
            }
        } catch (BalException e) {
            LOG.error("Error on saving menu", e);
            throw e;
        }
    }

    /**
     * Gets menu by UUID.
     *
     * @param id
     * @return menu.
     */
    @Override
    public Menu getMenuById(String id) {
        try {
            if (id == null || id.isEmpty()) {
                LOG.debug("No menu id recieved to find menu");
                throw new BalException("No menu found, no menu id provided!");
            }

            Menu menu = repository.findOne(id);
            if (menu == null) {
                LOG.debug("no menu found for id[" + id + "]");
                throw new BalException("No menu found by this id[" + id + "]");
            }
            return menu;
        } catch (BalException e) {
            LOG.error("Error on Menu Bal", e);
            throw e;
        }
    }

    /**
     * Gets all the menu's
     *
     * @return list of menus.
     *
     */
    @Override
    public List<Menu> getAllMenus() {
        return repository.findAll();
    }

    /**
     * In the link table we have only the record id because of this and scope
     * challenge we need to find all menu which has templates that match the
     * template id. This just means that, that view can see all records across
     * menu which share the same template.
     *
     * @param templateId
     * @return list of menus
     */
    @Override
    public List<Menu> getMenusForTemplateId(String templateId) {
        List<Menu> array = getAllMenus();
        List<Menu> menus = new ArrayList<>();

        array.forEach((menu) -> {
            List<Template> templates = new ArrayList<>();
            menu.getTemplates().stream().filter((temp)
                    -> (templateId.equals(temp.getId()))).forEachOrdered((temp) -> {
                templates.add(temp);
            });
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
     * Returns the Display name for menu
     *
     * @param menu
     * @return
     */
    @Override
    public String getDispayName(Menu menu) {
        if (menu == null) {
            throw new BalException("No menu object provided");
        }

        return menu.getName();
    }

    /**
     * Returns the display name for menu by menu UUID
     *
     * @param menuId
     * @return
     */
    @Override
    public String getDispayName(String menuId) {
        return getDispayName(getMenuById(menuId));
    }

    /**
     * deletes the menu
     *
     * @param id
     */
    @Override
    public void deleteMenu(String id) {
        try {
            Menu menu = getMenuById(id);
            if (menu == null) {
                throw new BalException("Menu does not exists in the db");
            }
            repository.delete(menu);
        } catch (BalException e) {
            LOG.error("error deleting menu[" + id + "]", e);
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
     */
    @Override
    public Template getMenuTemplate(String menuId, String templateId) {
        Menu menu = getMenuById(menuId);
        List<Template> menuTemplates = menu.getTemplates();
        for (Template menuTemplate : menuTemplates) {
            if (templateId.equals(menuTemplate.getId())) {
                return menuTemplate;
            }
        }
        return null;
    }
}
