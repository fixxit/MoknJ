package nl.it.fixx.moknj.bal.module;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.module.validator.access.Access;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nl.it.fixx.moknj.bal.module.validator.access.AccessValidation;
import nl.it.fixx.moknj.repository.wrapper.ModuleWrapper;

public abstract class ModuleBaseBal<DOMAIN extends Record, REPO extends RecordRepository<DOMAIN>, WRAPPER extends ModuleWrapper<DOMAIN, REPO>>
        implements ModuleBal<DOMAIN> {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleBaseBal.class);

    protected final MenuBal menuBal;
    protected final UserBal userBal;
    protected final AccessBal accessBal;
    protected WRAPPER wrapper;

    public ModuleBaseBal(WRAPPER wrapper, MenuBal menuBal, UserBal userBal, AccessBal accessBal) {
        this.wrapper = wrapper;
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
                List<DOMAIN> records = wrapper.getAllByTypeId(templateId);
                // Gets custom template settings saved to menu.
                Menu menu = menuBal.getMenuById(menuId);
                menu.getTemplates().stream().filter((template)
                        -> (template.getId().equals(templateId))).forEach((Template template)
                        -> {
                    records.stream().forEach((DOMAIN record) -> {
                        // checks if scope check is required for this asset.
                        boolean inScope = template.isAllowScopeChallenge()
                                ? record.getMenuScopeIds().contains(menuId)
                                : true;
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
        return wrapper.getRepository().exists(id);
    }

    @Override
    public DOMAIN get(String id) {
        DOMAIN record = wrapper.getRepository().findOne(id);
        if (record != null) {
            return record;
        }
        throw new BalException("Could not find record for id [" + id + "]");
    }

    @Override
    @AccessValidation(access = Access.DELETE)
    public void delete(DOMAIN record, String menuId, String access_token, boolean cascade) {
        DOMAIN result = wrapper.getRepository().findOne(record.getId());
        if (result != null) {
            if (cascade) {
                // delete asset from the asset list.
                result.setCascade(cascade);
                result.setMenuId(menuId);
                result.setToken(access_token);
                wrapper.delete(result);
            } else {
                // hide asset by updating hidden field
                result.setHidden(true);
                wrapper.save(result);
                LOG.debug("This record[" + result.getId() + "] is now hidden as "
                        + "audit links was detected");
            }
        } else {
            throw new BalException("Could not remove record [" + record.getId() + "] not found in db");
        }
    }

}
