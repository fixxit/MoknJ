package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author colin
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

}
