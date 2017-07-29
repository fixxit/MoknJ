package nl.it.fixx.moknj.bal.module.recordaction.delete;

import nl.it.fixx.moknj.bal.module.employee.EmployeeLinkBal;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDeleteAction extends DeleteActionBase<Void, Employee, EmployeeRepository> {

    private final EmployeeLinkBal employeeLinkBal;

    @Autowired
    public EmployeeDeleteAction(EmployeeRepository repository, EmployeeLinkBal employeeLinkBal) {
        super(repository);
        this.employeeLinkBal = employeeLinkBal;
    }

    @Override
    public boolean valid(Object domain) {
        return (domain instanceof Employee);
    }

    @Override
    public Void before(Employee domain) {
        Employee result = repository.findOne(domain.getId());
        if (result != null && domain.isCascade()) {
            employeeLinkBal.getAllLinksByRecordId(domain.getId(), domain.getToken()).stream().forEach((link) -> {
                employeeLinkBal.delete(link);
            });
        }
        return null;
    }

}
