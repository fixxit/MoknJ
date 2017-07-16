package nl.it.fixx.moknj.bal.module.asset;

import java.text.SimpleDateFormat;
import java.util.Date;
import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.FieldBal;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nl.it.fixx.moknj.bal.module.ModuleBaseBal;
import nl.it.fixx.moknj.bal.module.validator.access.Access;
import nl.it.fixx.moknj.bal.module.validator.access.AccessValidation;
import nl.it.fixx.moknj.bal.module.validator.field.FieldValidation;
import nl.it.fixx.moknj.bal.module.validator.field.Module;

/**
 * Asset Business Access Layer
 *
 * @author adriaan
 */
@Service
public class AssetBal extends ModuleBaseBal<Asset, AssetRepository> {

    @Autowired
    public AssetBal(MenuBal menuBal, UserBal userBal, AccessBal accessBal,
            FieldBal fieldBal, AssetRepository assetRepo) {
        super(assetRepo, menuBal, userBal, accessBal);
    }

    /**
     * Saves the Asset
     *
     * @param templateId template id
     * @param menuId
     * @param record
     * @param token
     * @return the saved asset for id
     */
    @AccessValidation(access = Access.SAVE)
    @FieldValidation(module = Module.ASSET)
    @Override
    public Asset save(String templateId, String menuId, Asset record, String token) {
        if (templateId != null) {
            record.setTypeId(templateId);
            // Get user details who logged this asset using the token.
            User user = userBal.getUserByToken(token);
            if (user != null && user.isSystemUser()) {
                record.setLastModifiedBy(user.getUserName());
            } else {
                throw new BalException("Asset save error, could not find system"
                        + " user for this token");
            }
            // Save asset
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            record.setLastModifiedDate(date);
            if (record.getId() == null) {
                record.setCreatedBy(user.getUserName());
                record.setCreatedDate(date);
            } else {
                Asset dbAsset = repository.findOne(record.getId());
                record.setCreatedBy(dbAsset.getCreatedBy());
                record.setCreatedDate(dbAsset.getCreatedDate());
            }

            return repository.save(record);
        } else {
            throw new BalException("No asset type id provided.");
        }
    }

}
