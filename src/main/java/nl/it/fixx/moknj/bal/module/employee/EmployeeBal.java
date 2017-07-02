package nl.it.fixx.moknj.bal.module.employee;

import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.FieldBal;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nl.it.fixx.moknj.bal.module.ModuleBaseBal;
import nl.it.fixx.moknj.bal.module.validator.access.AccessValidation;
import nl.it.fixx.moknj.bal.module.validator.field.FieldValidation;

/**
 * Employee Business Access Layer
 *
 * @author adriaan
 */
@Service
public class EmployeeBal extends ModuleBaseBal<Employee, EmployeeRepository> {

    @Autowired
    public EmployeeBal(EmployeeRepository employeeRepo, MenuBal menuBal,
            UserBal userBal, AccessBal accessBal, FieldBal fieldBal) {
        super(employeeRepo, menuBal, userBal, accessBal);
    }

    @AccessValidation(type = "save")
    @FieldValidation(module = "employee")
    @Override
    public Employee save(String templateId, String menuId, Employee record, String token) throws BalException {
        if (templateId != null) {
            record.setTypeId(templateId);
            return repository.save(record);
        } else {
            throw new BalException("No employee type id provided.");
        }
    }
}
