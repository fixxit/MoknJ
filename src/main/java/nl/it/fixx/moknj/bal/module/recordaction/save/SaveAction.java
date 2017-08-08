package nl.it.fixx.moknj.bal.module.recordaction.save;

import nl.it.fixx.moknj.bal.module.chainable.ChainBal;
import org.aspectj.lang.ProceedingJoinPoint;

public interface SaveAction<JOINDOMAIN, DOMAIN> extends ChainBal<SaveAction> {

    @Override
    void setNextIn(SaveAction saveAction);

    JOINDOMAIN before(DOMAIN domain);

    void after(JOINDOMAIN join, DOMAIN domain);

    boolean valid(Object domain);

    @Override
    boolean hasNext();

    void execute(ProceedingJoinPoint joinPoint, Save saveRecordAnotation) throws Throwable;
}
