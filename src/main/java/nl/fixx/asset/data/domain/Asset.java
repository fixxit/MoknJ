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
public class Asset {

    @Id
    private String id;
    private String typeId;
    private List<AssetField> details;

    @Override
    public String toString() {
	return "Asset{" + "id=" + id + ", typeId=" + typeId + ", values=" + details + '}';
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
    public String getTypeId() {
	return typeId;
    }

    /**
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(String typeId) {
	this.typeId = typeId;
    }

    /**
     * @return the details
     */
    public List<AssetField> getDetails() {
	return details;
    }

    /**
     * @param details
     *            the details to set
     */
    public void setDetails(List<AssetField> details) {
	this.details = details;
    }

}
