package nl.it.fixx.moknj.response;

import java.util.List;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.menu.MenuType;

/**
 *
 * @author adriaan
 */
public class MenuResponse extends Response {

    private List<Menu> menus;
    private List<MenuType> menuTypes;
    private Menu menu;

    /**
     * @return the menus
     */
    public List<Menu> getMenus() {
        return menus;
    }

    /**
     * @param menus the menus to set
     */
    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    /**
     * @return the menu
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    /**
     * @return the menuTypes
     */
    public List<MenuType> getMenuTypes() {
        return menuTypes;
    }

    /**
     * @param menuTypes the menuTypes to set
     */
    public void setMenuTypes(List<MenuType> menuTypes) {
        this.menuTypes = menuTypes;
    }

    @Override
    public String toString() {
        return "MenuResponse{" + "menus=" + menus + ", menuTypes=" + menuTypes + ", menu=" + menu + '}';
    }

}
