package nl.it.fixx.moknj.bal.core.user;

import java.util.List;
import nl.it.fixx.moknj.domain.core.user.User;

public interface UserCoreBal {

    List<User> getAll() throws Exception;

    List<User> getAll(String token) throws Exception;

    List<User> getAll(boolean isAdmin, String token) throws Exception;

    void delete(String userId, String token) throws Exception;

    User save(User payload, String token) throws Exception;

    User getUserById(String id);

    User getUserByToken(String token);

    String getFullName(User user);

    String getFullName(String userId) throws Exception;

}
