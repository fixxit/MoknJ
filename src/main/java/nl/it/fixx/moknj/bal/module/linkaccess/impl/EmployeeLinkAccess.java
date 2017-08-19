package nl.it.fixx.moknj.bal.module.linkaccess.impl;

import nl.it.fixx.moknj.bal.core.access.AccessCoreBal;
import nl.it.fixx.moknj.bal.core.menu.MenuCoreBal;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.bal.module.impl.EmployeeBal;
import nl.it.fixx.moknj.bal.module.linkaccess.ModuleLinkAccessBal;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.exception.AccessException;
import nl.it.fixx.moknj.exception.BalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeLinkAccess extends ModuleLinkAccessBal<EmployeeLink, EmployeeBal> {

    @Autowired
    public EmployeeLinkAccess(MenuCoreBal menuBal, AccessCoreBal accessBal,
            EmployeeBal employeeBal, UserCoreBal userBal) {
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
            throw new AccessException("Error while to set view values for link access check", e);
        }
    }

}