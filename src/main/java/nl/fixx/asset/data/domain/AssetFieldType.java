/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.domain;

/**
 * This class is used to define the field data types. Adding of new data types
 * can be done with no issue to db but changing the name of the enum class will
 * break the field link. You can change the string value type as "text" to
 * "string" with no issues.
 *
 * @author adriaan
 */
public enum AssetFieldType {
    ASSET_INPUT_INT_TYPE("Number"),
    ASSET_INPUT_STR_TYPE("Text"),
    ASSET_INPUT_DAT_TYPE("Date"),
    ASSET_INPUT_MON_TYPE("Monetary"),
    ASSET_INPUT_DRD_TYPE("Dropdown");

    private final String type;

    private AssetFieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
