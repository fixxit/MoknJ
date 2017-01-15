package nl.it.fixx.moknj.domain.core.graph;

import nl.it.fixx.moknj.domain.core.global.GlobalGraphDate;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphFocus;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphType;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphView;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

/**
 * This class is used to store home page graph setup value. These values are
 * used to generate a graph for the home page.
 *
 * @author adriaan
 */
public class Graph {

    /**
     * Data fields.
     */
    @Id
    private String id;
    private String menuId;
    private String templateId;
    private String name;
    private GlobalGraphType graphType;
    private GlobalGraphView graphView;
    private GlobalGraphFocus graphFocus;
    private GlobalGraphDate graphDateType;
    private String graphDate;
    private String freefieldId;
    private String freeDateFieldId;

    /**
     * Display field value on home setup filter view.
     */
    @Transient
    private String menu;
    @Transient
    private String template;
    @Transient
    private String view;
    @Transient
    private String type;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId the templateId to set
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the menuId
     */
    public String getMenuId() {
        return menuId;
    }

    /**
     * @param menuId the menuId to set
     */
    public void setMenuId(String menuId) {
        this.menuId = menuId;
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
     * @return the graphType
     */
    public GlobalGraphType getGraphType() {
        return graphType;
    }

    /**
     * @param graphType the graphType to set
     */
    public void setGraphType(GlobalGraphType graphType) {
        this.graphType = graphType;
    }

    /**
     * @return the graphView
     */
    public GlobalGraphView getGraphView() {
        return graphView;
    }

    /**
     * @param graphView the graphView to set
     */
    public void setGraphView(GlobalGraphView graphView) {
        this.graphView = graphView;
    }

    /**
     * @return the graphDate
     */
    public String getGraphDate() {
        return graphDate;
    }

    /**
     * @param graphDate the graphDate to set
     */
    public void setGraphDate(String graphDate) {
        this.graphDate = graphDate;
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

    /**
     * @return the menu
     */
    public String getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(String menu) {
        this.menu = menu;
    }

    /**
     * @return the view
     */
    public String getView() {
        return view;
    }

    /**
     * @param view the view to set
     */
    public void setView(String view) {
        this.view = view;
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
     * @return the graphFocus
     */
    public GlobalGraphFocus getGraphFocus() {
        return graphFocus;
    }

    /**
     * @param graphFocus the graphFocus to set
     */
    public void setGraphFocus(GlobalGraphFocus graphFocus) {
        this.graphFocus = graphFocus;
    }

    /**
     * @return the freefieldId
     */
    public String getFreefieldId() {
        return freefieldId;
    }

    /**
     * @param freefieldId the freefieldId to set
     */
    public void setFreefieldId(String freefieldId) {
        this.freefieldId = freefieldId;
    }

    /**
     * @return the freeDateFieldId
     */
    public String getFreeDateFieldId() {
        return freeDateFieldId;
    }

    /**
     * @param freeDateFieldId the freeDateFieldId to set
     */
    public void setFreeDateFieldId(String freeDateFieldId) {
        this.freeDateFieldId = freeDateFieldId;
    }

    /**
     * @return the graphDateType
     */
    public GlobalGraphDate getGraphDateType() {
        return graphDateType;
    }

    /**
     * @param graphDateType the graphDateType to set
     */
    public void setGraphDateType(GlobalGraphDate graphDateType) {
        this.graphDateType = graphDateType;
    }

}
