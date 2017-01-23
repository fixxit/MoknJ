package nl.it.fixx.moknj.domain.core.access;

import java.util.Objects;
import java.util.Set;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import org.springframework.data.annotation.Id;

/**
 * This class is used to save user access to menu-template.
 *
 * @author adriaan
 */
public class Access {

    @Id
    private String id;
    private String menuId;
    private String templateId;
    private String userId;

    private Set<GlobalAccessRights> rights;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the menuId
     */
    public String getMenuId() {
        return menuId;
    }

    /**
     * @param menuId the menuId to set
     */
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    /**
     * @return the templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId the templateId to set
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the rights
     */
    public Set<GlobalAccessRights> getRights() {
        return rights;
    }

    /**
     * @param rights the rights to set
     */
    public void setRights(Set<GlobalAccessRights> rights) {
        this.rights = rights;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.menuId);
        hash = 29 * hash + Objects.hashCode(this.templateId);
        hash = 29 * hash + Objects.hashCode(this.userId);
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
        final Access other = (Access) obj;
        if (!Objects.equals(this.menuId, other.menuId)) {
            return false;
        }
        if (!Objects.equals(this.templateId, other.templateId)) {
            return false;
        }
        return Objects.equals(this.userId, other.userId);
    }

}
