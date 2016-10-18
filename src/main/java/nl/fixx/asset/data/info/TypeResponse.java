/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.info;

import java.util.ArrayList;
import nl.fixx.asset.data.domain.AssetType;

/**
 *
 * @author adriaan
 */
public class TypeResponse extends Response {

    private AssetType type;
    private ArrayList<String> fieldTypes;

    /**
     * @return the type
     */
    public AssetType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AssetType type) {
        this.type = type;
    }

    /**
     * @return the fieldTypes
     */
    public ArrayList<String> getFieldTypes() {
        return fieldTypes;
    }

    /**
     * @param fieldTypes the fieldTypes to set
     */
    public void setFieldTypes(ArrayList<String> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    @Override
    public String toString() {
        return "TypeResponse{" + "type=" + type + '}';
    }

}
