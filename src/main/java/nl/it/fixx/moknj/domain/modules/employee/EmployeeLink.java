package nl.it.fixx.moknj.domain.modules.employee;

import org.springframework.data.annotation.Id;

/**
 *
 * @author adriaan
 */
public class EmployeeLink {

    @Id
    private String id;
    private EmployeeAction action;
    private String date;
    private String resourceId;
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
}
