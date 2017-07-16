package nl.it.fixx.moknj.bal.module.recordaction.save;

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
public final class SaveRecordExecutionChain {

    private static final Logger LOG = LoggerFactory.getLogger(SaveRecordExecutionChain.class);

    private SaveAction action;

    @Autowired
    public SaveRecordExecutionChain(ApplicationContext context) {
        Map<String, SaveAction> types = context.getBeansOfType(SaveAction.class);
        SaveAction focus = null;
        Iterator<Map.Entry<String, SaveAction>> saveActions = types.entrySet().iterator();
        while (saveActions.hasNext()) {
            Map.Entry<String, SaveAction> pair = saveActions.next();
            LOG.info("Discovered " + pair.getKey() + ", adding record save chain...");
            if (focus != null && !focus.hasNext()) {
                LOG.info("Chaining " + pair.getKey() + " to " + focus.getClass().getSimpleName());
                focus.setNextIn(pair.getValue());
            }
            focus = pair.getValue();
            if (action == null) {
                action = pair.getValue();
            }
        }
        LOG.info("Save Record Action [" + action.getClass().getSimpleName() + "] is the 1st in the chain");
    }

    @Around("execution(@nl.it.fixx.moknj.bal.module.recordaction.save.Save * *(..)) && @annotation(saveRecordAnotation)")
    public void deleteAction(ProceedingJoinPoint joinPoint, Save saveRecordAnotation) throws Throwable {
        if (action != null) {
            action.execute(joinPoint, saveRecordAnotation);
        } else {
            LOG.warn("No save record chain initialized!");
        }
    }

}
