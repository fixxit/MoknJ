package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.core.access.Access;

/**
 *
 * @author adriaan
 */
public interface AccessRepositoryCustom {

    public boolean hasAccess(String userId, String menuId, String templateId) throws Exception;

    public Access getAccess(String userId, String menuId, String templateId) throws Exception;

    public List<Access> getAccessList(String userId) throws Exception;

}
