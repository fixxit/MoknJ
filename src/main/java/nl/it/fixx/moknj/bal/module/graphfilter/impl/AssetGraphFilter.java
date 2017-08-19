package nl.it.fixx.moknj.bal.module.graphfilter.impl;

import nl.it.fixx.moknj.bal.core.access.MainAccessCoreBal;
import nl.it.fixx.moknj.bal.module.graphfilter.GraphFilterBase;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.bal.module.impl.AssetBal;
import nl.it.fixx.moknj.bal.module.link.impl.AssetLinkBal;
import nl.it.fixx.moknj.domain.core.global.GlobalMenuType;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetGraphFilter extends GraphFilterBase<AssetBal> {

    @Autowired
    public AssetGraphFilter(MainAccessCoreBal mainAccessBal, AssetBal module, AssetLinkBal linkBal, UserCoreBal userBal) {
        super(mainAccessBal, module, linkBal, userBal);
    }

    @Override
    public String getDefaultValueForModule() {
        return "Asset";
    }

    @Override
    public boolean valid(Graph graphInfo, String token) {
        final Menu menu = mainAccessBal.getMenu(graphInfo.getMenuId(), token);
        return GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType());
    }

}
