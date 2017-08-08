package nl.it.fixx.moknj.bal.module.validator.field;

import nl.it.fixx.moknj.bal.module.chainable.ChainBal;

public interface FieldModule extends ChainBal<FieldModule> {

    @Override
    void setNextIn(FieldModule nextFieldValidation);

    void validate(String templateId, String menuId, Object record);

    Module getModule();

    void setModuleAllowed(Module module);
}
