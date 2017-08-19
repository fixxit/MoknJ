package nl.it.fixx.moknj.bal.module.linkaccess;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.it.fixx.moknj.bal.core.access.AccessCoreBal;
import nl.it.fixx.moknj.bal.core.menu.MenuCoreBal;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.bal.module.ModuleBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.link.Link;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.exception.AccessException;
import nl.it.fixx.moknj.exception.BalException;

public abstract class ModuleLinkAccessBal<DOMAIN extends Link, MODBAL extends ModuleBal> {

    protected final MODBAL recordBal;
    protected final MenuCoreBal menuBal;
    protected final AccessCoreBal accessBal;
    protected final UserCoreBal userBal;

    public ModuleLinkAccessBal(MenuCoreBal menuBal, AccessCoreBal accessBal,
            MODBAL recordBal, UserCoreBal userBal) {
        this.menuBal = menuBal;
        this.accessBal = accessBal;
        this.recordBal = recordBal;
        this.userBal = userBal;
    }

    public final List<DOMAIN> filterRecordAccess(List<DOMAIN> records, String menuId, String templateId, User user) throws BalException {
        try {
            Set<DOMAIN> links = new HashSet<>();
            for (DOMAIN link : records) {
                if (recordBal.exists(link.getRecordId())) {
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
                                    templateId,
                                    GlobalAccessRights.VIEW)) {
                                setRecordViewValues(link.getRecordId(), link);
                                links.add(link);
                            }
                        } else {
                            setRecordViewValues(link.getRecordId(), link);
                            links.add(link);
                        }
                    } else {
                        setRecordViewValues(link.getRecordId(), link);
                        links.add(link);
                    }
                }
            }
            return links.stream().collect(Collectors.toList());
        } catch (Exception e) {
            throw new AccessException("Error while trying to check user access to employee", e);
        }
    }

    public void setRecordViewValues(String recordId, DOMAIN link) {
        //DO NOTHING IF NOT OVERRIDDEN
    }

}
