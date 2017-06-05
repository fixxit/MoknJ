package nl.it.fixx.moknj.bal.record;

import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.exception.BalException;
import org.springframework.data.mongodb.repository.MongoRepository;

public abstract class RecordChangeBal<T extends MongoRepository, I extends Record> {

    protected final T repository;

    public RecordChangeBal(T repository) {
        this.repository = repository;
    }

    public abstract String hasChange(I record, String templateId, String menuId, String token) throws BalException;

}
