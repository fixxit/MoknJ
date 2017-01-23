package nl.it.fixx.moknj.bal;

import java.util.List;
import nl.it.fixx.moknj.domain.core.access.Access;

/**
 * Main access BAL contract, if by any chance you implement access ball over
 * object it must have the following rules methods.
 *
 * @author adriaan
 */
public interface AccessBal {

    public void addAccess(Access access) throws Exception;

    public void addAccess(String userId, List<Access> accessList) throws Exception;

    public void deleteAccess(String id) throws Exception;

    public void updateAccess(Access access) throws Exception;

    public void hasAccess(String userId, String menuId, String templateId) throws Exception;

    public List<Access> getAccessList(String userId) throws Exception;

}
