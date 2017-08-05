package nl.it.fixx.moknj.bal.module.validator.access;

public interface AccessModule {

    void setNextIn(AccessModuleBase delete);

    void validate(Object[] args);

    boolean canValidate(Object[] args);

    String getModule();

    void setType(AccessValidation access);

}
