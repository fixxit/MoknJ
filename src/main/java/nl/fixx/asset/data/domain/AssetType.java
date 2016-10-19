/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.domain;

import java.util.List;
import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class AssetType {

    @Id
    private String id;
    private String name;
    // Fields names (etc <Sizeinteger>) and field type
    private List<AssetFieldDetail> details;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the type to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the details
     */
    public List<AssetFieldDetail> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(List<AssetFieldDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "AssetType{" + "id=" + id + ", name=" + name + ", details=" + details + '}';
    }


}
