package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.core.user.User;

/**
 *
 * @author colin
 */
public interface UserRepositoryCustom {

    User findById(String id);

    User findByEmail(String email);

    User findByUserName(String username);

    List<User> findByFullname(String name, String surname);
}
