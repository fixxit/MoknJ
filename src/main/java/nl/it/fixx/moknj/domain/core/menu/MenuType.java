package nl.it.fixx.moknj.domain.core.menu;

/**
 *
 * @author adriaan
 */
public class MenuType {

    private String name;
    private String type;
    private String template;

    public MenuType(String name, String type, String template) {
        this.name = name;
        this.type = type;
        this.template = template;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }
}
