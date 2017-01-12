package nl.it.fixx.moknj.domain.modules.employee;

/**
 * This class is used define what actions is available to EMPLOYEE LINK (Audit
 * table). Keep in mind these actions could possibly used when a worker picks up
 * an action and decides what action to take on it.
 *
 * @author adriaan
 */
public enum EmployeeAction {
    EMP_ACTION_EDIT("Update"),
    EMP_ACTION_NEW("Create"),
    EMP_ACTION_OPEN("View"),
    EMP_ACTION_DELETE("Delete"),
    EMP_ACTION_HIDE("Hide");

    final String displayValue;

    EmployeeAction(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
