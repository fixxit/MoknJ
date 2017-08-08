package nl.it.fixx.moknj.repository.wrapper.impl;

import nl.it.fixx.moknj.bal.module.recordaction.Intercept;
import nl.it.fixx.moknj.bal.module.recordaction.delete.Delete;
import nl.it.fixx.moknj.bal.module.recordaction.save.Save;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import nl.it.fixx.moknj.repository.wrapper.BaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeWrapper extends BaseWrapper<Employee, EmployeeRepository> {

    @Autowired
    public EmployeeWrapper(EmployeeRepository repository) {
        super(repository);
    }

    @Override
    @Save(intercepts = {Intercept.AFTER, Intercept.BEFORE})
    public Employee save(Employee s) {
        return super.save(s);
    }

    @Override
    @Delete(intercepts = {Intercept.BEFORE})
    public void delete(Employee t) {
        super.delete(t);
    }

}
