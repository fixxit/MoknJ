package nl.it.fixx.moknj.bal.module.validator.access;

import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAccessModule extends AccessModuleBase<Employee> {

    @Autowired
    public EmployeeAccessModule(UserBal userBal, AccessBal accessBal) {
        super(userBal, accessBal);
    }

    @Override
    public boolean canValidate(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Employee) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getModule() {
        return "employee";
    }

}
