package nl.it.fixx.moknj.domain.modules.employee;

/**
 * This class is used define what actions is available to EMPLOYEE LINK (Audit
 * table). Keep in mind these actions could possibly used when a worker picks up
 * an action and decides what action to take on it.
 *
 * @author adriaan
 */
public enum EmployeeAction {
    EMP_ACTION_SAVE,
    EMP_ACTION_EDIT,
    EMP_ACTION_NEW,
    EMP_ACTION_OPEN,
    EMP_ACTION_DELETE,
    EMP_ACTION_HIDE;
}
