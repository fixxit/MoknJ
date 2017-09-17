package nl.it.fixx.moknj.bal.module.recordaction.save;

import org.aspectj.lang.ProceedingJoinPoint;
import nl.it.fixx.moknj.bal.module.chainable.ModuleChainPointer;

public interface SaveAction<JOINDOMAIN, DOMAIN> extends ModuleChainPointer<SaveAction> {

    @Override
    void setNext(SaveAction saveAction);

    JOINDOMAIN before(DOMAIN domain);

    void after(JOINDOMAIN join, DOMAIN domain);

    boolean valid(Object domain);

    @Override
    boolean hasNext();

    void execute(ProceedingJoinPoint joinPoint, Save saveRecordAnotation) throws Throwable;
}
