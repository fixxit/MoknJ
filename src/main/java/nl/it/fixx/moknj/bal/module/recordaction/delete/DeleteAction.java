package nl.it.fixx.moknj.bal.module.recordaction.delete;

import org.aspectj.lang.ProceedingJoinPoint;
import nl.it.fixx.moknj.bal.module.chainable.ModuleChainPointer;

public interface DeleteAction<JOINDOMAIN, DOMAIN> extends ModuleChainPointer<DeleteAction> {

    @Override
    void setNext(DeleteAction deleteAction);

    JOINDOMAIN before(DOMAIN domain);

    void after(JOINDOMAIN join, DOMAIN domain);

    boolean valid(Object domain);

    @Override
    boolean hasNext();

    void execute(ProceedingJoinPoint joinPoint, Delete deleteAnnotation) throws Throwable;
}
