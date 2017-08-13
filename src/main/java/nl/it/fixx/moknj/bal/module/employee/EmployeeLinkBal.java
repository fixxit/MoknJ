package nl.it.fixx.moknj.bal.module.employee;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.it.fixx.moknj.bal.core.MainAccessBal;
import nl.it.fixx.moknj.bal.BalBase;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.bal.module.ModuleBal;
import static nl.it.fixx.moknj.domain.core.global.GlobalMenuType.GBL_MT_EMPLOYEE;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.repository.EmployeeLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nl.it.fixx.moknj.bal.module.ModuleLinkBal;
import org.springframework.beans.factory.annotation.Qualifier;

@Service("employeeLinkBal")
public class EmployeeLinkBal extends BalBase<EmployeeLinkRepository>
        implements ModuleLinkBal<EmployeeLink> {

    private final UserCoreBal userBal;
    private final ModuleBal<Employee> employeeBal;
    private final MainAccessBal mainAccessBal;
    private final EmployeeLinkAccess empLinkAccess;

    @Autowired
    public EmployeeLinkBal(EmployeeLinkRepository employeeLinkRepo,
            MainAccessBal mainAccessBal, UserCoreBal userBal,
            @Qualifier("employeeBal") ModuleBal<Employee> employeeBal,
            EmployeeLinkAccess empLinkAccess) {
        super(employeeLinkRepo);
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
    public List<EmployeeLink> getAllLinks(String token) {
        Set<EmployeeLink> results = new HashSet<>();
        mainAccessBal.getUserMenus(token).stream().filter((menu)
                -> (menu.getMenuType().equals(GBL_MT_EMPLOYEE))).forEachOrdered((menu) -> {
            menu.getTemplates().forEach((temp) -> {
                List<Employee> employees = employeeBal.getAll(temp.getId(), menu.getId(), token);
                employees.forEach((employee) -> {
                    results.addAll(empLinkAccess.filterRecordAccess(
                            getAllLinksByRecordId(employee.getId(), token),
                            menu.getId(),
                            temp.getId(),
                            userBal.getUserByToken(token)));
                });
            });
        });
        return results.stream().collect(Collectors.toList());

    }

    @Override
    public List<EmployeeLink> getAllLinksByRecordId(String recordId, String token) {
        Set<EmployeeLink> results = new HashSet<>();
        mainAccessBal.getUserMenus(token).stream().filter((menu)
                -> (menu.getMenuType().equals(GBL_MT_EMPLOYEE))).forEachOrdered((menu) -> {
            menu.getTemplates().forEach((temp) -> {
                results.addAll(empLinkAccess.filterRecordAccess(
                        repository.getAllByEmployeeId(recordId),
                        menu.getId(),
                        temp.getId(),
                        userBal.getUserByToken(token)));
            });
        });
        return results.stream().collect(Collectors.toList());
    }

}
