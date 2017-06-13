package nl.it.fixx.moknj.bal.module.asset;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.FieldBal;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.RepositoryBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.RepositoryContext;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.AssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nl.it.fixx.moknj.bal.module.ModuleBal;

/**
 * Asset Business Access Layer
 *
 * @author adriaan
 */
@Service
public class AssetBal extends RepositoryBal<AssetRepository> implements ModuleBal<Asset> {

    private static final Logger LOG = LoggerFactory.getLogger(AssetBal.class);

    private final MenuBal menuBal;
    private final UserBal userBal;
    private final AccessBal accessBal;
    private final FieldBal fieldBal;

    @Autowired
    public AssetBal(MenuBal menuBal, UserBal userBal, AccessBal accessBal,
            FieldBal fieldBal, RepositoryContext context) {
        super(context.getRepository(AssetRepository.class));
        this.menuBal = menuBal;
        this.userBal = userBal;
        this.accessBal = accessBal;
        this.fieldBal = fieldBal;
    }

    /**
     * Saves the Asset
     *
     * @param templateId template id
     * @param token
     * @return the saved asset for id
     */
    @Override
    public Asset save(String templateId, String menuId, Asset record, String token) throws BalException {
        try {
            if (templateId != null) {
                record.setTypeId(templateId);
                // checks if the original object differs from new saved object
                // stop the unique filter form check for duplicates on asset
                // which has not changed from the db version...
                String flag = (record.equals(repository.findOne(record.getId())))
                        ? "no_changes" : "has_changes";
                /**
                 * Find unique fields for asset and check if the current list of
                 * assets is unique for the field...
                 */
                Map<String, String> uniqueFields = new HashMap<>();
                Map<String, Boolean> unifieldIndicator = new HashMap<>();
                Map<String, List<String>> uniqueValues = new HashMap<>();

                // Get all the unique field ids
                List<FieldValue> assetFields = record.getDetails();
                assetFields.stream().forEach((field) -> {
                    try {
                        FieldDetail detail = fieldBal.get(field.getId());
                        if (detail != null && detail.isUnique()) {
                            uniqueFields.put(field.getId(), detail.getName());
                        }
                    } catch (Exception ex) {
                        LOG.error("Error getting field detail", ex);
                    }
                });

                // Create a list of all the values for the unique assets
                for (Asset asset : repository.getAllByTypeId(templateId)) {
                    // if statement below checks that if update asset does not check
                    // it self to flag for duplication
                    if (!asset.getId().equals(record.getId())
                            && !"no_changes".equals(flag)) {
                        List<FieldValue> details = asset.getDetails();
                        details.stream().filter((field) -> (uniqueFields.keySet().contains(field.getId()))).map((field) -> {
                            if (uniqueValues.get(field.getId()) == null) {
                                uniqueValues.put(field.getId(), new ArrayList<>());
                            }
                            return field;
                        }).filter((field) -> (!uniqueValues.get(field.getId()).contains(field.getValue()))).forEach((field) -> {
                            uniqueValues.get(field.getId()).add(field.getValue());
                        });
                    }
                }

                // check if fields to be saved for asset has duplicates
                if (!uniqueValues.isEmpty()) {
                    assetFields.stream().filter((field) -> (uniqueValues.containsKey(field.getId()))).filter((field) -> (uniqueValues.get(field.getId()).contains(field.getValue()))).forEach((field) -> {
                        unifieldIndicator.put(field.getId(), true);
                    });
                }

                // generate duplication message
                if (!unifieldIndicator.isEmpty()) {
                    String message = unifieldIndicator.size() > 1
                            ? "Non unique values for fields ["
                            : "Non unique value for field ";
                    message = unifieldIndicator.keySet().stream().map((typeId) -> uniqueFields.get(typeId)).map((fieldName) -> fieldName + ",").reduce(message, String::concat);
                    if (message.endsWith(",")) {
                        message = message.substring(0, message.length() - 1);
                    }

                    message += unifieldIndicator.size() > 1
                            ? "]. Please check values"
                            : ". Please input new value";
                    throw new BalException(message);
                }

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
        } catch (Exception e) {
            throw new BalException("Could not save asset due to internal error", e);
        }
    }

    /**
     * Gets a asset by template id and menu id
     *
     * @param templateId
     * @param menuId
     * @param token
     * @return
     */
    @Override
    public List<Asset> getAll(String templateId, String menuId, String token) throws BalException {
        try {
            List<Asset> assets = new ArrayList<>();
            User user = userBal.getUserByToken(token);
            if (accessBal.hasAccess(user, menuId, templateId, GlobalAccessRights.VIEW)) {
                List<Asset> records = repository.getAllByTypeId(templateId);
                // Gets custom template settings saved to menu.
                Menu menu = menuBal.getMenuById(menuId);
                menu.getTemplates().stream().filter((template)
                        -> (template.getId().equals(templateId))).forEach((Template template)
                        -> {
                    records.stream().forEach((Asset record) -> {
                        // checks if scope check is required for this asset.
                        boolean inScope;
                        if (template.isAllowScopeChallenge()) {
                            inScope = record.getMenuScopeIds().contains(menuId);
                        } else {
                            inScope = true;
                        }
                        // if asset is inscope allow adding of asset.
                        if (inScope) {
                            // checks if the asset is hidden.
                            if (!record.isHidden()) {
                                assets.add(record);
                            }
                        }
                    });
                });
            }
            return assets;
        } catch (Exception e) {
            throw new BalException("Could not find all assets for template "
                    + "[" + templateId + "] and menu [" + menuId + "]", e);
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Asset get(String id) throws Exception {
        try {
            Asset asset = repository.findOne(id);
            if (asset != null) {
                return asset;
            }
            throw new BalException("Could not find asset for id [" + id + "]");
        } catch (BalException e) {
            LOG.error("Error on trying to retieve asset for id [" + id + "]", e);
            throw e;
        }
    }

    /**
     *
     * @param record
     * @param menuId
     * @param token
     * @param cascade
     */
    @Override
    public void delete(Asset record, String menuId, String token, boolean cascade) throws BalException {
        try {
            Asset result = repository.findOne(record.getId());
            if (result != null) {
                String templateId = result.getTypeId();
                User user = userBal.getUserByToken(token);
                if (!accessBal.hasAccess(user, menuId, templateId,
                        GlobalAccessRights.DELETE)) {
                    throw new BalException("This user does not have sufficient "
                            + "access rights to delete this asset!");
                }

                if (cascade) {
                    // delete asset from the asset list.
                    repository.delete(result);
                } else {
                    // hide asset by updating hidden field=
                    result.setHidden(true);
                    LOG.debug("This asset[" + result.getId() + "] is now "
                            + "hidden as audit links was detected");
                    repository.save(result);
                }

            } else {
                throw new BalException("Could not remove asset "
                        + "[" + record.getId() + "] not found in db");
            }

        } catch (Exception e) {
            throw new BalException("Error on trying to to delete asset [" + record + "]", e);
        }
    }

    @Override
    public boolean exists(String id) {
        return this.repository.exists(id);
    }

}
