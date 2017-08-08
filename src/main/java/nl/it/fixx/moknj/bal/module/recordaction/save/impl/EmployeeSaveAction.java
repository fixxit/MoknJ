package nl.it.fixx.moknj.bal.module.recordaction.save.impl;

import java.util.Objects;
import nl.it.fixx.moknj.bal.core.FieldBal;
import nl.it.fixx.moknj.bal.core.TemplateBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.module.employee.EmployeeLinkBal;
import nl.it.fixx.moknj.bal.module.recordaction.save.SaveActionBase;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeAction;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSaveAction extends SaveActionBase<EmployeeLink, Employee, EmployeeRepository> {

    private final EmployeeLinkBal employeeLinkBal;
    private final TemplateBal tempBal;
    private final UserBal userBal;
    private final FieldBal fieldBal;

    @Autowired
    public EmployeeSaveAction(EmployeeLinkBal employeeLinkBal, TemplateBal tempBal,
            UserBal userBal, FieldBal fieldBal, EmployeeRepository repository) {
        super(repository);
        this.employeeLinkBal = employeeLinkBal;
        this.tempBal = tempBal;
        this.userBal = userBal;
        this.fieldBal = fieldBal;
    }

    @Override
    public boolean valid(Object domain) {
        return (domain instanceof Employee);
    }

    @Override
    public EmployeeLink before(Employee domain) {
        EmployeeLink join = new EmployeeLink();
        // Set audit logs
        join.setCreatedDate(domain.getCreatedDate());
        join.setCreatedBy(domain.getLastModifiedBy());

        if (domain.getTypeId() != null) {
            Template template = tempBal.getTemplateById(domain.getTypeId());
            if (template != null) {
                join.setTemplate(template.getName());
            }
        }

        if (domain.getId() != null) {
            // get the field changes. This code scans fields for any
            // changes then adds its to the string builder and saves
            // it to the audit trail.
            Employee employee = repository.findOne(domain.getId());
            StringBuilder changes = new StringBuilder();

            if (!Objects.equals(employee.getDetails(), domain.getDetails())) {
                for (FieldValue dbFieldValue : employee.getDetails()) {
                    for (FieldValue newFieldValue : domain.getDetails()) {
                        if (dbFieldValue.getId().equals(newFieldValue.getId())) {
                            if (!dbFieldValue.getValue().equals(newFieldValue.getValue())) {
                                // get field details
                                FieldDetail detail = fieldBal.get(dbFieldValue.getId());
                                StringBuilder change = new StringBuilder();
                                change.append(detail.getName()).append(" value [");
                                change.append(dbFieldValue.getValue()).append("] ");
                                change.append("changed to [").append(newFieldValue.getValue()).append("], ");
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
                join.setChanges(changes.toString());
            } else {
                join.setChanges("NO_CHANGE");
            }
        } else {
            join.setChanges(toAuditString(domain));
        }

        // Set action
        join.setAction(domain.getId() != null
                ? EmployeeAction.EMP_ACTION_EDIT
                : EmployeeAction.EMP_ACTION_NEW);

        // gets the user edited the domain.
        if (domain.getResourceId() != null) {
            User linkedUser = userBal.getUserById(domain.getResourceId());
            String fullname = linkedUser.getFirstName() + " " + linkedUser.getSurname();
            join.setUser(fullname);
        }
        return join;
    }

    @Override
    public void after(EmployeeLink join, Employee domain) {
        join.setEmployeeId(domain.getId());

        // make sure action is added to audit.
        if (join.getAction() != null) {
            if (!"NO_CHANGE".equals(join.getChanges())) {
                employeeLinkBal.save(join);
            }
        }
    }

    public String toAuditString(Employee emp) {
        StringBuilder changedFields = new StringBuilder();
        changedFields.append(emp.getMenu());
        changedFields.append(", ");
        changedFields.append(emp.getEmployee());
        if (emp.getDetails() != null && !emp.getDetails().isEmpty()) {
            StringBuilder fields = new StringBuilder();
            for (FieldValue field : emp.getDetails()) {
                FieldDetail dbField = fieldBal.get(field.getId());
                String fieldName = dbField.getName();
                String fieldValue = field.getValue();
                fields.append(fieldName);
                fields.append("=");
                fields.append(fieldValue);
                fields.append(",");
            }

            if (fields.toString().endsWith(",")) {
                fields = new StringBuilder(fields.toString().substring(0,
                        fields.toString().length() - 1));
            }
            changedFields.append(", value={");
            changedFields.append(fields);
            changedFields.append("}");
        }

        changedFields.append(", createdDate=");
        changedFields.append(emp.getCreatedDate());
        changedFields.append(", createdBy=");
        changedFields.append(emp.getCreatedBy());
        return changedFields.toString();
    }

}
