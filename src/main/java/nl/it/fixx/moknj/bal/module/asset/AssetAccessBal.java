package nl.it.fixx.moknj.bal.module.asset;

import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.module.ModuleAccessBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AssetAccessBal extends ModuleAccessBal<Asset> {

    @Autowired
    public AssetAccessBal(UserBal userBal, AccessBal accessBal) {
        super(userBal, accessBal);
    }

    @Override
    @Before("execution(* nl.it.fixx.moknj.bal.module.asset.AssetBal.save(String, String, nl.it.fixx.moknj.domain.modules.asset.Asset, String)) && args(templateId, menuId, record, token)")
    public void hasSaveAccess(JoinPoint joinPoint, String templateId, String menuId, Asset record, String token) {
        GlobalAccessRights access = (record.getId() != null)
                ? GlobalAccessRights.EDIT : GlobalAccessRights.NEW;
        checkAccess(menuId, templateId, access, token, "asset");
    }

    @Override
    @Before("execution(* nl.it.fixx.moknj.bal.module.asset.AssetBal.delete(nl.it.fixx.moknj.domain.modules.asset.Asset, String, String, boolean)) && args(record, menuId, token, cascade)")
    public void hasDeleteAccess(JoinPoint joinPoint, Asset record, String menuId, String token, boolean cascade) {
        checkAccess(menuId, record.getTypeId(), GlobalAccessRights.DELETE, token, "asset");
    }

}
