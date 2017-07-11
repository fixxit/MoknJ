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

        GlobalAccessRights access = null;
        if ("delete".equals(type)) {
            record = (DOMAIN) args[0];
            menuId = (String) args[1];
            templateId = record.getTypeId();
            token = (String) args[2];
            access = GlobalAccessRights.DELETE;
        } else if ("save".equals(type)) {
            templateId = (String) args[0];
            menuId = (String) args[1];
            record = (DOMAIN) args[2];
            token = (String) args[3];
            access = (record.getId() != null) ? GlobalAccessRights.EDIT : GlobalAccessRights.NEW;
        }

        if (access != null) {
            if (!accessBal.hasAccess(userBal.getUserByToken(token), menuId, templateId, access)) {
                throw new AccessException(String.format(ACCESS_ERROR, access.getDisplayValue(), module));
            }
        }
    }
}
