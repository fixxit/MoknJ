package nl.it.fixx.moknj.bal.module.validator.access;

import nl.it.fixx.moknj.bal.module.chainable.ChainBal;

public interface AccessModule extends ChainBal<AccessModule> {

    @Override
    void setNextIn(AccessModule delete);

    void validate(Object[] args);

    boolean canValidate(Object[] args);

    String getModule();

    void setType(AccessValidation access);

}
