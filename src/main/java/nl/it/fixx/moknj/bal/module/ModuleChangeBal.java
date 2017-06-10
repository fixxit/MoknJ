package nl.it.fixx.moknj.bal.module;

import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.exception.BalException;
import org.springframework.data.mongodb.repository.MongoRepository;

public abstract class ModuleChangeBal<T extends MongoRepository, I extends Record> {
    
    public static String NO_CHANGES = "no_changes";
    public static String HAS_CHANGES = "has_changes";
    
    
    protected final T repository;

    public ModuleChangeBal(T repository) {
        this.repository = repository;
    }

    public abstract String hasChange(I record, String templateId, String menuId, String token) throws BalException;

}
