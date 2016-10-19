/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.domain;

/**
 *
 * @author adriaan
 */
public enum AssetFieldType {
    ASSET_INPUT_INT_TYPE("Integer"),
    ASSET_INPUT_STR_TYPE("String"),
    ASSET_INPUT_DAT_TYEP("Date"),
    ASSET_INPUT_MON_TYPE("Monetary");

    private final String type;

    private AssetFieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
