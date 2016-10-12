/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.domain;

import java.util.ArrayList;
import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class Asset {

    @Id
    private String id;
    private long typeId;
    private ArrayList<AssetField> values;

    @Override
    public String toString() {
        return "Asset{" + "id=" + id + ", typeId=" + typeId + ", values=" + values + '}';
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the typeId
     */
    public long getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the values
     */
    public ArrayList<AssetField> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(ArrayList<AssetField> values) {
        this.values = values;
    }

}
