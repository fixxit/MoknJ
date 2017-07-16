package nl.it.fixx.moknj.bal.module.recordaction.delete;

import nl.it.fixx.moknj.bal.module.recordaction.Action;
import org.aspectj.lang.ProceedingJoinPoint;

public interface DeleteAction<JOINDOMAIN, DOMAIN> extends Action<DeleteAction> {

    @Override
    void setNextIn(DeleteAction deleteAction);

    JOINDOMAIN before(DOMAIN domain);

    void after(JOINDOMAIN join, DOMAIN domain);

    boolean valid(Object domain);

    @Override
    boolean hasNext();

    void execute(ProceedingJoinPoint joinPoint, Delete deleteAnnotation) throws Throwable;
}
