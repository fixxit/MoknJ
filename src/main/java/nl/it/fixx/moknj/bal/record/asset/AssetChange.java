package nl.it.fixx.moknj.bal.record.asset;

import nl.it.fixx.moknj.bal.core.access.AccessBal;
import nl.it.fixx.moknj.bal.record.RecordChangeBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetChange extends RecordChangeBal<AssetRepository, Asset> {

    private final UserBal userBal;
    private final AccessBal accessBal;

    @Autowired
    public AssetChange(AssetRepository repository, UserBal userBal, AccessBal accessBal) {
        super(repository);
        this.userBal = userBal;
        this.accessBal = accessBal;
    }

    @Override
    public String hasChange(Asset record, String templateId, String menuId, String token) throws BalException {
        try {
            String flag = null;
            if (record.getId() != null) {
                // check if user has acess for edit record
                User user = userBal.getUserByToken(token);
                if (!accessBal.hasAccess(user, menuId, templateId,
                        GlobalAccessRights.EDIT)) {
                    throw new BalException("This user does not have sufficient "
                            + "access rights to update this asset!");
                }

                Asset asset = repository.findOne(record.getId());
                if (record.equals(asset)) {
                    flag = "no_changes";
                } else {
                    flag = "has_changes";
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
            return flag;
        } catch (Exception e) {
            throw new BalException("Error trying to find change", e);
        }
    }

}
