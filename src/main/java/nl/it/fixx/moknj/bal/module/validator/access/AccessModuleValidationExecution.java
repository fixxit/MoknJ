package nl.it.fixx.moknj.bal.module.validator.access;

import nl.it.fixx.moknj.bal.module.chainable.impl.AccessModuleValidationChain;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AccessModuleValidationExecution {

    private final AccessModule chain;

    @Autowired
    public AccessModuleValidationExecution(AccessModuleValidationChain chain) {
        this.chain = chain.getChain();
    }

    @Around("execution(@nl.it.fixx.moknj.bal.module.validator.access.AccessValidation * *(..)) && @annotation(accessValidationnAnotation)")
    public void accessValidation(ProceedingJoinPoint joinPoint, AccessValidation accessValidationnAnotation) throws Throwable {
        Object[] args = joinPoint.getArgs();
        chain.setType(accessValidationnAnotation);
        chain.validate(args);
        joinPoint.proceed();
    }

}
