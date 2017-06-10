package nl.it.fixx.moknj.bal.module.employee;

import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.module.ModuleChangeBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeChange extends ModuleChangeBal<EmployeeRepository, Employee> {

    private final UserBal userBal;
    private final AccessBal accessBal;

    @Autowired
    public EmployeeChange(EmployeeRepository repository, UserBal userBal, AccessBal accessBal) {
        super(repository);
        this.userBal = userBal;
        this.accessBal = accessBal;
    }

    @Override
    public String hasChange(Employee record, String templateId, String menuId, String token) throws BalException {
        try {
            String flag = null;
            if (record.getId() != null) {
                // check if user has acess for edit record
                User user = userBal.getUserByToken(token);
                if (!accessBal.hasAccess(user, menuId, templateId, GlobalAccessRights.EDIT)) {
                    throw new BalException("This user does not have sufficient "
                            + "access rights to update this employee!");
                }

                Employee employee = repository.findOne(record.getId());
                if (record.equals(employee)) {
                    flag = "no_changes";
                } else {
                    flag = "has_changes";
                }
            } else {
                // check if user has acess for new record
                User user = userBal.getUserByToken(token);
                if (!accessBal.hasAccess(user, menuId, templateId, GlobalAccessRights.NEW)) {
                    throw new BalException("This user does not have sufficient "
                            + "access rights to create this employee!");
                }
            }
            return flag;
        } catch (Exception e) {
            throw new BalException("Error trying to find change", e);
        }
    }

}
