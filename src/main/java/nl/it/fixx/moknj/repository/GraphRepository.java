package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.repository.custom.GraphRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author adriaan
 */
@Repository
public interface GraphRepository extends MongoRepository<Graph, String>, GraphRepositoryCustom {

}
