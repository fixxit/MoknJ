package nl.it.fixx.moknj.controller;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.bal.core.MainAccessBal;
import nl.it.fixx.moknj.domain.core.global.GlobalMenuType;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.menu.MenuType;
import nl.it.fixx.moknj.response.MenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/menu")
public class MenuContoller {

    @Autowired
    private MainAccessBal mainAccessBal;

    /**
     *
     * @param payload
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    MenuResponse add(@RequestBody Menu payload, @RequestParam String access_token) {
        MenuResponse response = new MenuResponse();
        try {
            Menu menu = mainAccessBal.saveMenu(payload, access_token);
            response.setSuccess(menu != null);
            response.setMessage("Saved " + menu.getName());
            response.setMenu(menu);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    /**
     * Gets menu by id.
     *
     * @param id
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public @ResponseBody
    MenuResponse get(@PathVariable String id, @RequestParam String access_token) {
        MenuResponse response = new MenuResponse();
        try {
            Menu menu = mainAccessBal.getMenu(id, access_token);
            response.setMenu(menu);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    /**
     * Used to getMenu all mapped menu's from db.
     *
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public @ResponseBody
    MenuResponse all(@RequestParam String access_token) {
        MenuResponse response = new MenuResponse();
        try {
            response.setMenus(mainAccessBal.getUserMenus(access_token));
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    /**
     * To do add delete method for getGlobalFields.
     *
     * @param id
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody
    MenuResponse delete(@PathVariable String id, @RequestParam String access_token) {
        MenuResponse response = new MenuResponse();
        try {
            mainAccessBal.deleteMenu(id, access_token);
            response.setSuccess(true);
            response.setMessage("Menu deleted from active menu list");
        } catch (Exception ex) {
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
