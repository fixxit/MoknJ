package nl.it.fixx.moknj.domain.core.record;

import java.util.List;
import nl.it.fixx.moknj.domain.core.field.FieldValue;

/**
 *
 * @author adriaan
 */
public interface Record {

    /**
     * @return the id
     */
    String getId();

    /**
     * @return the typeId
     */
    String getTypeId();

    /**
     * @param typeId the typeId to set
     */
    void setTypeId(String typeId);

    /**
     * @return the details
     */
    List<FieldValue> getDetails();

    /**
     * @param details the details to set
     */
    void setDetails(List<FieldValue> details);

    /**
     * @return the lastModifiedDate
     */
    String getLastModifiedDate();

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    void setLastModifiedDate(String lastModifiedDate);

    /**
     * @return the lastModifiedBy
     */
    String getLastModifiedBy();

    /**
     * @param lastModifiedBy the lastModifiedBy to set
     */
    void setLastModifiedBy(String lastModifiedBy);

    /**
     * @return the hidden
     */
    boolean isHidden();

    /**
     * @param hidden the hidden to set
     */
    void setHidden(boolean hidden);

    /**
     * @return the menuScopeIds
     */
    List<String> getMenuScopeIds();

    /**
     * @param menuScopeIds the menuScopeIds to set
     */
    void setMenuScopeIds(List<String> menuScopeIds);

    /**
     * @return the createdDate
     */
    String getCreatedDate();

    /**
     * @param createdDate the createdDate to set
     */
    void setCreatedDate(String createdDate);

    /**
     * @return the createdBy
     */
    String getCreatedBy();

    /**
     * @param createdBy the createdBy to set
     */
    void setCreatedBy(String createdBy);

    /**
     * @return the resourceId
     */
    String getResourceId();

    /**
     * @param resourceId the resourceId to set
     */
    void setResourceId(String resourceId);

    /**
     * @return the freeDate
     */
    String getFreeDate();

    /**
     * @param freedate the freeDate to set
     */
    void setFreeDate(String freedate);

    /**
     * @return the freeValue
     */
    String getFreeValue();

    /**
     * @param freeValue the freeValue to set
     */
    void setFreeValue(String freeValue);

    //Transiant Data
    String getMenuId();

    void setMenuId(String menuId);

    String getToken();

    void setToken(String token);

    boolean isCascade();

    void setCascade(boolean cascade);
}
