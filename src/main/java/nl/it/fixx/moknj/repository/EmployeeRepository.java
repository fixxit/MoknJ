package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.bal.module.recordaction.Intercept;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.repository.custom.EmployeeRepositoryCustom;
import org.springframework.stereotype.Repository;
import nl.it.fixx.moknj.bal.module.recordaction.save.Save;

@Repository
public interface EmployeeRepository extends RecordRepository<Employee>, EmployeeRepositoryCustom {

    @Override
    @Save(intercepts = {Intercept.AFTER, Intercept.BEFORE})
    public <S extends Employee> S insert(S s);

}
