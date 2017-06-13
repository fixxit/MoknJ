package nl.it.fixx.moknj.bal.module;

import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.exception.BalException;
import org.aspectj.lang.JoinPoint;

public abstract class ModuleAccessBal<I extends Record> {

    public abstract void hasSaveAccess(JoinPoint joinPoint, String templateId, String menuId, I record, String token) throws BalException;

}
