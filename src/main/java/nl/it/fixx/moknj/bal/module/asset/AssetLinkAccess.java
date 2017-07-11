package nl.it.fixx.moknj.bal.module.asset;

import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.module.ModuleLinkAccessBal;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetLinkAccess extends ModuleLinkAccessBal<AssetLink, AssetBal> {

    @Autowired
    public AssetLinkAccess(MenuBal menuBal, AccessBal accessBal, AssetBal recordBal, UserBal userBal) {
        super(menuBal, accessBal, recordBal, userBal);
    }

}
