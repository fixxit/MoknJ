/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.bal.module;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.link.Link;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.exception.AccessException;
import nl.it.fixx.moknj.exception.BalException;

public abstract class ModuleLinkAccessBal<DOMAIN extends Link> {

    protected final MenuBal menuBal;
    protected final AccessBal accessBal;
    protected final ModuleBal recordBal;
    protected final UserBal userBal;

    public ModuleLinkAccessBal(MenuBal menuBal, AccessBal accessBal,
            ModuleBal recordBal, UserBal userBal) {
        this.menuBal = menuBal;
        this.accessBal = accessBal;
        this.recordBal = recordBal;
        this.userBal = userBal;
    }

    public List<DOMAIN> filterRecordAccess(List<DOMAIN> records, String menuId, String templateId, User user) throws BalException {
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

    public abstract void setRecordViewValues(String recordId, DOMAIN link) throws BalException;

}
