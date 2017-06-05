/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.bal.record;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.it.fixx.moknj.bal.core.MenuBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.bal.core.access.AccessBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.link.Link;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.exception.BalException;

public abstract class RecordLinkAccess<T extends Link> {

    protected final MenuBal menuBal;
    protected final AccessBal accessBal;
    protected final RecordBal recordBal;
    protected final UserBal userBal;

    public RecordLinkAccess(MenuBal menuBal, AccessBal accessBal,
            RecordBal recordBal, UserBal userBal) {
        this.menuBal = menuBal;
        this.accessBal = accessBal;
        this.recordBal = recordBal;
        this.userBal = userBal;
    }

    public List<T> filterRecordAccess(List<T> records, String menuId, String templateId, User user) throws BalException {
        try {
            Set<T> links = new HashSet<>();
            for (T link : records) {
                if (recordBal.exists(link.getRecordId())) {
                    if (!user.getAuthorities().contains(ALL_ACCESS.toString())) {
                        // Get template from menu item
                        Template template = getMenuTemplate(menuId, templateId);
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
            throw new BalException("Error while trying to check user access to employee", e);
        }
    }

    public abstract void setRecordViewValues(String recordId, T link) throws BalException;

    /**
     * Gets Template for the menu and template id, this is to get the saved
     * settings to the template as menu template can differ to plain template as
     * it has scope settings included.
     *
     * @param menuId
     * @param templateId
     * @return
     * @throws Exception
     */
    private Template getMenuTemplate(String menuId, String templateId) throws Exception {
        Menu menu = menuBal.getMenuById(menuId);
        List<Template> menuTemplates = menu.getTemplates();
        for (Template menuTemplate : menuTemplates) {
            if (templateId.equals(menuTemplate.getId())) {
                return menuTemplate;
            }
        }
        return null;
    }

}
