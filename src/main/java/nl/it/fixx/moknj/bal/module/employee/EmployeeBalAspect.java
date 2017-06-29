package nl.it.fixx.moknj.bal.module.employee;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import nl.it.fixx.moknj.bal.RepositoryBal;
import nl.it.fixx.moknj.bal.RepositoryContext;
import nl.it.fixx.moknj.bal.core.FieldBal;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.TemplateBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeAction;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.exception.AccessException;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmployeeBalAspect extends RepositoryBal<EmployeeRepository> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeBalAspect.class);

    private final EmployeeLinkBal employeeLinkBal;
    private final TemplateBal tempBal;
    private final UserBal userBal;
    private final FieldBal fieldBal;
    private final MenuBal menuBal;

    @Autowired
    public EmployeeBalAspect(RepositoryContext context, TemplateBal tempBal,
            EmployeeLinkBal employeeLinkBal, UserBal userBal, FieldBal fieldBal, MenuBal menuBal) {
        super(context.getRepository(EmployeeRepository.class));
        this.employeeLinkBal = employeeLinkBal;
        this.tempBal = tempBal;
        this.userBal = userBal;
        this.fieldBal = fieldBal;
        this.menuBal = menuBal;
    }

    @After("execution(* nl.it.fixx.moknj.bal.module.employee.EmployeeBal.delete(nl.it.fixx.moknj.domain.modules.employee.Employee, String, boolean)) && args(record, menuId, token, cascade)")
    public void deleteAfter(JoinPoint joinPoint, Employee record, String menuId, String token, boolean cascade) throws Throwable {
        Employee result = repository.findOne(record.getId());
        if (result != null && cascade) {
            employeeLinkBal.getAllLinksByRecordId(record.getId(), token).stream().forEach((link) -> {
                employeeLinkBal.delete(link);
            });
            LOG.info("A request was issued for a sample name: " + record);
        }
    }

    @Around("execution(* nl.it.fixx.moknj.bal.module.employee.EmployeeBal.save(String, String, nl.it.fixx.moknj.domain.modules.employee.Employee, String)) && args(templateId, menuId, record, token)")
    public void saveAround(ProceedingJoinPoint joinPoint, String templateId, String menuId, Employee record, String token) throws Throwable {
        if (templateId != null) {
            final String createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            EmployeeLink audit = new EmployeeLink();
            // Set audit logs
            audit.setCreatedDate(createdDate);
            audit.setCreatedBy(record.getLastModifiedBy());

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

            if (record.getTypeId() != null) {
                Template template = tempBal.getTemplateById(record.getTypeId());
                if (template != null) {
                    audit.setTemplate(template.getName());
                }
            }

            if (record.getId() != null) {
                // get the field changes. This code scans fields for any
                // changes then adds its to the string builder and saves
                // it to the audit trail.
                Employee employee = repository.findOne(record.getId());
                StringBuilder changes = new StringBuilder();

                if (!Objects.equals(employee.getDetails(), record.getDetails())) {
                    for (FieldValue dbFieldValue : employee.getDetails()) {
                        for (FieldValue newFieldValue : record.getDetails()) {
                            if (dbFieldValue.getId().equals(newFieldValue.getId())) {
                                if (!dbFieldValue.getValue().equals(newFieldValue.getValue())) {
                                    // get field details
                                    FieldDetail detail = fieldBal.get(dbFieldValue.getId());
                                    String change = detail.getName() + " value [";
                                    change += dbFieldValue.getValue() + "] ";
                                    change += "changed to [" + newFieldValue.getValue() + "], ";
                                    changes.append(change);
                                }
                            }
                        }
                    }
                }
                // Remove trailing comma's
                if (changes.toString() != null
                        && !"".equals(changes.toString())) {
                    if (changes.toString().trim().endsWith(",")) {
                        changes = new StringBuilder(changes.toString().trim().substring(0,
                                changes.toString().trim().length() - 1));
                    }
                    audit.setChanges(changes.toString());
                } else {
                    audit.setChanges("NO_CHANGE");
                }
            } else {
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
                audit.setChanges(toAuditString(record));
            }

            // Set action
            audit.setAction(record.getId() != null
                    ? EmployeeAction.EMP_ACTION_EDIT
                    : EmployeeAction.EMP_ACTION_NEW);

            // gets the user edited the record.
            if (record.getResourceId() != null) {
                User linkedUser = userBal.getUserById(record.getResourceId());
                String fullname = linkedUser.getFirstName() + " " + linkedUser.getSurname();
                audit.setUser(fullname);
            }

            // proceed and set the save return value to
            record = (Employee) joinPoint.proceed(new Object[]{templateId, menuId, record, token});
            // Set audit link employeeid
            audit.setEmployeeId(record.getId());

            // make sure action is added to audit.
            if (audit.getAction() != null) {
                if (!"NO_CHANGE".equals(audit.getChanges())) {
                    employeeLinkBal.save(audit);
                }
            }
        }
    }

    public String toAuditString(Employee emp) throws Exception {
        String str = emp.getMenu() + ", " + emp.getEmployee();
        if (emp.getDetails() != null && !emp.getDetails().isEmpty()) {
            String fields = "";

            for (FieldValue field : emp.getDetails()) {
                FieldDetail dbField = fieldBal.get(field.getId());
                String fieldName = dbField.getName();
                String fieldValue = field.getValue();
                fields += fieldName + "=" + fieldValue + ",";
            }

            if (fields.endsWith(",")) {
                fields = fields.substring(0, fields.length() - 1);
            }
            str += ", value={" + fields + "}";
        }

        str += ", createdDate=" + emp.getCreatedDate()
                + ", createdBy=" + emp.getCreatedBy();
        return str;
    }

}
