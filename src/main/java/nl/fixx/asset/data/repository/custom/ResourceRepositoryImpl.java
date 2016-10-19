
package nl.fixx.asset.data.repository.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import nl.fixx.asset.data.domain.Resource;

/**
 *
 * @author colin
 */
public class ResourceRepositoryImpl implements ResourceRepositoryCustom {
    @Autowired
    private MongoOperations operations;

    @Override
    public Resource findById(String id) {
	Query query = new Query(new Criteria().where("id").is(id));
	return operations.findOne(query, Resource.class);
    }
}
