/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.domain.core.template;

import nl.it.fixx.moknj.domain.core.global.GlobalTemplateType;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import java.util.List;
import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class Template {

    @Id
    protected String id;
    protected String name;
    protected GlobalTemplateType templateType;
    // Fields names (etc <Sizeinteger>) and field type
    protected List<FieldDetail> details;
    protected boolean hidden;
    protected String index;

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
     * @param name the type to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the details
     */
    public List<FieldDetail> getDetails() {
        return (List<FieldDetail>) details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(List<FieldDetail> details) {
        this.details = details;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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
     * @return the templateType
     */
    public GlobalTemplateType getTemplateType() {
        return templateType;
    }

    /**
     * @param templateType the templateType to set
     */
    public void setTemplateType(GlobalTemplateType templateType) {
        this.templateType = templateType;
    }

    @Override
    public String toString() {
        return "Template{" + "id=" + id + ", name=" + name + ", templateType=" + templateType + ", details=" + details + ", hidden=" + hidden + ", index=" + index + '}';
    }

}
