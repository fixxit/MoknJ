package nl.fixx.asset.data.repository.custom;

import java.util.List;
import nl.fixx.asset.data.domain.Resource;

/**
 *
 * @author colin
 */
public interface ResourceRepositoryCustom {

    public Resource findById(String id);

    public Resource findByEmail(String email);

    public Resource findByUserName(String username);

    public List<Resource> findByFullname(String name, String surname);
}
