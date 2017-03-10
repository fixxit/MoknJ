package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.repository.custom.AssetLinkRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author adriaan
 */
@Repository
public interface AssetLinkRepository extends MongoRepository<AssetLink, String>, AssetLinkRepositoryCustom {

}
