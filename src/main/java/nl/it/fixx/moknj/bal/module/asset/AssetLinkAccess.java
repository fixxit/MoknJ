package nl.it.fixx.moknj.bal.module.asset;

import nl.it.fixx.moknj.bal.core.access.AccessCoreBalImpl;
import nl.it.fixx.moknj.bal.core.menu.MenuCoreBal;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.bal.module.ModuleLinkAccessBal;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetLinkAccess extends ModuleLinkAccessBal<AssetLink, AssetBal> {

    @Autowired
    public AssetLinkAccess(MenuCoreBal menuBal, AccessCoreBalImpl accessBal, AssetBal recordBal, UserCoreBal userBal) {
        super(menuBal, accessBal, recordBal, userBal);
    }

}
