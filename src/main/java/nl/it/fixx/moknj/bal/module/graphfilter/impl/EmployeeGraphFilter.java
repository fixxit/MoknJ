package nl.it.fixx.moknj.bal.module.graphfilter.impl;

import nl.it.fixx.moknj.bal.core.access.MainAccessCoreBal;
import nl.it.fixx.moknj.bal.module.graphfilter.GraphFilterBase;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.bal.module.link.impl.AssetLinkBal;
import nl.it.fixx.moknj.bal.module.impl.EmployeeBal;
import nl.it.fixx.moknj.domain.core.global.GlobalMenuType;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeGraphFilter extends GraphFilterBase<EmployeeBal> {

    @Autowired
    public EmployeeGraphFilter(MainAccessCoreBal mainAccessBal, EmployeeBal module, AssetLinkBal linkBal, UserCoreBal userBal) {
        super(mainAccessBal, module, linkBal, userBal);
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

}
