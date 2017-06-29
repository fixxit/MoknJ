package nl.it.fixx.moknj.bal.module.asset;

import java.util.List;
import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.module.ModuleLinkAccessBal;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.exception.BalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetLinkAccess extends ModuleLinkAccessBal<AssetLink> {

    @Autowired
    public AssetLinkAccess(MenuBal menuBal, AccessBal accessBal, AssetBal recordBal, UserBal userBal) {
        super(menuBal, accessBal, recordBal, userBal);
    }

    @Override
    public void setRecordViewValues(String recordId, AssetLink link) throws BalException {
    
    }

    /**
     * This not one of the the proudest pieces of code, but this code works,
     * this check the audit list which is passed for scope challenge and access
     * rights. Scope challenge basically adds records from the same template
     * used over different menu to the list and bypasses the access rights.
     *
     * @param records
     * @param menuId
     * @param templateId
     * @param user
     * @return
     * @throws nl.it.fixx.moknj.exception.BalException
     */
    @Override
    public List<AssetLink> filterRecordAccess(List<AssetLink> records, String menuId, String templateId, User user) throws BalException {
        return super.filterRecordAccess(records, menuId, templateId, user); //To change body of generated methods, choose Tools | Templates.
    }

}
