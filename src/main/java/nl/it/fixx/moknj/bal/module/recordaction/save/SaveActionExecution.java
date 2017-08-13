package nl.it.fixx.moknj.bal.module.recordaction.save;

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
public final class SaveActionExecution {

    private static final Logger LOG = LoggerFactory.getLogger(SaveActionExecution.class);

    private final SaveAction action;

    @Autowired
    public SaveActionExecution(@Qualifier("recordSaveActionChain") ModuleChainBal<SaveAction> saveActionChain) {
        action = saveActionChain.getChain();
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
