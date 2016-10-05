package nl.fixx.asset.data.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import nl.fixx.asset.data.domain.Asset;

public class AssetRepositoryCustomImpl implements AssetRepositoryCustom {
    
    @Autowired 
    private MongoOperations operations;

    public AssetRepositoryCustomImpl() {
    }

    @Override
    public List<Asset> findAllByPrice(BigDecimal price, Asset asset) {
	//example to to implemented
	return operations.find(query(where("").is("")), Asset.class);
    }

}
