/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.bal;

import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adriaan
 */
public class MenuBal implements BusinessAccessLayer {

    private static final Logger LOG = LoggerFactory.getLogger(MenuBal.class);
    private final MenuRepository menuRep;

    public MenuBal(MenuRepository menuRep) {
        this.menuRep = menuRep;
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
                LOG.info("No menu id recieved to find menu");
                throw new Exception("No menu found, no menu id provided!");
            }

            Menu menu = menuRep.findOne(id);
            if (menu == null) {
                LOG.info("no menu found for id[" + id + "]");
                throw new Exception("No menu found by this id[" + id + "]");
            }
            return menu;
        } catch (Exception e) {
            LOG.info("Error on Menu Bal", e);
            throw new Exception("Exception trying to retrieve menu", e);
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
}
