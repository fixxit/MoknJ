package nl.it.fixx.moknj.bal.module;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.bal.RepositoryBal;
import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.module.ModuleBal;
import nl.it.fixx.moknj.bal.module.asset.AssetBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ModuleBaseBal<DOMAIN extends Record, REPO extends RecordRepository> extends RepositoryBal<REPO> implements ModuleBal<DOMAIN> {

    private static final Logger LOG = LoggerFactory.getLogger(AssetBal.class);

    protected final MenuBal menuBal;
    protected final UserBal userBal;
    protected final AccessBal accessBal;

    public ModuleBaseBal(REPO repository, MenuBal menuBal, UserBal userBal, AccessBal accessBal) {
        super(repository);
        this.menuBal = menuBal;
        this.userBal = userBal;
        this.accessBal = accessBal;
    }

    @Override
    public List<DOMAIN> getAll(String templateId, String menuId, String token) throws BalException {
        try {
            List<DOMAIN> returnRecords = new ArrayList<>();
            User user = userBal.getUserByToken(token);
            if (accessBal.hasAccess(user, menuId, templateId, GlobalAccessRights.VIEW)) {
                List<DOMAIN> records = repository.getAllByTypeId(templateId);
                // Gets custom template settings saved to menu.
                Menu menu = menuBal.getMenuById(menuId);
                menu.getTemplates().stream().filter((template)
                        -> (template.getId().equals(templateId))).forEach((Template template)
                        -> {
                    records.stream().forEach((DOMAIN record) -> {
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
                                returnRecords.add(record);
                            }
                        }
                    });
                });
            }
            return returnRecords;
        } catch (Exception e) {
            throw new BalException("Could not find all records for template "
                    + "[" + templateId + "] and menu [" + menuId + "]", e);
        }
    }

    @Override
    public boolean exists(String id) {
        return this.repository.exists(id);
    }

    @Override
    public DOMAIN get(String id) {
        DOMAIN record = (DOMAIN) repository.findOne(id);
        if (record != null) {
            return record;
        }
        throw new BalException("Could not find record for id [" + id + "]");
    }

    @Override
    public void delete(DOMAIN record, String menuId, String access_token, boolean cascade) {
        DOMAIN result = (DOMAIN) repository.findOne(record.getId());
        if (result != null) {
            if (cascade) {
                // delete asset from the asset list.
                repository.delete(result);
            } else {
                // hide asset by updating hidden field=
                result.setHidden(true);
                repository.save(result);
                LOG.debug("This record[" + result.getId() + "] is now "
                        + "hidden as audit links was detected");
            }
        } else {
            throw new BalException("Could not remove record "
                    + "[" + record.getId() + "] not found in db");
        }
    }

}
