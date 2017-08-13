package nl.it.fixx.moknj.bal.module.asset;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.it.fixx.moknj.bal.BalBase;
import nl.it.fixx.moknj.bal.core.MainAccessBal;
import nl.it.fixx.moknj.bal.core.access.AccessCoreBal;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.bal.module.ModuleBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import static nl.it.fixx.moknj.domain.core.global.GlobalMenuType.GBL_MT_ASSET;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.AssetLinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nl.it.fixx.moknj.bal.module.ModuleLinkBal;
import nl.it.fixx.moknj.exception.AccessException;
import org.springframework.beans.factory.annotation.Qualifier;

@Service("assetLinkBal")
public class AssetLinkBal extends BalBase<AssetLinkRepository> implements ModuleLinkBal<AssetLink> {

    private static final Logger LOG = LoggerFactory.getLogger(AssetLinkBal.class);

    private final UserCoreBal userBal;
    private final ModuleBal<Asset> assetBal;
    private final AccessCoreBal accessBal;
    private final MainAccessBal mainAccessBal;
    private final AssetLinkAccess assetLinkAccess;

    @Autowired
    public AssetLinkBal(AssetLinkRepository assetLinkRepo, UserCoreBal userBal, @Qualifier("assetBal") ModuleBal<Asset> assetBal,
            AccessCoreBal accessBal, MainAccessBal mainAccessBal, AssetLinkAccess assetLinkAccess) {
        super(assetLinkRepo);
        this.userBal = userBal;
        this.assetBal = assetBal;
        this.accessBal = accessBal;
        this.mainAccessBal = mainAccessBal;
        this.assetLinkAccess = assetLinkAccess;
    }

    @Override
    public void delete(AssetLink link) {
        repository.delete(link);
    }

    @Override
    public void save(AssetLink link) {
        repository.save(link);
    }

    /**
     * Saves the link for a asset, this is used as audit in and out history
     *
     * @param menuId
     * @param templateId
     * @param payload
     * @param token
     * @return
     * @throws Exception
     */
    public AssetLink linkAssetToUser(String menuId, String templateId, AssetLink payload, String token) throws Exception {
        try {
            User user = userBal.getUserByToken(token);

            payload.setCreatedBy(user.getUserName());
            payload.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));

            // addAssetLink resource id to asset depending on isChecked
            if (payload.getAssetId() != null) {
                Asset asset = assetBal.get(payload.getAssetId());
                if (asset != null) {
                    // check if user has any access asigned to him.
                    LOG.info("user : " + user.getAuthorities());

                    if (!accessBal.hasAccess(user, menuId, templateId, GlobalAccessRights.EDIT)) {
                        throw new AccessException("Unable to check out/in this asset. "
                                + "This user does not have "
                                + "" + GlobalAccessRights.EDIT.toString()
                                + " rights");
                    }

                    if (payload.isChecked()) {
                        asset.setResourceId(payload.getResourceId());
                    } else {
                        asset.setResourceId(null);
                    }

                    asset.setLastModifiedBy(user.getUserName());
                    asset.setLastModifiedDate(
                            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                    assetBal.save(templateId, menuId, asset, token);
                }
            }
            return repository.save(payload);
        } catch (BalException e) {
            LOG.error("Error while trying to link user to asset", e);
            throw e;
        }
    }

    /**
     * Gets all asset in and out audit links for user token.
     *
     * @param token
     * @return
     * @throws nl.it.fixx.moknj.exception.BalException
     */
    @Override
    public List<AssetLink> getAllLinks(String token) throws BalException {
        try {
            Set<AssetLink> results = new HashSet<>();
            for (Menu menu : mainAccessBal.getUserMenus(token)) {
                if (GBL_MT_ASSET.equals(menu.getMenuType())) {
                    for (Template temp : menu.getTemplates()) {
                        List<Asset> assets = assetBal.getAll(temp.getId(), menu.getId(), token);
                        for (Asset asset : assets) {
                            results.addAll(assetLinkAccess.filterRecordAccess(
                                    getAllAssetLinksByAssetId(
                                            asset.getId(),
                                            menu.getId(),
                                            temp.getId(),
                                            token),
                                    menu.getId(),
                                    temp.getId(),
                                    userBal.getUserByToken(token)));
                        }
                    }
                }
            }
            return results.stream().collect(Collectors.toList());
        } catch (Exception e) {
            throw new BalException("Error while trying to find all asset links", e);
        }
    }

    /**
     * Gets all in out audit logs for asset id and token (access check).
     *
     * @param assetId
     * @param token
     * @return
     * @throws nl.it.fixx.moknj.exception.BalException
     */
    @Override
    public List<AssetLink> getAllLinksByRecordId(String assetId, String token) throws BalException {
        try {
            Set<AssetLink> results = new HashSet<>();
            mainAccessBal.getUserMenus(token).stream().filter((menu)
                    -> (menu.getMenuType().equals(GBL_MT_ASSET))).forEachOrdered((menu) -> {
                menu.getTemplates().forEach((temp) -> {
                    results.addAll(assetLinkAccess.filterRecordAccess(
                            repository.getAllByAssetId(assetId),
                            menu.getId(),
                            temp.getId(),
                            userBal.getUserByToken(token)));
                });
            });
            return results.stream().collect(Collectors.toList());
        } catch (BalException e) {
            throw new BalException("Error while trying to find all asset links", e);
        }
    }

    /**
     * Gets all in out audit logs for asset id and token (access check).
     * Enforces the menuId. For scope challenge.
     *
     * @param assetId
     * @param menuId
     * @param templateId
     * @param token
     * @return
     * @throws Exception
     */
    public List<AssetLink> getAllAssetLinksByAssetId(String assetId, String menuId, String templateId, String token) throws Exception {
        try {
            return assetLinkAccess.filterRecordAccess(
                    repository.getAllByAssetId(assetId),
                    menuId,
                    templateId,
                    userBal.getUserByToken(token));
        } catch (BalException e) {
            LOG.error("Error while trying to find all asset links", e);
            throw e;
        }
    }

    /**
     * Gets all audit logs for user id and token.
     *
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    public List<AssetLink> getAllAssetLinksByResourceId(String userId, String token) throws Exception {
        try {
            Set<AssetLink> results = new HashSet<>();
            mainAccessBal.getUserMenus(token).stream().filter((menu)
                    -> (menu.getMenuType().equals(GBL_MT_ASSET))).forEachOrdered((menu) -> {
                menu.getTemplates().forEach((temp) -> {
                    results.addAll(assetLinkAccess.filterRecordAccess(
                            repository.getAllByResourceId(userId),
                            menu.getId(),
                            temp.getId(),
                            userBal.getUserByToken(token)));
                });
            });
            return results.stream().collect(Collectors.toList());
        } catch (BalException e) {
            LOG.error("Error while trying to find all asset links", e);
            throw e;
        }
    }

}
