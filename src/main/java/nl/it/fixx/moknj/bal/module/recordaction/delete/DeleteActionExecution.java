package nl.it.fixx.moknj.bal.module.recordaction.delete;

import nl.it.fixx.moknj.bal.module.chainable.ModuleChainBal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DeleteActionExecution {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteActionExecution.class);

    private final DeleteAction action;

    @Autowired
    public DeleteActionExecution(@Qualifier("recordDeleteActionChain") ModuleChainBal<DeleteAction> deleteActionChain) {
        action = deleteActionChain.getChain();
    }

    @Around("execution(@nl.it.fixx.moknj.bal.module.recordaction.delete.Delete * *(..)) && @annotation(deleteAnnotation)")
    public void deleteAction(ProceedingJoinPoint joinPoint, Delete deleteAnnotation) throws Throwable {
        if (action != null) {
            action.execute(joinPoint, deleteAnnotation);
        } else {
            LOG.warn("No delete record chain initialized!");
        }
    }

}
