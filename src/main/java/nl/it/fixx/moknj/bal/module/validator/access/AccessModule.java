package nl.it.fixx.moknj.bal.module.validator.access;

public interface AccessModule {

    void setNextIn(AccessModuleBase delete);

    void validate(Object record, String menuId, String token, boolean cascade);

    boolean canValidate(Object record);

    String getModule();

    void setType(String moduleName);

}
