/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.domain.core.graph;

/**
 *
 * @author adriaan
 */
public class GraphType {

    private String name;
    private String property;
    private String enumName;

    public GraphType(String name, String property, String enumName) {
        this.name = name;
        this.property = property;
        this.enumName = enumName;
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
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * @return the enumName
     */
    public String getEnumName() {
        return enumName;
    }

    /**
     * @param enumName the enumName to set
     */
    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }
}
