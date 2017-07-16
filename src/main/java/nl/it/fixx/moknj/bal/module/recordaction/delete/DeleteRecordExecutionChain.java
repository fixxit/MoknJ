package nl.it.fixx.moknj.bal.module.recordaction.delete;

import java.util.Iterator;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DeleteRecordExecutionChain {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteRecordExecutionChain.class);

    private DeleteAction action;

    @Autowired
    public DeleteRecordExecutionChain(ApplicationContext context) {
        Map<String, DeleteAction> types = context.getBeansOfType(DeleteAction.class);
        DeleteAction focus = null;
        Iterator<Map.Entry<String, DeleteAction>> deleteActions = types.entrySet().iterator();
        while (deleteActions.hasNext()) {
            Map.Entry<String, DeleteAction> pair = deleteActions.next();
            LOG.info("Discovered " + pair.getKey() + ", adding delete record chain...");
            if (focus != null && !focus.hasNext()) {
                LOG.info("Chaining " + pair.getKey() + " to " + focus.getClass().getSimpleName());
                focus.setNextIn(pair.getValue());
            }
            focus = pair.getValue();
            if (action == null) {
                action = pair.getValue();
            }
        }
        LOG.info("Delete Record Action [" + action.getClass().getSimpleName() + "] is the 1st in the chain");
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
