package nl.it.fixx.moknj.bal.module;

import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.exception.AccessException;
import org.aspectj.lang.JoinPoint;

public abstract class ModuleAccessBal<DOMAIN extends Record> {

    private final String ACCESS_ERROR = "This user does not have sufficient "
            + "access rights to %s this %s!";

    private final UserBal userBal;
    private final AccessBal accessBal;

    public ModuleAccessBal(UserBal userBal, AccessBal accessBal) {
        this.userBal = userBal;
        this.accessBal = accessBal;
    }

    public abstract void hasSaveAccess(JoinPoint joinPoint, String templateId, String menuId, DOMAIN record, String token) throws AccessException;

    public abstract void hasDeleteAccess(JoinPoint joinPoint, DOMAIN record, String menuId, String token, boolean cascade) throws AccessException;

    protected void checkAccess(String menuId, String templateId, GlobalAccessRights access, String token, String module) throws AccessException {
        if (!accessBal.hasAccess(userBal.getUserByToken(token), menuId, templateId, access)) {
            throw new AccessException(String.format(ACCESS_ERROR, access.getDisplayValue(), module));
        }
    }
}
