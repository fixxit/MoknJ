package nl.it.fixx.moknj.bal.core.menu;

import java.util.List;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;

public interface MenuCoreBal {

    Menu saveMenu(Menu payload);

    Menu getMenuById(String id);

    List<Menu> getAllMenus();

    List<Menu> getMenusForTemplateId(String templateId);

    String getDispayName(Menu menu);

    String getDispayName(String menuId) throws Exception;

    void deleteMenu(String id);

    Template getMenuTemplate(String menuId, String templateId) throws Exception;
}
