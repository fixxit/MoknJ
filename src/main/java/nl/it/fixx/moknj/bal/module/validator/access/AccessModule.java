package nl.it.fixx.moknj.bal.module.validator.access;

import nl.it.fixx.moknj.bal.module.chainable.ModuleChainPointer;

public interface AccessModule extends ModuleChainPointer<AccessModule> {

    @Override
    void setNext(AccessModule delete);

    void validate(Object[] args);

    boolean canValidate(Object[] args);

    String getModule();

    void setType(AccessValidation access);

}
