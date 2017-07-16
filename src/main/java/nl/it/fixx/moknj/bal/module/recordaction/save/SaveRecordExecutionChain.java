package nl.it.fixx.moknj.bal.module.recordaction.save;

import nl.it.fixx.moknj.bal.module.recordaction.RecordActionChain;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public final class SaveRecordExecutionChain {

    private static final Logger LOG = LoggerFactory.getLogger(SaveRecordExecutionChain.class);

    private final SaveAction action;

    @Autowired
    public SaveRecordExecutionChain(RecordActionChain<SaveAction> chain) {
        action = chain.getActionChain(SaveAction.class);
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
