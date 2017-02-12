/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.controller;

import nl.it.fixx.moknj.bal.EmployeeBal;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.repository.RepositoryContext;
import nl.it.fixx.moknj.response.EmployeeResponse;
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
    private RepositoryContext factory;

    @RequestMapping(value = "/add/{menuId}/{id}", method = RequestMethod.POST)
    public EmployeeResponse add(@PathVariable String id,
            @PathVariable String menuId,
            @RequestBody Employee employee,
            @RequestParam String access_token) {
        EmployeeResponse response = new EmployeeResponse();
        try {
            EmployeeBal bal = new EmployeeBal(factory);
            if (employee == null || employee.getMenuScopeIds().isEmpty()) {
                throw new Exception("No menu id provided for the employee record");
            }

            Employee savedEmployee = bal.save(id, menuId, employee, access_token);
            response.setSuccess(true);
            response.setEmployee(savedEmployee);
            response.setMessage("Successfully saved employee");
            return response;
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
     * @param access_token
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/get/all/{templateId}/{menuId}", method = RequestMethod.POST)
    public EmployeeResponse getAllEmployes(@PathVariable String templateId,
            @PathVariable String menuId, @RequestParam String access_token) throws Exception {
        EmployeeResponse response = new EmployeeResponse();
        try {
            response.setEmployees(new EmployeeBal(factory).
                    getAll(templateId, menuId, access_token));
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
            return response;
        }
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
        try {
            response.setEmployee(new EmployeeBal(factory).get(id));
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
            return response;
        }
        return response;
    }

    /**
     * Deletes employee
     *
     * @param employee
     * @param menuId
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/delete/{menuId}", method = RequestMethod.POST)
    public EmployeeResponse delete(@RequestBody Employee employee,
            @PathVariable String menuId,
            @RequestParam String access_token) {
        // to insure that the below fields have no influence on find all.
        EmployeeResponse response = new EmployeeResponse();
        try {
            new EmployeeBal(factory).delete(employee, menuId, access_token, false);
            response.setMessage("Removed employee record successfully.");
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }
}
