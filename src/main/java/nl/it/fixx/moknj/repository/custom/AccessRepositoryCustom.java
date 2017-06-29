package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.core.access.Access;

/**
 *
 * @author adriaan
 */
public interface AccessRepositoryCustom {

    boolean hasAccess(String userId, String menuId, String templateId);

    Access getAccess(String userId, String menuId, String templateId);

    List<Access> getAccessList(String userId);

}
