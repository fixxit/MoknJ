package nl.it.fixx.moknj.bal.module.validator.field;

import nl.it.fixx.moknj.bal.module.chainable.ModuleChainPointer;

public interface FieldModule extends ModuleChainPointer<FieldModule> {

    @Override
    void setNext(FieldModule nextFieldValidation);

    void validate(String templateId, String menuId, Object record);

    Module getModule();

    void setModuleAllowed(Module module);
}
