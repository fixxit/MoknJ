package nl.fixx.asset.data.repository;

import nl.fixx.asset.data.domain.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String>, AssetRepositoryCustom {

}
