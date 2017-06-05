package nl.it.fixx.moknj.bal.record.employee;

import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.core.access.AccessBal;
import nl.it.fixx.moknj.bal.record.RecordLinkAccess;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.exception.BalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeLinkAccess extends RecordLinkAccess<EmployeeLink> {

    @Autowired
    public EmployeeLinkAccess(MenuBal menuBal, AccessBal accessBal,
            EmployeeBal employeeBal, UserBal userBal) {
        super(menuBal, accessBal, employeeBal, userBal);
    }

    @Override
    public void setRecordViewValues(String recordId, EmployeeLink link) throws BalException {
        try {
            Employee employee = (Employee) recordBal.get(recordId);
            User linkedUser = userBal.getUserById(employee.getResourceId());
            String fullname = linkedUser.getFirstName() + " " + linkedUser.getSurname();
            link.setUser(fullname);
            link.setActionValue(link.getAction().getDisplayValue());
        } catch (Exception e) {
            throw new BalException("Error while to set Review values for link access check", e);
        }
    }

}
