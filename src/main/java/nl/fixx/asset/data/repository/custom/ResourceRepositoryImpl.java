
package nl.fixx.asset.data.repository.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 *
 * @author colin
 */
public class ResourceRepositoryImpl implements ResourceRepositoryCustom {
    @Autowired
    private MongoOperations operations;
}
