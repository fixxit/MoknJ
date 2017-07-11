package nl.it.fixx.moknj.bal.module.recordaction.save;

import org.aspectj.lang.ProceedingJoinPoint;

public interface SaveAction<JOINDOMAIN, DOMAIN> {

    void setNextIn(SaveActionBase saveAction);

    JOINDOMAIN before(DOMAIN domain);

    void after(JOINDOMAIN join, DOMAIN domain);

    boolean valid(Object domain);

    void execute(ProceedingJoinPoint joinPoint, Save saveRecordAnotation) throws Throwable;
}
