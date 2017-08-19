package nl.it.fixx.moknj.bal.module.recordaction.delete.impl;

import nl.it.fixx.moknj.bal.module.link.ModuleLinkBal;
import nl.it.fixx.moknj.bal.module.recordaction.delete.DeleteActionBase;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDeleteAction extends DeleteActionBase<Void, Record, RecordRepository<Employee>> {

    private final ModuleLinkBal<EmployeeLink> employeeLinkBal;

    @Autowired
    public EmployeeDeleteAction(
            @Qualifier("employeeRepository") RecordRepository<Employee> repository,
            @Qualifier("employeeLinkBal") ModuleLinkBal<EmployeeLink> employeeLinkBal) {
        super(repository);
        this.employeeLinkBal = employeeLinkBal;
    }

    @Override
    public boolean valid(Object domain) {
        return (domain instanceof Employee);
    }

    @Override
    public Void before(Record domain) {
        Employee result = repository.findOne(domain.getId());
        if (result != null && domain.isCascade()) {
            // delete links
            employeeLinkBal.getAllLinksByRecordId(domain.getId(), 
                    domain.getToken()).stream().forEach((link) -> {
                employeeLinkBal.delete(link);
            });
        }
        return null;
    }

}
