package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.core.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author colin
 */
public class UserRepositoryImpl implements UserRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    @Override
    public User findById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return operations.findOne(query, User.class);
    }

    @Override
    public User findByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return operations.findOne(query, User.class);
    }

    @Override
    public List<User> findByFullname(String firstName, String surname) {
        Query query = new Query();
        query.addCriteria(Criteria.where("firstName").is(firstName)
                .andOperator(Criteria.where("surname").is(surname)));
        query.with(new Sort(Sort.Direction.DESC, "id"));
        return operations.find(query, User.class);
    }

    @Override
    public User findByUserName(String username) {
        Query query = new Query(Criteria.where("userName").is(username));
        User resource = operations.findOne(query, User.class);
        return resource;
    }
}
