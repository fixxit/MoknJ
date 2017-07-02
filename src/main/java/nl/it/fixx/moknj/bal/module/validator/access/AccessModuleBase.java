package nl.it.fixx.moknj.bal.module.validator.access;

import nl.it.fixx.moknj.bal.core.AccessBal;
import nl.it.fixx.moknj.bal.core.UserBal;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.exception.AccessException;

public abstract class AccessModuleBase<DOMAIN extends Record> implements AccessModule {

    private final String ACCESS_ERROR = "This user does not have sufficient "
            + "access rights to %s this %s!";

    private final UserBal userBal;
    private final AccessBal accessBal;
    private AccessModuleBase nextAccessValidation;
    private String type;

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public AccessModuleBase(UserBal userBal, AccessBal accessBal) {
        this.userBal = userBal;
        this.accessBal = accessBal;
    }

    @Override
    public void setNextIn(AccessModuleBase next) {
        this.nextAccessValidation = next;
    }

    @Override
    public void validate(Object obj, String menuId, String token, boolean cascade) {
        String module = getModule();
        if (canValidate(obj)) {
            DOMAIN record = (DOMAIN) obj;
            checkAccess(record, menuId, token, module);
        } else {
            if (nextAccessValidation != null) {
                nextAccessValidation.validate(obj, menuId, token, cascade);
            }
        }
    }

    private void checkAccess(DOMAIN record, String menuId, String token, String module) throws AccessException {
        GlobalAccessRights access = null;
        if ("delete".equals(type)) {
            access = GlobalAccessRights.DELETE;
        } else if ("save".equals(type)) {
            access = (record.getId() != null) ? GlobalAccessRights.EDIT : GlobalAccessRights.NEW;
        }

        if (access != null) {
            if (!accessBal.hasAccess(userBal.getUserByToken(token), menuId, record.getTypeId(), access)) {
                throw new AccessException(String.format(ACCESS_ERROR, access.getDisplayValue(), module));
            }
        }
    }
}
