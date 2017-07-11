package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.employee.Employee;

/**
 *
 * @author adriaan
 */
public interface EmployeeRepositoryCustom {

    List<Employee> getAllByTypeId(String id);

    List<Employee> findAll();
}
