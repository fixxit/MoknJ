package nl.it.fixx.moknj.bal.core.user;

import java.util.List;
import nl.it.fixx.moknj.domain.core.user.User;

public interface UserCoreBal {

    List<User> getAll();

    List<User> getAll(String token);

    List<User> getAll(boolean isAdmin, String token);

    void delete(String userId, String token);

    User save(User payload, String token);

    User getUserById(String id);

    User getUserByToken(String token);

    String getFullName(User user);

    String getFullName(String userId);

}
