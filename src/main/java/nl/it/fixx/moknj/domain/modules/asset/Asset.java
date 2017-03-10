package nl.it.fixx.moknj.domain.modules.asset;

import java.util.List;
import java.util.Objects;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.record.Record;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

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

    @Transient
    private String freeDate;
    @Transient
    private String freeValue;
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

    /**
     * @return the createdDate
     */
    @Override
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    @Override
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the createdBy
     */
    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Asset{" + "id=" + id + ", typeId=" + typeId + ", details=" + details + ", resourceId=" + resourceId + ", lastModifiedDate=" + lastModifiedDate + ", lastModifiedBy=" + lastModifiedBy + ", createdDate=" + createdDate + ", createdBy=" + createdBy + ", hidden=" + hidden + ", menuScopeIds=" + menuScopeIds + '}';
    }

    @Override
    public String getFreeDate() {
        return this.freeDate;
    }

    @Override
    public void setFreeDate(String freeDate) {
        this.freeDate = freeDate;
    }

    @Override
    public String getFreeValue() {
        return this.freeValue;
    }

    @Override
    public void setFreeValue(String freeValue) {
        this.freeValue = freeValue;
    }

}
