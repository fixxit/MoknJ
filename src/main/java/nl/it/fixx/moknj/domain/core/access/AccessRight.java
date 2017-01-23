/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.domain.core.access;

/**
 *
 * @author adriaan
 */
public class AccessRight {

    private String enumName;
    private String displayName;

    public AccessRight(String enumName, String displayName) {
        this.enumName = enumName;
        this.displayName = displayName;
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

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
