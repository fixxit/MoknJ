/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.domain.core.field;

import nl.it.fixx.moknj.domain.core.global.GlobalFieldType;
import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class FieldDetail {

    @Id
    private String id;
    private GlobalFieldType type;
    private String name;
    private boolean unique;
    private boolean mandatory;
    private boolean display;

    @Override
    public String toString() {
        return "FieldDetail{" + "id=" + getId() + ", type=" + getType() + ", name=" + getName() + ", unique=" + isUnique() + ", mandatory=" + isMandatory() + ", display=" + isDisplay() + '}';
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the type
     */
    public GlobalFieldType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(GlobalFieldType type) {
        this.type = type;
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
     * @return the unique
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * @param unique the unique to set
     */
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    /**
     * @return the mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * @param mandatory the mandatory to set
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * @return the display
     */
    public boolean isDisplay() {
        return display;
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(boolean display) {
        this.display = display;
    }

}
