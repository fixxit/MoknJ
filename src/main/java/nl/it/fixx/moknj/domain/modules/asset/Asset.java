/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.domain.modules.asset;

import java.util.List;
import java.util.Objects;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class Asset {

    @Id
    private String id;
    private String typeId;
    private List<FieldValue> details;
    private String resourceId;
    private String lastModifiedDate;
    private String lastModifiedBy;
    private boolean hidden;

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
     * @param typeId the typeId to set
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the details
     */
    public List<FieldValue> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(List<FieldValue> details) {
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

    /**
     * @return the lastModifiedDate
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * @return the lastModifiedBy
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * @param lastModifiedBy the lastModifiedBy to set
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return "Asset{" + "id=" + id
                + ", typeId=" + typeId
                + ", details=" + details
                + ", resourceId=" + resourceId
                + ", lastModifiedDate=" + lastModifiedDate
                + ", lastModifiedBy=" + lastModifiedBy
                + ", hidden=" + hidden + '}';
    }

}
