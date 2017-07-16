package nl.it.fixx.moknj.bal.module.recordaction.delete;

import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDeleteAction extends DeleteActionBase<EmployeeLink, Employee, EmployeeRepository> {

    @Autowired
    public EmployeeDeleteAction(EmployeeRepository repository) {
        super(repository);
    }

    @Override
    public boolean valid(Object domain) {
        return (domain instanceof Employee);
    }

}
