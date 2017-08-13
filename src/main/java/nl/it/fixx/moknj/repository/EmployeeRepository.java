package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.repository.custom.EmployeeRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository("employeeRepository")
public interface EmployeeRepository extends RecordRepository<Employee>, EmployeeRepositoryCustom {

}
