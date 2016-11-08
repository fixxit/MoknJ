
package nl.fixx.asset.data.repository.custom;

import java.util.List;
import nl.fixx.asset.data.domain.Resource;

/**
 *
 * @author colin
 */
public interface ResourceRepositoryCustom {
    Resource findById(String id);

    Resource findByEmail(String email);

    List<Resource> findByFullname(String name, String surname);
}
