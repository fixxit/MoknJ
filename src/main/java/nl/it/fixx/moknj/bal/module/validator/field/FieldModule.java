package nl.it.fixx.moknj.bal.module.validator.field;

public interface FieldModule {

    void setNextIn(FieldModuleBase nextFieldValidation);

    void validate(String templateId, String menuId, Object record);

    Module getModule();

    void setModuleAllowed(Module module);
}
