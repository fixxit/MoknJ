package nl.it.fixx.moknj.bal.module.validator.field;

import nl.it.fixx.moknj.bal.core.FieldBal;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeFieldModuleValidation extends FieldModuleBase<Employee, EmployeeRepository> {

    @Autowired
    public EmployeeFieldModuleValidation(EmployeeRepository repository, FieldBal fieldBal) {
        super(repository, fieldBal, AssetFieldModuleValidation.class);
    }

    @Override
    public Module getModule() {
        return Module.EMPLOYEE;
    }

}
