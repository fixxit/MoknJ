
package nl.fixx.asset.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nl.fixx.asset.data.domain.Resource;
import nl.fixx.asset.data.repository.custom.ResourceRepositoryCustom;

/**
 *
 * @author colin
 */

@Repository
public interface ResourceRepository extends MongoRepository<Resource, String>, ResourceRepositoryCustom {
    // add as required
}
