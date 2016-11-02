/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.domain;

import java.util.List;
import java.util.Objects;
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
    private String resourceId;

    @Override
    public String toString() {
        return "Asset{" + "id=" + id + ", typeId=" + typeId + ", details=" + details + ", resourceId=" + resourceId + '}';
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.typeId);
        hash = 67 * hash + Objects.hashCode(this.details);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Asset other = (Asset) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.typeId, other.typeId)) {
            return false;
        }
        if (!Objects.equals(this.details, other.details)) {
            return false;
        }
        return true;
    }

    /**
     * @return the resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * @param resourceId the resourceId to set
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

}
