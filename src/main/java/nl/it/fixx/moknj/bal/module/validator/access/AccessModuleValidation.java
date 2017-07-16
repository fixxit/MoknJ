package nl.it.fixx.moknj.bal.module.validator.access;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AccessModuleValidation {

    private final AccessModule validation;

    @Autowired
    public AccessModuleValidation(AssetAccessModule assetValidation, EmployeeAccessModule employeeValidation) {
        this.validation = assetValidation;
        this.validation.setNextIn(employeeValidation);
    }

    @Around("execution(@nl.it.fixx.moknj.bal.module.validator.access.AccessValidation * *(..)) && @annotation(accessValidationnAnotation)")
    public void accessValidation(ProceedingJoinPoint joinPoint, AccessValidation accessValidationnAnotation) throws Throwable {
        Object[] args = joinPoint.getArgs();
        validation.setType(accessValidationnAnotation.access());
        validation.validate(args);
        joinPoint.proceed();
    }

}
