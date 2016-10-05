package nl.fixx.asset.data.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import nl.fixx.asset.data.domain.Asset;

public interface AssetRepository extends MongoRepository<Asset, String>, AssetRepositoryCustom{
    public Asset findByName(@Param("name") String name);
    
    @Query("") //add query for a specific requirement 
    public List<String> someRandomQuery();
}
