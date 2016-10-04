package nl.fixx.asset.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import nl.fixx.asset.data.domain.Asset;

public interface AssetRepository extends MongoRepository<Asset, String>{
    public Asset findByName(@Param("name") String name);
}
