package nl.it.fixx.moknj.response;

import java.util.List;
import nl.it.fixx.moknj.domain.core.field.FieldType;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.template.TemplateType;

/**
 *
 * @author adriaan
 */
public class TemplateResponse extends Response {

    private Template type;
    private List<FieldType> fieldTypes;
    private List<Template> types;
    private List<TemplateType> templateTypes;

    /**
     * @return the fieldTypes
     */
    public List<FieldType> getFieldTypes() {
        return fieldTypes;
    }

    /**
     * @param fieldTypes the fieldTypes to set
     */
    public void setFieldTypes(List<FieldType> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    /**
     * @return the type
     */
    public Template getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Template type) {
        this.type = type;
    }

    /**
     * @return the types
     */
    public List<Template> getTypes() {
        return types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(List<Template> types) {
        this.types = types;
    }

    /**
     * @return the templateTypes
     */
    public List<TemplateType> getTemplateTypes() {
        return templateTypes;
    }

    /**
     * @param templateTypes the templateTypes to set
     */
    public void setTemplateTypes(List<TemplateType> templateTypes) {
        this.templateTypes = templateTypes;
    }

    @Override
    public String toString() {
        return "TemplateResponse{" + "type=" + type + ", fieldTypes=" + fieldTypes + ", types=" + types + ", templateTypes=" + templateTypes + '}';
    }

}
