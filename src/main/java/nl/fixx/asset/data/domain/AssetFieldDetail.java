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
public class AssetFieldDetail {

    @Id
    private String id;
    private AssetFieldType type;
    private String name;
    private boolean unique;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the type
     */
    public AssetFieldType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AssetFieldType type) {
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

    @Override
    public String toString() {
        return "AssetFieldDetail{" + "id=" + id + ", type=" + type + ", name=" + name + ", unique=" + unique + '}';
    }

}
