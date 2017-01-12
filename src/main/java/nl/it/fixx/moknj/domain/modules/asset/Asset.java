/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.domain.modules.asset;

import java.util.List;
import java.util.Objects;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.record.Record;
import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class Asset implements Record {

    @Id
    private String id;
    private String typeId;
    private List<FieldValue> details;
    private String resourceId;
    private String lastModifiedDate;
    private String lastModifiedBy;
    private String createdDate;
    private String createdBy;
    private boolean hidden;
    /**
     * Scope id's define the menu's (cards) which this asset is visible on.
     */
    private List<String> menuScopeIds;

    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @return the typeId
     */
    @Override
    public String getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    @Override
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the details
     */
    @Override
    public List<FieldValue> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    @Override
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

        return Objects.equals(this.details, other.details);
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
    @Override
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    @Override
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * @return the lastModifiedBy
     */
    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * @param lastModifiedBy the lastModifiedBy to set
     */
    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * @return the hidden
     */
    @Override
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * @return the menuScopeIds
     */
    @Override
    public List<String> getMenuScopeIds() {
        return menuScopeIds;
    }

    /**
     * @param menuScopeIds the menuScopeIds to set
     */
    @Override
    public void setMenuScopeIds(List<String> menuScopeIds) {
        this.menuScopeIds = menuScopeIds;
    }

    @Override
    public String toString() {
        return "Asset{" + "id=" + id + ", typeId=" + typeId
                + ", details=" + details + ", resourceId=" + resourceId
                + ", lastModifiedDate=" + lastModifiedDate
                + ", lastModifiedBy=" + lastModifiedBy + ", hidden=" + hidden
                + ", scopeIds=" + menuScopeIds + '}';
    }

    /**
     * @return the createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
