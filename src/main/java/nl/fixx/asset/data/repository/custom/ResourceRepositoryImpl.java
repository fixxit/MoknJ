package nl.fixx.asset.data.repository.custom;

import java.util.List;
import nl.fixx.asset.data.domain.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author colin
 */
public class ResourceRepositoryImpl implements ResourceRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    @Override
    public Resource findById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return operations.findOne(query, Resource.class);
    }

    @Override
    public Resource findByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return operations.findOne(query, Resource.class);
    }

    @Override
    public List<Resource> findByFullname(String firstName, String surname) {
        Query query = new Query();
        query.addCriteria(Criteria.where("firstName").is(firstName)
                .andOperator(Criteria.where("surname").is(surname)));
        query.with(new Sort(Sort.Direction.DESC, "id"));
        return operations.find(query, Resource.class);
    }

    @Override
    public Resource findByUserName(String username) {
        Query query = new Query(Criteria.where("userName").is(username));
        Resource resource = operations.findOne(query, Resource.class);
        return resource;
    }
}
