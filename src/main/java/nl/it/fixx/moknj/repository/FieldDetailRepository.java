package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldDetailRepository extends MongoRepository<FieldDetail, String> {

}
