
package nl.fixx.asset.data.repository.custom;

import nl.fixx.asset.data.domain.Resource;

/**
 *
 * @author colin
 */
public interface ResourceRepositoryCustom {
    Resource findById(String id);
    Resource findByEmail(String email);
}
