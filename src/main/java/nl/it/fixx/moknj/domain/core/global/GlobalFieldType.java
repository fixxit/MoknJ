/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.domain.core.global;

/**
 * This class is used to define the field data types. Adding of new data types
 * can be done with no issue to db but changing the name of the enum class will
 * break the field link. You can change the string value type as "text" to
 * "string" with no issues.
 *
 * @author adriaan
 */
public enum GlobalFieldType {
    GBL_INPUT_INT_TYPE("Number"),
    GBL_INPUT_TXT_TYPE("Text"),
    //GBL_INPUT_LTX_TYPE("Large Text"),
    GBL_INPUT_DAT_TYPE("Date"),
    GBL_INPUT_MON_TYPE("Monetary"),
    GBL_INPUT_DRP_TYPE("Dropdown");

    private final String name;

    private GlobalFieldType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
