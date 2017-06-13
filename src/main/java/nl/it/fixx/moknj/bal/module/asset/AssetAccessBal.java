package nl.it.fixx.moknj.bal.module.asset;

import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.module.ModuleAccessBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.AssetRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AssetAccessBal extends ModuleAccessBal<Asset> {

    private final UserBal userBal;
    private final AccessBal accessBal;

    @Autowired
    public AssetAccessBal(AssetRepository repository, UserBal userBal, AccessBal accessBal) {
        this.userBal = userBal;
        this.accessBal = accessBal;
    }

    @Override
    @Before("execution(* nl.it.fixx.moknj.bal.module.asset.AssetBal.save(String, String, nl.it.fixx.moknj.domain.modules.asset.Asset, String)) && args(templateId, menuId, record, token)")
    public void hasSaveAccess(JoinPoint joinPoint, String templateId, String menuId, Asset record, String token) throws BalException {
        try {
            if (record.getId() != null) {
                // check if user has acess for edit record
                User user = userBal.getUserByToken(token);
                if (!accessBal.hasAccess(user, menuId, templateId,
                        GlobalAccessRights.EDIT)) {
                    throw new BalException("This user does not have sufficient "
                            + "access rights to update this asset!");
                }
            } else {
                // check if user has acess for new record
                User user = userBal.getUserByToken(token);
                if (!accessBal.hasAccess(user, menuId, templateId,
                        GlobalAccessRights.NEW)) {
                    throw new BalException("This user does not have sufficient "
                            + "access rights to save this asset!");
                }
            }
        } catch (Exception e) {
            throw new BalException(e);
        }
    }

}
