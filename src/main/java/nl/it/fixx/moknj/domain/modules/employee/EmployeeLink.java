package nl.it.fixx.moknj.domain.modules.employee;

import java.util.Objects;
import nl.it.fixx.moknj.domain.core.link.Link;
import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class EmployeeLink implements Link {

    @Id
    private String id;
    private EmployeeAction action;
    private String actionValue;
    private String changes;
    private String template;
    private String user;
    private String employeeId;
    private String createdBy;
    private String createdDate;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
     * @return the action
     */
    public EmployeeAction getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(EmployeeAction action) {
        this.action = action;
    }

    /**
     * @return the changes
     */
    public String getChanges() {
        return changes;
    }

    /**
     * @param changes the changes to set
     */
    public void setChanges(String changes) {
        this.changes = changes;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the actionValue
     */
    public String getActionValue() {
        return actionValue;
    }

    /**
     * @param actionValue the actionValue to set
     */
    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }

    @Override
    public String toString() {
        return "EmployeeLink{" + "id=" + id
                + ", action=" + action
                + ", actionValue=" + actionValue
                + ", changes=" + changes
                + ", template=" + template
                + ", user=" + user
                + ", employeeId=" + employeeId
                + ", createdBy=" + createdBy
                + ", createdDate=" + createdDate
                + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.action);
        hash = 37 * hash + Objects.hashCode(this.actionValue);
        hash = 37 * hash + Objects.hashCode(this.changes);
        hash = 37 * hash + Objects.hashCode(this.template);
        hash = 37 * hash + Objects.hashCode(this.user);
        hash = 37 * hash + Objects.hashCode(this.employeeId);
        hash = 37 * hash + Objects.hashCode(this.createdBy);
        hash = 37 * hash + Objects.hashCode(this.createdDate);
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
        final EmployeeLink other = (EmployeeLink) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.actionValue, other.actionValue)) {
            return false;
        }
        if (!Objects.equals(this.changes, other.changes)) {
            return false;
        }
        if (!Objects.equals(this.template, other.template)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.employeeId, other.employeeId)) {
            return false;
        }
        if (!Objects.equals(this.createdBy, other.createdBy)) {
            return false;
        }
        if (!Objects.equals(this.createdDate, other.createdDate)) {
            return false;
        }
        return this.action == other.action;
    }

    @Override
    public String getLinkId() {
        return getId();
    }

    @Override
    public String getRecordId() {
        return getEmployeeId();
    }

}
