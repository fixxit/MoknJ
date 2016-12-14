package nl.it.fixx.moknj.domain.modules.employee;

/**
 * This class is used define what actions is available to EMPLOYEE LINK (Audit
 * table). Keep in mind these actions could possibly used when a worker picks up
 * an action and decides what action to take on it.
 *
 * @author adriaan
 */
public enum EmployeeAction {
    EMP_ACTION_EDIT("Updated Employee"),
    EMP_ACTION_NEW("Created Employee"),
    EMP_ACTION_OPEN("Viewed Employee"),
    EMP_ACTION_DELETE("Deleted Employee"),
    EMP_ACTION_HIDE("Employee Hidden");

    final String displayValue;

    EmployeeAction(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
