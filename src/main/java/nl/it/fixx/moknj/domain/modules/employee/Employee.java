package nl.it.fixx.moknj.domain.modules.employee;

import java.util.List;
import java.util.Objects;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class Employee {

    @Id
    private String id;
    private String typeId;
    private List<FieldValue> details;
    private String resourceId;
    private String lastModifiedDate;
    private String lastModifiedBy;
    private boolean hidden;
    /**
     * Scope id's define the menu's (cards) which this asset is visible on.
     */
    private List<String> menuScopeIds;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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
        final Employee other = (Employee) obj;
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
     * @return the menuScopeIds
     */
    public List<String> getMenuScopeIds() {
        return menuScopeIds;
    }

    /**
     * @param menuScopeIds the menuScopeIds to set
     */
    public void setMenuScopeIds(List<String> menuScopeIds) {
        this.menuScopeIds = menuScopeIds;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", typeId=" + typeId
                + ", details=" + details + ", resourceId=" + resourceId
                + ", lastModifiedDate=" + lastModifiedDate
                + ", lastModifiedBy=" + lastModifiedBy
                + ", hidden=" + hidden + ", menuScopeIds=" + menuScopeIds + '}';
    }

}
