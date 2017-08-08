package nl.it.fixx.moknj.bal.module.validator.field;

import nl.it.fixx.moknj.bal.module.chainable.impl.FieldModuleValidationChain;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FieldModuleValidationExecution {

    private final FieldModule chain;

    @Autowired
    public FieldModuleValidationExecution(FieldModuleValidationChain chain) {
        this.chain = chain.getChain();
    }

    @Around("execution(@nl.it.fixx.moknj.bal.module.validator.field.FieldValidation * *(..)) && @annotation(fieldValidationAnotation)")
    public void accessValidation(ProceedingJoinPoint joinPoint, FieldValidation fieldValidationAnotation) throws Throwable {
        Object[] args = joinPoint.getArgs();

        String templateId = (String) args[0];
        String menuId = (String) args[1];
        Object record = args[2];

        chain.setModuleAllowed(fieldValidationAnotation.module());
        chain.validate(templateId, menuId, record);

        joinPoint.proceed();
    }

}
