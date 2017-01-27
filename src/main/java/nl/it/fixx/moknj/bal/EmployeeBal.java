/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.bal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeAction;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.repository.EmployeeLinkRepository;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import nl.it.fixx.moknj.repository.FieldDetailRepository;
import nl.it.fixx.moknj.repository.RepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Employee Business Access Layer
 *
 * @author adriaan
 */
public class EmployeeBal implements RecordBal, BusinessAccessLayer {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeBal.class);
    private final EmployeeRepository employeeRep;
    private final FieldDetailRepository fieldRep;
    private final EmployeeLinkRepository employeeLinkRep;

    private final MenuBal menuBal;
    private final UserBal userBal;
    private final TemplateBal tempBal;
    private final AccessBal userAccessBall;

    public EmployeeBal(RepositoryFactory factory) {
        this.employeeRep = factory.getEmployeeRep();
        this.fieldRep = factory.getFieldDetailRep();
        this.employeeLinkRep = factory.getEmployeeLinkRep();

        this.userBal = new UserBal(factory);
        this.menuBal = new MenuBal(factory);
        this.tempBal = new TemplateBal(factory);
        this.userAccessBall = new AccessBal(factory);
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
    public List<Employee> getAll(String templateId, String menuId, String token) throws Exception {
        try {
            List<Employee> employees = new ArrayList<>();
            User user = userBal.getUserByToken(token);
            if (userAccessBall.hasAccess(user, menuId, templateId, GlobalAccessRights.VIEW)) {
                List<Employee> records = employeeRep.getAllByTypeId(templateId);
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
            LOG.error("Could not find all employees for template "
                    + "[" + templateId + "] and menu [" + menuId + "]", e);
            throw e;
        }
    }

    @Override
    public Employee save(String templateId, String menuId, Object record, String token) throws Exception {
        EmployeeLink audit = new EmployeeLink();
        try {
            if (templateId != null) {
                Employee passedEmployee = (Employee) record;

                passedEmployee.setTypeId(templateId);

                // checks if the original object differs from new saved object
                // stop the unique filter form check for duplicates on employee
                // which has not changed from the db version...
                String flag = null;
                if (passedEmployee.getId() != null) {
                    // check if user has acess for edit record
                    User user = userBal.getUserByToken(token);
                    if (!userAccessBall.hasAccess(user, menuId, templateId,
                            GlobalAccessRights.EDIT)) {
                        throw new Exception("This user does not have sufficient "
                                + "access rights to update this employee!");
                    }

                    Employee dbEmployee = employeeRep.findOne(passedEmployee.getId());
                    if (passedEmployee.equals(dbEmployee)) {
                        flag = "no_changes";
                    } else {
                        flag = "has_changes";
                    }
                } else {
                    // check if user has acess for new record
                    User user = userBal.getUserByToken(token);
                    if (!userAccessBall.hasAccess(user, menuId, templateId,
                            GlobalAccessRights.NEW)) {
                        throw new Exception("This user does not have sufficient "
                                + "access rights to create this employee!");
                    }
                }

                /**
                 * Find unique fields for employee and check if the current list
                 * of employees is unique for the field...
                 */
                Map<String, String> uniqueFields = new HashMap<>();
                Map<String, Boolean> unifieldIndicator = new HashMap<>();
                Map<String, List<String>> uniqueValues = new HashMap<>();

                // Get all the unique field ids
                List<FieldValue> newEmployeeFields = passedEmployee.getDetails();
                newEmployeeFields.stream().forEach((field) -> {
                    FieldDetail detail = fieldRep.findOne(field.getId());
                    if (detail != null && detail.isUnique()) {
                        uniqueFields.put(field.getId(), detail.getName());
                    }
                });

                // Create a list of all the values for the unique employees
                for (Employee employee : employeeRep.getAllByTypeId(templateId)) {
                    // if statement below checks that if update employee does not check
                    // it self to flag for duplication
                    if (!employee.getId().equals(passedEmployee.getId())
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

                    throw new Exception(message);
                }

                // Get user details who logged this employee using the token.
                User user = userBal.getUserByToken(token);
                if (user != null && user.isSystemUser()) {
                    passedEmployee.setLastModifiedBy(user.getUserName());
                } else {
                    throw new Exception("Employee save error, could not find system"
                            + " user for this token");
                }

                String createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

                // Save employee
                String date = createdDate;
                passedEmployee.setLastModifiedDate(date);
                if (passedEmployee.getId() == null) {
                    passedEmployee.setCreatedBy(user.getUserName());
                    passedEmployee.setCreatedDate(date);
                } else {
                    Employee dbEmployee = employeeRep.findOne(passedEmployee.getId());
                    passedEmployee.setCreatedBy(dbEmployee.getCreatedBy());
                    passedEmployee.setCreatedDate(dbEmployee.getCreatedDate());
                }

                // Set audit logs
                audit.setCreatedDate(createdDate);
                audit.setCreatedBy(passedEmployee.getLastModifiedBy());

                if (passedEmployee.getTypeId() != null) {
                    Template template = tempBal.getTemplateById(passedEmployee.getTypeId());
                    if (template != null) {
                        audit.setTemplate(template.getName());
                    }
                }

                if (passedEmployee.getId() != null) {
                    // get the field changes. This code scans fields for any
                    // changes then adds its to the string builder and saves
                    // it to the audit trail.
                    Employee dbEmployee = employeeRep.findOne(passedEmployee.getId());
                    StringBuilder changes = new StringBuilder();

                    if (!Objects.equals(dbEmployee.getDetails(), passedEmployee.getDetails())) {
                        for (FieldValue dbFieldValue : dbEmployee.getDetails()) {
                            for (FieldValue newFieldValue : passedEmployee.getDetails()) {
                                if (dbFieldValue.getId().equals(newFieldValue.getId())) {
                                    if (!dbFieldValue.getValue().equals(newFieldValue.getValue())) {
                                        // get field details
                                        FieldDetail detail = fieldRep.findOne(dbFieldValue.getId());
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
                    User emp = userBal.getUserById(passedEmployee.getResourceId());
                    if (emp != null) {
                        passedEmployee.setEmployee(emp.getFirstName() + " " + emp.getSurname());
                    }

                    if (menuId != null && !menuId.trim().isEmpty()) {
                        Menu menu = menuBal.getMenuById(menuId);
                        if (menu != null) {
                            passedEmployee.setMenu(menu.getName());
                        }
                    }

                    audit.setChanges(passedEmployee.toAuditString(fieldRep));
                }

                // Set action
                if (passedEmployee.getId() != null) {
                    audit.setAction(EmployeeAction.EMP_ACTION_EDIT);
                } else {
                    audit.setAction(EmployeeAction.EMP_ACTION_NEW);
                }

                // gets the user edited the record.
                if (passedEmployee.getResourceId() != null) {
                    User linkedUser = userBal.getUserById(passedEmployee.getResourceId());
                    String fullname = linkedUser.getFirstName() + " " + linkedUser.getSurname();
                    audit.setUser(fullname);
                }

                Employee savedEmployee = employeeRep.save(passedEmployee);
                audit.setEmployeeId(savedEmployee.getId());

                // make sure action is added to audit.
                if (audit.getAction() != null) {
                    if (!"NO_CHANGE".equals(audit.getChanges())) {
                        employeeLinkRep.save(audit);
                    }
                }

                return savedEmployee;
            } else {
                throw new Exception("No employee type id provided.");
            }
        } catch (Exception e) {
            LOG.error("Could not save employee due to internal error", e);
            throw e;
        }
    }

    @Override
    public Employee get(String id) throws Exception {
        try {
            Employee employee = employeeRep.findOne(id);
            if (employee != null) {
                return employee;
            }

            throw new Exception("Could not find employee for id [" + id + "]");
        } catch (Exception e) {
            LOG.error("Error on trying to retieve employee for id [" + id + "]", e);
            throw e;
        }
    }

    @Override
    public void delete(Object record, String token, boolean cascade) throws Exception {
        try {
            if (record instanceof Employee) {
                Employee employee = (Employee) record;
                Employee result = employeeRep.findOne(employee.getId());
                if (result != null) {
                    String menuId = employee.getMenuScopeIds().get(0);
                    String templateId = employee.getTypeId();

                    User user = userBal.getUserByToken(token);
                    if (!userAccessBall.hasAccess(user, menuId, templateId,
                            GlobalAccessRights.DELETE)) {
                        throw new Exception("This user does not have sufficient "
                                + "access rights to delete this employee!");
                    }

                    List<EmployeeLink> links = employeeLinkRep.getAllByEmployeeId(result.getId());
                    if (cascade) {
                        // delete links
                        links.stream().forEach((link) -> {
                            employeeLinkRep.delete(link);
                        });
                        employeeRep.delete(result);
                    } else {
                        // hide asset by updating hidden field=
                        result.setHidden(true);
                        employeeRep.save(result);
                        LOG.info("This employee[" + result.getId() + "] is now "
                                + "hidden as audit links was detected");
                    }
                } else {
                    throw new Exception("Could not remove employee "
                            + "[" + employee.getId() + "] not found in db");
                }
            }
        } catch (Exception e) {
            LOG.error("Error on trying to retieve employee for id [" + record + "]", e);
            throw e;
        }
    }

}
