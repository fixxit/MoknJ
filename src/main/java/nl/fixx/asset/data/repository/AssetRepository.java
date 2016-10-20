package nl.fixx.asset.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.repository.custom.AssetRepositoryCustom;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String>, AssetRepositoryCustom {

}
