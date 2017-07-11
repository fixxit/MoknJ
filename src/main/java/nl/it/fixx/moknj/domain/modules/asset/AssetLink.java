package nl.it.fixx.moknj.domain.modules.asset;

import java.util.Objects;
import nl.it.fixx.moknj.domain.core.link.Link;
import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class AssetLink implements Link {

    @Id
    private String id;
    private boolean checked;
    private String date;
    private String resourceId;
    private String assetId;
    private String createdBy;
    private String createdDate;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
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
     * @return the assetId
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * @param assetId the assetId to set
     */
    public void setAssetId(String assetId) {
        this.assetId = assetId;
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

    @Override
    public String toString() {
        return "AssetLink{" + "id=" + id
                + ", checked=" + checked
                + ", date=" + date
                + ", resourceId=" + resourceId
                + ", assetId=" + assetId
                + ", createdBy=" + createdBy
                + ", createdDate=" + createdDate + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + (this.checked ? 1 : 0);
        hash = 71 * hash + Objects.hashCode(this.date);
        hash = 71 * hash + Objects.hashCode(this.resourceId);
        hash = 71 * hash + Objects.hashCode(this.assetId);
        hash = 71 * hash + Objects.hashCode(this.createdBy);
        hash = 71 * hash + Objects.hashCode(this.createdDate);
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
        final AssetLink other = (AssetLink) obj;
        if (this.checked != other.checked) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.resourceId, other.resourceId)) {
            return false;
        }
        if (!Objects.equals(this.assetId, other.assetId)) {
            return false;
        }
        if (!Objects.equals(this.createdBy, other.createdBy)) {
            return false;
        }
        if (!Objects.equals(this.createdDate, other.createdDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String getLinkId() {
        return getId();
    }

    @Override
    public String getRecordId() {
        return getAssetId();
    }

}
