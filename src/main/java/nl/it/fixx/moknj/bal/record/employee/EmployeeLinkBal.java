/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.bal.record.employee;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.core.access.MainAccessBal;
import nl.it.fixx.moknj.bal.record.RecordLinkable;
import nl.it.fixx.moknj.bal.record.RepositoryChain;
import nl.it.fixx.moknj.bal.record.RepositoryContext;
import static nl.it.fixx.moknj.domain.core.global.GlobalMenuType.GBL_MT_EMPLOYEE;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.EmployeeLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeLinkBal extends RepositoryChain<EmployeeLinkRepository>
        implements RecordLinkable<EmployeeLink> {

    private final UserBal userBal;
    private final EmployeeBal employeeBal;
    private final MainAccessBal mainAccessBal;
    private final EmployeeLinkAccess empLinkAccess;

    @Autowired
    public EmployeeLinkBal(RepositoryContext context, MainAccessBal mainAccessBal,
            UserBal userBal, EmployeeBal employeeBal, EmployeeLinkAccess empLinkAccess) {
        super(context.getRepository(EmployeeLinkRepository.class));
        this.mainAccessBal = mainAccessBal;
        this.userBal = userBal;
        this.employeeBal = employeeBal;
        this.empLinkAccess = empLinkAccess;
    }

    @Override
    public void delete(EmployeeLink link) {
        repository.delete(link);
    }

    @Override
    public void save(EmployeeLink link) {
        repository.save(link);
    }

    @Override
    public List<EmployeeLink> getAllLinks(String token) throws BalException {
        try {
            Set<EmployeeLink> results = new HashSet<>();
            for (Menu menu : mainAccessBal.getUserMenus(token)) {
                if (menu.getMenuType().equals(GBL_MT_EMPLOYEE)) {
                    for (Template temp : menu.getTemplates()) {
                        List<Employee> employees = employeeBal.getAll(temp.getId(), menu.getId(), token);
                        for (Employee employee : employees) {
                            results.addAll(empLinkAccess.filterRecordAccess(
                                    getAllLinksByRecordId(employee.getId(), token),
                                    menu.getId(),
                                    temp.getId(),
                                    userBal.getUserByToken(token)));
                        }
                    }
                }
            }
            return results.stream().collect(Collectors.toList());
        } catch (Exception e) {
            throw new BalException("Error while trying to find all employee links", e);
        }
    }

    @Override
    public List<EmployeeLink> getAllLinksByRecordId(String recordId, String token) throws BalException {
        try {
            Set<EmployeeLink> results = new HashSet<>();
            for (Menu menu : mainAccessBal.getUserMenus(token)) {
                if (menu.getMenuType().equals(GBL_MT_EMPLOYEE)) {
                    for (Template temp : menu.getTemplates()) {
                        results.addAll(empLinkAccess.filterRecordAccess(
                                repository.getAllByEmployeeId(recordId),
                                menu.getId(),
                                temp.getId(),
                                userBal.getUserByToken(token)));
                    }
                }
            }
            return results.stream().collect(Collectors.toList());
        } catch (Exception e) {
            throw new BalException("Error while trying to find all employee links", e);
        }
    }

}
