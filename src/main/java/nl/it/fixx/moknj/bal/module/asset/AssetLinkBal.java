package nl.it.fixx.moknj.bal.module.asset;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.it.fixx.moknj.bal.RepositoryBal;
import nl.it.fixx.moknj.bal.RepositoryContext;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.MainAccessBal;
import nl.it.fixx.moknj.bal.module.ModuleLinkable;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import static nl.it.fixx.moknj.domain.core.global.GlobalMenuType.GBL_MT_ASSET;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.AssetLinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetLinkBal extends RepositoryBal<AssetLinkRepository> implements ModuleLinkable<AssetLink> {

    private static final Logger LOG = LoggerFactory.getLogger(AssetLinkBal.class);

    private final UserBal userBal;
    private final AssetBal assetBal;
    private final AccessBal accessBal;
    private final MainAccessBal mainAccessBal;
    private final MenuBal menuBal;

    @Autowired
    public AssetLinkBal(RepositoryContext context, UserBal userBal, AssetBal assetBal,
            AccessBal accessBal, MainAccessBal mainAccessBal, MenuBal menuBal) {
        super(context.getRepository(AssetLinkRepository.class));
        this.userBal = userBal;
        this.assetBal = assetBal;
        this.accessBal = accessBal;
        this.mainAccessBal = mainAccessBal;
        this.menuBal = menuBal;
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
                        throw new BalException("Unable to check out/in this asset. "
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
        } catch (Exception e) {
            LOG.error("Error while trying to link user to asset", e);
            throw e;
        }
    }

    /**
     * This not one of the the proudest pieces of code, but this code works,
     * this check the audit list which is passed for scope challenge and access
     * rights. Scope challenge basically adds records from the same template
     * used over different menu to the list and bypasses the access rights.
     *
     * @param assetLinks
     * @param menuId
     * @param templateId
     * @param user
     * @return
     * @throws Exception
     */
    public List<AssetLink> checkAssetRecordAccess(List<AssetLink> assetLinks, String menuId, String templateId, User user) throws Exception {
        try {
            Set<AssetLink> links = new HashSet<>();
            for (AssetLink link : assetLinks) {
                if (assetBal.exists(link.getAssetId())) {
                    if (!user.getAuthorities().contains(ALL_ACCESS.toString())) {
                        // Get template from menu item
                        Template template = menuBal.getMenuTemplate(menuId, templateId);
                        if (template == null) {
                            continue;
                        }
                        if (!template.isAllowScopeChallenge()) {
                            if (accessBal.hasAccess(
                                    user,
                                    menuId,
                                    template.getId(),
                                    GlobalAccessRights.VIEW)) {
                                links.add(link);
                            }
                        } else {
                            links.add(link);
                        }
                    } else {
                        links.add(link);
                    }
                }
            }
            return links.stream().collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Error while trying to check user access to employee", e);
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
                            results.addAll(checkAssetRecordAccess(
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
            for (Menu menu : mainAccessBal.getUserMenus(token)) {
                if (menu.getMenuType().equals(GBL_MT_ASSET)) {
                    for (Template temp : menu.getTemplates()) {
                        results.addAll(checkAssetRecordAccess(
                                repository.getAllByAssetId(assetId),
                                menu.getId(),
                                temp.getId(),
                                userBal.getUserByToken(token)));
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
            return checkAssetRecordAccess(
                    repository.getAllByAssetId(assetId),
                    menuId,
                    templateId,
                    userBal.getUserByToken(token));
        } catch (Exception e) {
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
            for (Menu menu : mainAccessBal.getUserMenus(token)) {
                if (menu.getMenuType().equals(GBL_MT_ASSET)) {
                    for (Template temp : menu.getTemplates()) {
                        results.addAll(checkAssetRecordAccess(
                                repository.getAllByResourceId(userId),
                                menu.getId(),
                                temp.getId(),
                                userBal.getUserByToken(token)));
                    }
                }
            }
            return results.stream().collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Error while trying to find all asset links", e);
            throw e;
        }
    }

}
