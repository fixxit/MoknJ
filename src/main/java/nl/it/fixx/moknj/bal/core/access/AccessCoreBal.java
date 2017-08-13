package nl.it.fixx.moknj.bal.core.access;

import java.util.List;
import nl.it.fixx.moknj.domain.core.access.Access;
import nl.it.fixx.moknj.domain.core.global.GlobalAccessRights;
import nl.it.fixx.moknj.domain.core.user.User;

public interface AccessCoreBal {

    void addAccess(String userId, List<Access> newList, String token) throws Exception;

    void addAccess(Access access, String token) throws Exception;

    void deleteAccess(String id, String token) throws Exception;

    void updateAccess(Access access, String token);

    void checkAccess(String userId, String menuId, String templateId);

    boolean hasAccess(User user, String menuId, String templateId) throws Exception;

    boolean hasAccess(User user, String menuId, String templateId, GlobalAccessRights right);

    List<Access> getAccessList(String userId) throws Exception;

}
