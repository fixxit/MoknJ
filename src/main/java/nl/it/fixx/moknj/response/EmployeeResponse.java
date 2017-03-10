package nl.it.fixx.moknj.response;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.employee.Employee;

/**
 *
 * @author adriaan
 */
public class EmployeeResponse extends Response {

    private Employee employee;
    private List<Employee> employees;

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "EmployeeResponse{" + "employee=" + employee + '}';
    }

    /**
     * @return the employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
