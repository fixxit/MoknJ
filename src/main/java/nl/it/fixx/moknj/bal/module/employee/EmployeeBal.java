package nl.it.fixx.moknj.bal.module.employee;

import java.text.SimpleDateFormat;
import java.util.Date;
import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nl.it.fixx.moknj.bal.module.ModuleBaseBal;
import nl.it.fixx.moknj.bal.module.validator.access.Access;
import nl.it.fixx.moknj.bal.module.validator.access.AccessValidation;
import nl.it.fixx.moknj.bal.module.validator.field.FieldValidation;
import nl.it.fixx.moknj.bal.module.validator.field.Module;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.exception.AccessException;

/**
 * Employee Business Access Layer
 *
 * @author adriaan
 */
@Service
public class EmployeeBal extends ModuleBaseBal<Employee, EmployeeRepository> {

    @Autowired
    public EmployeeBal(EmployeeRepository employeeRepo, MenuBal menuBal,
            UserBal userBal, AccessBal accessBal) {
        super(employeeRepo, menuBal, userBal, accessBal);
    }

    @Override
    @AccessValidation(access = Access.SAVE)
    @FieldValidation(module = Module.EMPLOYEE)
    public Employee save(String templateId, String menuId, Employee record, String token) throws BalException {
        if (templateId != null) {
            final String createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

            record.setTypeId(templateId);

            // Get user details who logged this employee using the token.
            User user = userBal.getUserByToken(token);
            if (user != null && user.isSystemUser()) {
                record.setLastModifiedBy(user.getUserName());
            } else {
                throw new AccessException("Employee save error, could not find system"
                        + " user for this token");
            }
            // Save employee
            record.setLastModifiedDate(createdDate);
            if (record.getId() == null) {
                record.setCreatedBy(user.getUserName());
                record.setCreatedDate(createdDate);
            } else {
                Employee dbEmployee = repository.findOne(record.getId());
                record.setCreatedBy(dbEmployee.getCreatedBy());
                record.setCreatedDate(dbEmployee.getCreatedDate());
            }

            if (record.getId() == null) {
                User emp = userBal.getUserById(record.getResourceId());
                if (emp != null) {
                    record.setEmployee(emp.getFirstName() + " " + emp.getSurname());
                }

                if (menuId != null && !menuId.trim().isEmpty()) {
                    Menu menu = menuBal.getMenuById(menuId);
                    if (menu != null) {
                        record.setMenu(menu.getName());
                    }
                }
            }

            return repository.save(record);
        } else {
            throw new BalException("No employee type id provided.");
        }
    }
}
