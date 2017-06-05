package nl.it.fixx.moknj.bal.record.employee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import nl.it.fixx.moknj.bal.core.access.AccessBal;
import nl.it.fixx.moknj.bal.core.FieldBal;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.record.RecordBal;
import nl.it.fixx.moknj.bal.record.RepositoryChain;
import nl.it.fixx.moknj.bal.core.TemplateBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.record.RepositoryContext;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeAction;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Employee Business Access Layer
 *
 * @author adriaan
 */
@Service
public class EmployeeBal extends RepositoryChain<EmployeeRepository> implements RecordBal<Employee> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeBal.class);

    private final MenuBal menuBal;
    private final UserBal userBal;
    private final TemplateBal tempBal;
    private final AccessBal accessBal;
    private final FieldBal fieldBal;
    private final EmployeeLinkBal linkBal;

    @Autowired
    public EmployeeBal(MenuBal menuBal, UserBal userBal, TemplateBal tempBal,
            AccessBal accessBal, FieldBal fieldBal, RepositoryContext context) {
        super(context.getRepository(EmployeeRepository.class));
        this.menuBal = menuBal;
        this.userBal = userBal;
        this.tempBal = tempBal;
        this.accessBal = accessBal;
        this.fieldBal = fieldBal;
        this.linkBal = new EmployeeLinkBal(context, mainAccessBal, userBal, this, empLinkAccess);
    }

    /**
     * Gets a employee by template id and menu id
     *
     * @param templateId
     * @param menuId
     * @param token
     * @return
     */
    @Override
    public List<Employee> getAll(String templateId, String menuId, String token) throws BalException {
        try {
            List<Employee> employees = new ArrayList<>();
            User user = userBal.getUserByToken(token);
            if (accessBal.hasAccess(user, menuId, templateId, GlobalAccessRights.VIEW)) {
                List<Employee> records = repository.getAllByTypeId(templateId);
                // Gets custom template settings saved to menu.
                Menu menu = menuBal.getMenuById(menuId);
                menu.getTemplates().stream().filter((template)
                        -> (template.getId().equals(templateId))).forEach((Template template)
                        -> {
                    records.stream().forEach((Employee record) -> {
                        boolean inScope;
                        // checks if scope check is required for this template.
                        if (template.isAllowScopeChallenge()) {
                            inScope = record.getMenuScopeIds().contains(menuId);
                        } else {
                            inScope = true;
                        }
                        // if employee is inscope allow adding of employee.
                        if (inScope) {
                            // Find user whic is hidden and remove the employe record link
                            // to that record.
                            //User resource = userRep.findById(record.getResourceId());
                            // checks if the employee is hidden.
                            if (!record.isHidden()) {
                                employees.add(record);
                            }
                        }
                    });
                });
            }
            return employees;
        } catch (Exception e) {
            throw new BalException("Could not find all employees for template "
                    + "[" + templateId + "] and menu [" + menuId + "]", e);
        }
    }

    @Override
    public Employee save(String templateId, String menuId, Employee record, String token) throws BalException {
        try {
            if (templateId != null) {
                record.setTypeId(templateId);
                // checks if the original object differs from new saved object
                // stop the unique filter form check for duplicates on employee
                // which has not changed from the db version...
                String flag = new EmployeeChange(repository, userBal, accessBal).
                        hasChange(record, templateId, menuId, token);
                /**
                 * Find unique fields for employee and check if the current list
                 * of employees is unique for the field...
                 */
                Map<String, String> uniqueFields = new HashMap<>();
                Map<String, Boolean> unifieldIndicator = new HashMap<>();
                Map<String, List<String>> uniqueValues = new HashMap<>();

                // Get all the unique field ids
                List<FieldValue> newEmployeeFields = record.getDetails();
                newEmployeeFields.stream().forEach((field) -> {
                    try {
                        FieldDetail detail = fieldBal.getField(field.getId());
                        if (detail != null && detail.isUnique()) {
                            uniqueFields.put(field.getId(), detail.getName());
                        }
                    } catch (Exception ex) {
                        LOG.error("Error getting field detail", ex);
                    }
                });

                // Create a list of all the values for the unique employees
                for (Employee employee : repository.getAllByTypeId(templateId)) {
                    // if statement below checks that if update employee does not check
                    // it self to flag for duplication
                    if (!employee.getId().equals(record.getId())
                            && !"no_changes".equals(flag)) {
                        List<FieldValue> details = employee.getDetails();
                        details.stream().filter((field) -> (uniqueFields.keySet().contains(field.getId()))).map((field) -> {
                            if (uniqueValues.get(field.getId()) == null) {
                                uniqueValues.put(field.getId(), new ArrayList<>());
                            }
                            return field;
                        }).filter((field) -> (!uniqueValues.get(field.getId()).contains(field.getValue()))).forEach((field) -> {
                            uniqueValues.get(field.getId()).add(field.getValue());
                        });
                    }
                }

                // check if fields to be saved for employee has duplicates
                if (!uniqueValues.isEmpty()) {
                    newEmployeeFields.stream().filter((field) -> (uniqueValues.containsKey(field.getId()))).filter((field) -> (uniqueValues.get(field.getId()).contains(field.getValue()))).forEach((field) -> {
                        unifieldIndicator.put(field.getId(), true);
                    });
                }

                // generate duplication message
                if (!unifieldIndicator.isEmpty()) {
                    String message = unifieldIndicator.size() > 1
                            ? "Non unique values for fields ["
                            : "Non unique value for field ";
                    message = unifieldIndicator.keySet().stream().map((typeId) -> uniqueFields.get(typeId)).map((fieldName) -> fieldName + ",").reduce(message, String::concat);
                    if (message.endsWith(",")) {
                        message = message.substring(0, message.length() - 1);
                    }

                    message += unifieldIndicator.size() > 1
                            ? "]. Please check values"
                            : ". Please input new value";

                    throw new BalException(message);
                }

                // Get user details who logged this employee using the token.
                User user = userBal.getUserByToken(token);
                if (user != null && user.isSystemUser()) {
                    record.setLastModifiedBy(user.getUserName());
                } else {
                    throw new BalException("Employee save error, could not find system"
                            + " user for this token");
                }

                String createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

                // Save employee
                String date = createdDate;
                record.setLastModifiedDate(date);
                if (record.getId() == null) {
                    record.setCreatedBy(user.getUserName());
                    record.setCreatedDate(date);
                } else {
                    Employee dbEmployee = repository.findOne(record.getId());
                    record.setCreatedBy(dbEmployee.getCreatedBy());
                    record.setCreatedDate(dbEmployee.getCreatedDate());
                }

                EmployeeLink audit = new EmployeeLink();
                // Set audit logs
                audit.setCreatedDate(createdDate);
                audit.setCreatedBy(record.getLastModifiedBy());

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
                    Employee dbEmployee = repository.findOne(record.getId());
                    StringBuilder changes = new StringBuilder();

                    if (!Objects.equals(dbEmployee.getDetails(), record.getDetails())) {
                        for (FieldValue dbFieldValue : dbEmployee.getDetails()) {
                            for (FieldValue newFieldValue : record.getDetails()) {
                                if (dbFieldValue.getId().equals(newFieldValue.getId())) {
                                    if (!dbFieldValue.getValue().equals(newFieldValue.getValue())) {
                                        // get field details
                                        FieldDetail detail = fieldBal.getField(dbFieldValue.getId());
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
                if (record.getId() != null) {
                    audit.setAction(EmployeeAction.EMP_ACTION_EDIT);
                } else {
                    audit.setAction(EmployeeAction.EMP_ACTION_NEW);
                }

                // gets the user edited the record.
                if (record.getResourceId() != null) {
                    User linkedUser = userBal.getUserById(record.getResourceId());
                    String fullname = linkedUser.getFirstName() + " " + linkedUser.getSurname();
                    audit.setUser(fullname);
                }

                Employee savedEmployee = repository.save(record);
                audit.setEmployeeId(savedEmployee.getId());

                // make sure action is added to audit.
                if (audit.getAction() != null) {
                    if (!"NO_CHANGE".equals(audit.getChanges())) {
//                        linkBal.saveEmployeeLink(audit);
                    }
                }

                return savedEmployee;
            } else {
                throw new BalException("No employee type id provided.");
            }
        } catch (Exception e) {
            throw new BalException("Could not save employee due to internal error", e);
        }
    }

    @Override
    public Employee get(String id) throws Exception {
        try {
            Employee employee = repository.findOne(id);
            if (employee != null) {
                return employee;
            }

            throw new BalException("Could not find employee for id [" + id + "]");
        } catch (BalException e) {
            LOG.error("Error on trying to retieve employee for id [" + id + "]", e);
            throw e;
        }
    }

    @Override
    public void delete(Employee record, String menuId, String token, boolean cascade) throws BalException {
        try {
            Employee result = repository.findOne(record.getId());
            if (result != null) {
                String templateId = record.getTypeId();

                User user = userBal.getUserByToken(token);
                if (!accessBal.hasAccess(user, menuId, templateId,
                        GlobalAccessRights.DELETE)) {
                    throw new BalException("This user does not have sufficient "
                            + "access rights to delete this employee!");
                }

                if (cascade) {
                    // delete links
                      
//                    linkBal.getAllEmployeeLinksForEmployee(
//                            result.getId(), token).stream().forEach((link) -> {
//                        linkBal.deleteEmployeeLink(link);
//                    });
                    repository.delete(result);
                } else {
                    // hide asset by updating hidden field=
                    result.setHidden(true);
                    repository.save(result);
                    LOG.debug("This employee[" + result.getId() + "] is now "
                            + "hidden as audit links was detected");
                }
            } else {
                throw new BalException("Could not remove employee "
                        + "[" + record.getId() + "] not found in db");
            }
        } catch (Exception e) {
            throw new BalException("Error on trying to retieve employee for id [" + record + "]", e);
        }
    }

    public String toAuditString(Employee emp) throws Exception {
        String str = "";

        str += emp.getMenu() + ", " + emp.getEmployee();

        if (emp.getDetails() != null && !emp.getDetails().isEmpty()) {
            String fields = "";

            for (FieldValue field : emp.getDetails()) {
                FieldDetail dbField = fieldBal.getField(field.getId());
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

    @Override
    public boolean exists(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
