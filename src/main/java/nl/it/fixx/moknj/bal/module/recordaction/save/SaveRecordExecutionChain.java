package nl.it.fixx.moknj.bal.module.recordaction.save;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SaveRecordExecutionChain {

    private final SaveAction employeeRecordAction;

    @Autowired
    public SaveRecordExecutionChain(EmployeeSaveAction saveAction) {
        this.employeeRecordAction = saveAction;
    }

    @Around("execution(@nl.it.fixx.moknj.bal.module.recordaction.save.SaveRecord * *(..)) && @annotation(saveRecordAnotation)")
    public void accessValidation(ProceedingJoinPoint joinPoint, SaveRecord saveRecordAnotation) throws Throwable {
        employeeRecordAction.execute(joinPoint, saveRecordAnotation);
    }

}
