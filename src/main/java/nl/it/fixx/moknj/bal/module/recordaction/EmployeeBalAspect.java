package nl.it.fixx.moknj.bal.module.recordaction;

import nl.it.fixx.moknj.bal.BAL;
import nl.it.fixx.moknj.bal.core.FieldBal;
import nl.it.fixx.moknj.bal.core.TemplateBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.module.employee.EmployeeLinkBal;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// to do needs to be split up in actions. Action pattern follow chain of responsiblity
@Aspect
@Component
public class EmployeeBalAspect extends BAL<EmployeeRepository> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeBalAspect.class);

    private final EmployeeLinkBal employeeLinkBal;
    private final TemplateBal tempBal;
    private final UserBal userBal;
    private final FieldBal fieldBal;

    @Autowired
    public EmployeeBalAspect(EmployeeRepository EmployeeRepo, TemplateBal tempBal,
            EmployeeLinkBal employeeLinkBal, UserBal userBal, FieldBal fieldBal) {
        super(EmployeeRepo);
        this.employeeLinkBal = employeeLinkBal;
        this.tempBal = tempBal;
        this.userBal = userBal;
        this.fieldBal = fieldBal;
    }

    public void delete(Employee record, String menuId, String token, boolean cascade) throws Throwable {
        Employee result = repository.findOne(record.getId());
        if (result != null && cascade) {
            employeeLinkBal.getAllLinksByRecordId(record.getId(), token).stream().forEach((link) -> {
                employeeLinkBal.delete(link);
            });
            LOG.info("A request was issued for a sample name: " + record);
        }
    }

    

}
