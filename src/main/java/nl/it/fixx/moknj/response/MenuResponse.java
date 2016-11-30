/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.response;

import java.util.List;
import nl.it.fixx.moknj.domain.core.menu.Menu;

/**
 *
 * @author adriaan
 */
public class MenuResponse extends Response {

    private List<Menu> menus;
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

    @Override
    public String toString() {
        return "MenuResponse{" + "menus=" + menus + ", menu=" + menu + '}';
    }

}
