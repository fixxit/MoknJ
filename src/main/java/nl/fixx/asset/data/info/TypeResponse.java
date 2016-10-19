/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.info;

import java.util.List;
import nl.fixx.asset.data.domain.AssetType;
import nl.fixx.asset.data.domain.FieldType;

/**
 *
 * @author adriaan
 */
public class TypeResponse extends Response {

    private AssetType type;
    private List<FieldType> fieldTypes;

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
    public AssetType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AssetType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TypeResponse{" + "type=" + getType() + '}';
    }

}
