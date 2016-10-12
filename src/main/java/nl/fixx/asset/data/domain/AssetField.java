/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.domain;

import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class AssetField {

    @Id
    private long id;
    private long fieldDetailId;
    private String value;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the fieldDetailId
     */
    public long getFieldDetailId() {
        return fieldDetailId;
    }

    /**
     * @param fieldDetailId the fieldDetailId to set
     */
    public void setFieldDetailId(long fieldDetailId) {
        this.fieldDetailId = fieldDetailId;
    }

    @Override
    public String toString() {
        return "AssetField{" + "id=" + id + ", fieldDetailId=" + fieldDetailId + ", value=" + value + '}';
    }
}
