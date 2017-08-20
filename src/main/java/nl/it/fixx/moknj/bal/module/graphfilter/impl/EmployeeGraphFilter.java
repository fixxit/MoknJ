package nl.it.fixx.moknj.bal.module.graphfilter.impl;

import java.util.List;
import nl.it.fixx.moknj.bal.core.access.MainAccessCoreBal;
import nl.it.fixx.moknj.bal.module.graphfilter.GraphFilterBase;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.bal.module.ModuleBal;
import nl.it.fixx.moknj.domain.core.global.GlobalMenuType;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EmployeeGraphFilter extends GraphFilterBase<Employee> {

    private final ModuleBal<Employee> module;

    @Autowired
    public EmployeeGraphFilter(MainAccessCoreBal mainAccessBal,
            @Qualifier("employeeBal") ModuleBal<Employee> module,
            UserCoreBal userBal) {
        super(mainAccessBal, userBal);
        this.module = module;
    }

    @Override
    public String getDefaultValueForModule() {
        return "Employee";
    }

    @Override
    public boolean valid(Graph graphInfo, String token) {
        final Menu menu = mainAccessBal.getMenu(graphInfo.getMenuId(), token);
        return GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType());
    }

    @Override
    public List<Employee> getData(Graph graphInfo, Menu menu, Template template, String token) {
        return module.getAll(template.getId(), menu.getId(), token);
    }
}
