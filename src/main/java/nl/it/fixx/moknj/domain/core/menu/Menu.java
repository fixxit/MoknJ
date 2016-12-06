package nl.it.fixx.moknj.domain.core.menu;

import java.util.List;
import nl.it.fixx.moknj.domain.core.global.GlobalMenuType;
import nl.it.fixx.moknj.domain.core.template.Template;

/**
 *
 * @author adriaan
 */
public class Menu {

    private String id;
    private String name;
    private String pageName;
    private String index;
    private List<Template> templates;
    private GlobalMenuType menuType;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the templates
     */
    public List<Template> getTemplates() {
        return templates;
    }

    /**
     * @param templates the templates to set
     */
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    /**
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * @return the pageName
     */
    public String getPageName() {
        return pageName;
    }

    /**
     * @param pageName the pageName to set
     */
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    /**
     * @return the menuType
     */
    public GlobalMenuType getMenuType() {
        return menuType;
    }

    /**
     * @param menuType the menuType to set
     */
    public void setMenuType(GlobalMenuType menuType) {
        this.menuType = menuType;
    }

    @Override
    public String toString() {
        return "Menu{" + "id=" + id + ", name=" + name + ", pageName=" + pageName + ", index=" + index + ", templates=" + templates + ", menuType=" + menuType + '}';
    }

}
