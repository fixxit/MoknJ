package nl.it.fixx.moknj.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.it.fixx.moknj.domain.core.global.GlobalMenuType;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.menu.MenuType;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.repository.MenuRepository;
import nl.it.fixx.moknj.repository.TemplateRepository;
import nl.it.fixx.moknj.response.MenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.comparator.NullSafeComparator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adriaan
 */
@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/menu")
public class MenuContoller {

    @Autowired
    private MenuRepository menuRep;
    @Autowired
    private TemplateRepository templateRep;

    /**
     *
     * @param payload
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    MenuResponse add(@RequestBody Menu payload) {
        MenuResponse response = new MenuResponse();
        response.setAction("POST");
        response.setMethod("/menu/add");

        if (payload.getTemplates() == null && payload.getTemplates().isEmpty()) {
            response.setMessage("No templates recieved to save. Aborting insert "
                    + "due to empty template list!");
            return response;
        }

        try {
            // For updates if the type has a id then bypass the exists
            boolean bypassExists = false;
            if (payload.getId() != null) {
                bypassExists = true;
            }

            boolean exists = menuRep.existsByName(payload.getName());
            if (!exists || bypassExists) {
                Menu menu = menuRep.save(payload);
                response.setSuccess(menu != null);
                response.setMessage("Saved menu[" + menu.getId() + "]");
                response.setMenu(menu);
            } else {
                response.setSuccess(false);
                response.setMessage("Menu by name " + payload.getName() + " exists");
            }
        } catch (IllegalArgumentException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    /**
     * Gets menu by id.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public @ResponseBody
    MenuResponse get(@PathVariable String id) {
        MenuResponse response = new MenuResponse();
        Menu menu = menuRep.findOne(id);
        if (menu != null) {
            List<Template> templates = menu.getTemplates();
            for (int tempIndex = 0; tempIndex < templates.size(); tempIndex++) {
                Template temp = templates.get(tempIndex);

                Template globalTemplate = templateRep.findOne(temp.getId());
                if (globalTemplate != null) {
                    // sets teh template menu rules down to template passed back
                    globalTemplate.setAllowScopeChallenge(temp.isAllowScopeChallenge());

                    // check if template is hidden
                    if (globalTemplate.isHidden()) {
                        templates.remove(tempIndex);
                    } else {
                        templates.set(tempIndex, globalTemplate);
                    }
                } else {
                    templates.remove(tempIndex);
                }
            }
            response.setMenu(menu);
        } else {
            response.setSuccess(false);
            response.setMessage("Menu not found could be delete?");
        }
        return response;
    }

    /**
     * Used to get all mapped menu's from db.
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public @ResponseBody
    MenuResponse all() {
        MenuResponse response = new MenuResponse();
        List<Menu> array = menuRep.findAll();
        List<Menu> menus = new ArrayList<>();

        array.stream().forEach((menu) -> {
            List<Template> templates = menu.getTemplates();
            for (int tempIndex = 0; tempIndex < templates.size(); tempIndex++) {
                Template temp = templates.get(tempIndex);

                Template globalTemplate = templateRep.findOne(temp.getId());
                // check if template is in db
                if (globalTemplate != null) {
                    // sets teh template menu rules down to template passed back
                    globalTemplate.setAllowScopeChallenge(temp.isAllowScopeChallenge());

                    // check if template is hidden
                    if (globalTemplate.isHidden()) {
                        // remove from list if so
                        templates.remove(tempIndex);
                    } else {
                        // update template with db version
                        templates.set(tempIndex, globalTemplate);
                    }
                } else {
                    // if db does not contain the template remove it from list.
                    templates.remove(tempIndex);
                }
            }
            menus.add(menu);
        });

        Collections.sort(menus, (Menu a1, Menu a2) -> {
            return new NullSafeComparator<>(String::compareTo,
                    true).compare(a1.getIndex(), a2.getIndex());
        });

        response.setMenus(menus);
        return response;
    }

    /**
     * To do add delete method for getGlobalFields.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody
    MenuResponse delete(@PathVariable String id) {
        MenuResponse response = new MenuResponse();
        Menu meun = menuRep.findOne(id);
        try {
            menuRep.delete(meun);
            response.setSuccess(true);
            response.setMessage("Menu [" + meun.getName() + "] removed from active menu list");
        } catch (IllegalArgumentException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    /**
     * Gets all the modules allowed for menu item
     *
     * @return
     */
    @RequestMapping(value = "/module/types", method = RequestMethod.POST)
    public @ResponseBody
    MenuResponse getMenuTypes() {
        MenuResponse response = new MenuResponse();
        GlobalMenuType[] moduleTypes = GlobalMenuType.values();
        List<MenuType> menuTypes = new ArrayList<>();
        for (GlobalMenuType type : moduleTypes) {
            menuTypes.add(new MenuType(type.name(), type.getName(), type.getTemplate()));
        }
        response.setMenuTypes(menuTypes);
        return response;
    }

}
