package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.employee.Employee;

/**
 *
 * @author adriaan
 */
public interface EmployeeRepositoryCustom {

    public List<Employee> getAllByTypeId(String id);

    public List<Employee> findAll();
}
