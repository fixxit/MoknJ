/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeAction;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.repository.EmployeeLinkRepository;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import nl.it.fixx.moknj.repository.FieldDetailRepository;
import nl.it.fixx.moknj.repository.MenuRepository;
import nl.it.fixx.moknj.repository.TemplateRepository;
import nl.it.fixx.moknj.repository.UserRepository;
import nl.it.fixx.moknj.response.EmployeeResponse;
import nl.it.fixx.moknj.security.OAuth2SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adriaan
 */
@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRep;// main Employee Repository
    @Autowired
    private FieldDetailRepository fieldRep; // Employee FieldValue Detail Repository
    @Autowired
    private UserRepository userRep;
    @Autowired
    private MenuRepository menuRep;
    @Autowired
    private EmployeeLinkRepository employeeLinkRep;
    @Autowired
    private TemplateRepository typeRep;

    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    public EmployeeResponse add(@PathVariable String id,
            @RequestBody Employee passedEmployee,
            @RequestParam String access_token) {
        EmployeeResponse response = new EmployeeResponse();
        EmployeeLink audit = new EmployeeLink();
        try {
            if (id != null) {
                passedEmployee.setTypeId(id);

                // checks if the original object differs from new saved object
                // stop the unique filter form check for duplicates on employee
                // which has not changed from the db version...
                String flag = null;
                if (passedEmployee.getId() != null) {
                    Employee dbEmployee = employeeRep.findOne(passedEmployee.getId());
                    if (passedEmployee.equals(dbEmployee)) {
                        flag = "no_changes";
                    } else {
                        flag = "has_changes";
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
                for (Employee employee : employeeRep.getAllByTypeId(id)) {
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
                    response.setSuccess(false);
                    response.setMessage(message);
                    return response;
                }

                // Get user details who logged this employee using the token.
                User user = userRep.findByUserName(OAuth2SecurityConfig.getUserForToken(access_token));
                if (user != null && user.isSystemUser()) {
                    String fullname = user.getFirstName() + " " + user.getSurname();
                    if (!fullname.trim().isEmpty()) {
                        passedEmployee.setLastModifiedBy(fullname);
                    } else {
                        passedEmployee.setLastModifiedBy(user.getUserName());
                    }
                } else {
                    response.setSuccess(false);
                    response.setMessage("Employee save error, could not find system"
                            + " user for this token");
                    return response;
                }

                String createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

                // Save employee
                passedEmployee.setLastModifiedDate(createdDate);
                // Set audit logs
                audit.setCreatedDate(createdDate);
                audit.setCreatedBy(passedEmployee.getLastModifiedBy());

                if (passedEmployee.getTypeId() != null) {
                    Template template = typeRep.findOne(passedEmployee.getTypeId());
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
                    audit.setChanges("n/a");
                }

                // Set action
                if (passedEmployee.getId() != null) {
                    audit.setAction(EmployeeAction.EMP_ACTION_EDIT);
                } else {
                    audit.setAction(EmployeeAction.EMP_ACTION_NEW);
                }

                // gets the user edited the record.
                if (passedEmployee.getResourceId() != null) {
                    User linkedUser = userRep.findById(passedEmployee.getResourceId());
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

                response.setSuccess(true);
                response.setEmployee(passedEmployee);
                response.setMessage("saved employee[" + savedEmployee.getId() + "]");
                return response;
            } else {
                response.setSuccess(false);
                response.setMessage("No employee type id provided.");
                return response;
            }
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
            return response;
        }
    }

    /**
     * Gets a employee by template id and menu id
     *
     * @param templateId
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/get/all/{templateId}/{menuId}", method = RequestMethod.POST)
    public EmployeeResponse getAllEmployes(@PathVariable String templateId, @PathVariable String menuId) {
        EmployeeResponse response = new EmployeeResponse();
        List<Employee> employees = new ArrayList<>();
        List<Employee> records = employeeRep.getAllByTypeId(templateId);
        // Gets custom template settings saved to menu.
        Menu menu = menuRep.findOne(menuId);
        menu.getTemplates().stream().filter((template)
                -> (template.getId().equals(templateId))).forEach((Template template)
                -> {
            records.stream().forEach((Employee record) -> {
                boolean inScope = false;
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

        response.setEmployees(employees);
        return response;
    }

    /**
     * Gets the employee by id
     *
     * @param id
     * @return Employee
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public EmployeeResponse get(@PathVariable String id) {
        EmployeeResponse response = new EmployeeResponse();
        response.setEmployee(employeeRep.findOne(id));
        return response;
    }

    /**
     * Deletes employee
     *
     * @param employee
     * @return
     */
    @RequestMapping(value = "/delete/", method = RequestMethod.POST)
    public EmployeeResponse delete(@RequestBody Employee employee) {
        // to insure that the below fields have no influence on find all.
        EmployeeResponse response = new EmployeeResponse();
        Employee result = employeeRep.findOne(employee.getId());
        try {
            // todo needs a check for linked resources ...
            if (result != null) {
                employeeRep.delete(result);
                response.setSuccess(true);
                response.setMessage("Removed employee "
                        + "[" + employee.getId() + "] successfully.");
            } else {
                response.setSuccess(false);
                response.setMessage("Could not remove employee "
                        + "[" + employee.getId() + "] not found in db");
            }
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }
}

//    /**
//     * Gets a employee by template id and menu id
//     *
//     * @param templateId
//     * @param menuId
//     * @return
//     */
//    @RequestMapping(value = "/get/all/obj/{templateId}/{menuId}", method = RequestMethod.POST)
//    public EmployeeResponse getAllInEmployeeStr(@PathVariable String templateId, @PathVariable String menuId) {
//        EmployeeResponse response = new EmployeeResponse();
//        List<Employee> employees = new ArrayList<>();
//        List<Employee> records = employeeRep.getAllByTypeId(templateId);
//        // Gets custom template settings saved to menu.
//        Menu menu = menuRep.findOne(menuId);
//        menu.getTemplates().stream().filter((template)
//                -> (template.getId().equals(templateId))).forEach((Template template)
//                -> {
//            records.stream().forEach((Employee record) -> {
//                boolean inScope = false;
//                // checks if scope check is required for this template.
//                if (template.isAllowScopeChallenge()) {
//                    inScope = record.getMenuScopeIds().contains(menuId);
//                } else {
//                    inScope = true;
//                }
//                // if employee is inscope allow adding of employee.
//                if (inScope) {
//                    // Find user whic is hidden and remove the employe record link
//                    // to that record.
//                    //User resource = userRep.findById(record.getResourceId());
//                    // checks if the employee is hidden.
//                    if (!record.isHidden()) {
//                        employees.add(record);
//                    }
//                }
//            });
//        });
//
//        StringBuilder empObj = new StringBuilder();
//        empObj.append("[");
//        for (Employee emp : employees) {
//            List<FieldValue> empValues = emp.getDetails();
//            empObj.append("{");
//            for (FieldValue field : empValues) {
//                FieldDetail detail = fieldRep.findOne(field.getId());
//                if (detail != null) {
//                    empObj.append("\"");
//                    empObj.append(detail.getName().replaceAll(" ", "_"));
//                    empObj.append("\":{");
//
//                    empObj.append("\"type\":");
//                    empObj.append("\"");
//                    empObj.append(detail.getType());
//                    empObj.append("\",");
//
//                    empObj.append("\"value\":");
//                    empObj.append("\"");
//                    empObj.append(field.getValue());
//                    empObj.append("\"},");
//                }
//            }
//            if (empObj.toString().endsWith(",")) {
//                empObj = new StringBuilder(empObj.toString().substring(0,
//                        empObj.toString().length() - 1));
//            }
//
//            empObj.append("},");
//        }
//        if (empObj.toString().endsWith(",")) {
//            empObj = new StringBuilder(empObj.toString().substring(0,
//                    empObj.toString().length() - 1));
//        }
//        empObj.append("]");
//
//        response.setEmployees(employees);
//        response.setMessage(empObj.toString());
//
//        return response;
//    }
