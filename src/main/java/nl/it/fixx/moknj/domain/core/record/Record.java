/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public String getId();

    /**
     * @return the typeId
     */
    public String getTypeId();

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(String typeId);

    /**
     * @return the details
     */
    public List<FieldValue> getDetails();

    /**
     * @param details the details to set
     */
    public void setDetails(List<FieldValue> details);

    /**
     * @return the lastModifiedDate
     */
    public String getLastModifiedDate();

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(String lastModifiedDate);

    /**
     * @return the lastModifiedBy
     */
    public String getLastModifiedBy();

    /**
     * @param lastModifiedBy the lastModifiedBy to set
     */
    public void setLastModifiedBy(String lastModifiedBy);

    /**
     * @return the hidden
     */
    public boolean isHidden();

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden);

    /**
     * @return the menuScopeIds
     */
    public List<String> getMenuScopeIds();

    /**
     * @param menuScopeIds the menuScopeIds to set
     */
    public void setMenuScopeIds(List<String> menuScopeIds);

    /**
     * @return the createdDate
     */
    public String getCreatedDate();

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(String createdDate);

    /**
     * @return the createdBy
     */
    public String getCreatedBy();

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy);
}
