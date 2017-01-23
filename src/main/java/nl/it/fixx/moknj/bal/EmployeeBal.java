/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.bal;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import nl.it.fixx.moknj.repository.MenuRepository;

/**
 * Employee Business Access Layer
 *
 * @author adriaan
 */
public class EmployeeBal implements RecordBal, BusinessAccessLayer {

    private final EmployeeRepository employeeRep;// main Employee Repository
    private final MenuRepository menuRep;

    public EmployeeBal(EmployeeRepository employeeRep, MenuRepository menuRep) {
        this.employeeRep = employeeRep;
        this.menuRep = menuRep;
    }

    /**
     * Gets a employee by template id and menu id
     *
     * @param templateId
     * @param menuId
     * @return
     */
    @Override
    public List<Employee> getAll(String templateId, String menuId) {
        List<Employee> employees = new ArrayList<>();
        List<Employee> records = employeeRep.getAllByTypeId(templateId);
        // Gets custom template settings saved to menu.
        Menu menu = menuRep.findOne(menuId);
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

        return employees;
    }

}
