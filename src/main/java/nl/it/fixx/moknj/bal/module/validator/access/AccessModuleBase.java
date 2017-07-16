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
    private Access access;

    @Override
    public void setType(Access access) {
        this.access = access;
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
    public void validate(Object[] args) {
        String module = getModule();
        if (canValidate(args)) {
            checkAccess(args, module);
        } else {
            if (nextAccessValidation != null) {
                nextAccessValidation.validate(args);
            }
        }
    }

    private void checkAccess(Object[] args, String module) throws AccessException {
        DOMAIN record;
        String menuId = null;
        String templateId = null;
        String token = null;

        GlobalAccessRights gar = null;
        if (Access.DELETE.equals(this.access)) {
            record = (DOMAIN) args[0];
            menuId = (String) args[1];
            templateId = record.getTypeId();
            token = (String) args[2];
            gar = GlobalAccessRights.DELETE;
        } else if (Access.SAVE.equals(this.access)) {
            templateId = (String) args[0];
            menuId = (String) args[1];
            record = (DOMAIN) args[2];
            token = (String) args[3];
            gar = (record.getId() != null) ? GlobalAccessRights.EDIT : GlobalAccessRights.NEW;
        }

        if (gar != null) {
            if (!accessBal.hasAccess(userBal.getUserByToken(token), menuId, templateId, gar)) {
                throw new AccessException(String.format(ACCESS_ERROR, gar.getDisplayValue(), module));
            }
        }
    }
}
