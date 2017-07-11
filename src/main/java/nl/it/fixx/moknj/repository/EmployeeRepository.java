package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.bal.module.recordaction.Action;
import nl.it.fixx.moknj.bal.module.recordaction.save.SaveRecord;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.repository.custom.EmployeeRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends RecordRepository<Employee>, EmployeeRepositoryCustom {

    @Override
    @SaveRecord(position = {Action.AFTER, Action.BEFORE})
    public <S extends Employee> S insert(S s);

}
