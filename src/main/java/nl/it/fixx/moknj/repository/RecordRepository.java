package nl.it.fixx.moknj.repository;

import java.util.List;
import nl.it.fixx.moknj.domain.core.record.Record;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecordRepository<DOMAIN extends Record> extends MongoRepository<DOMAIN, String> {

    List<DOMAIN> getAllByTypeId(String id);

}
