package nl.it.fixx.moknj.controller;

import nl.it.fixx.moknj.bal.module.ModuleBal;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.response.EmployeeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final ModuleBal<Employee> employeeBal;

    @Autowired
    public EmployeeController(@Qualifier("employeeBal") ModuleBal employeeBal) {
        this.employeeBal = employeeBal;
    }

    @RequestMapping(value = "/add/{menuId}/{id}", method = RequestMethod.POST)
    public EmployeeResponse add(@PathVariable String id,
            @PathVariable String menuId,
            @RequestBody Employee employee,
            @RequestParam String access_token) {
        EmployeeResponse response = new EmployeeResponse();
        if (employee == null || employee.getMenuScopeIds().isEmpty()) {
            throw new BalException("No menu id provided for the employee record");
        }
        response.setSuccess(true);
        response.setEmployee(employeeBal.save(id, menuId, employee, access_token));
        response.setMessage("Successfully saved employee");
        return response;
    }

    /**
     * Gets a employee by template id and menu id
     *
     * @param templateId
     * @param menuId
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/get/all/{templateId}/{menuId}", method = RequestMethod.POST)
    public EmployeeResponse getAllEmployes(@PathVariable String templateId,
            @PathVariable String menuId, @RequestParam String access_token) {
        EmployeeResponse response = new EmployeeResponse();
        response.setEmployees(employeeBal.getAll(templateId, menuId, access_token));
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
        response.setEmployee(employeeBal.get(id));
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
        employeeBal.delete(employee, menuId, access_token, false);
        response.setMessage("Removed employee record successfully.");
        response.setSuccess(true);
        return response;
    }
}
