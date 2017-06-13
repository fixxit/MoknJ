package nl.it.fixx.moknj.bal.module.employee;

import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.module.ModuleAccessBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmployeeAccessBal extends ModuleAccessBal<Employee> {

    private final UserBal userBal;
    private final AccessBal accessBal;

    @Autowired
    public EmployeeAccessBal(EmployeeRepository repository, UserBal userBal, AccessBal accessBal) {
        this.userBal = userBal;
        this.accessBal = accessBal;
    }

    @Override
    @Before("execution(* nl.it.fixx.moknj.bal.module.employee.EmployeeBal.save(String, String, nl.it.fixx.moknj.domain.modules.employee.Employee, String)) && args(templateId, menuId, record, token)")
    public void hasSaveAccess(JoinPoint joinPoint, String templateId, String menuId, Employee record, String token) throws BalException {
        try {
            if (record.getId() != null) {
                // check if user has acess for edit record
                User user = userBal.getUserByToken(token);
                if (!accessBal.hasAccess(user, menuId, templateId, GlobalAccessRights.EDIT)) {
                    throw new BalException("This user does not have sufficient "
                            + "access rights to update this employee!");
                }
            } else {
                // check if user has acess for new record
                User user = userBal.getUserByToken(token);
                if (!accessBal.hasAccess(user, menuId, templateId, GlobalAccessRights.NEW)) {
                    throw new BalException("This user does not have sufficient "
                            + "access rights to create this employee!");
                }
            }
        } catch (Exception e) {
            throw new BalException(e);
        }
    }

}
