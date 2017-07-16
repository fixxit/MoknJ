package nl.it.fixx.moknj.bal.module.recordaction.delete;

import org.aspectj.lang.ProceedingJoinPoint;

public interface DeleteAction<JOINDOMAIN, DOMAIN> {

    void setNextIn(DeleteAction deleteAction);

    JOINDOMAIN before(DOMAIN domain);

    void after(JOINDOMAIN join, DOMAIN domain);

    boolean valid(Object domain);

    boolean hasNext();

    void execute(ProceedingJoinPoint joinPoint, Delete deleteAnnotation) throws Throwable;
}
