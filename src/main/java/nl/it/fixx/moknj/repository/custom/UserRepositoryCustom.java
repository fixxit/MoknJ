package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.core.user.User;

/**
 *
 * @author colin
 */
public interface UserRepositoryCustom {

    public User findById(String id);

    public User findByEmail(String email);

    public User findByUserName(String username);

    public List<User> findByFullname(String name, String surname);
}
