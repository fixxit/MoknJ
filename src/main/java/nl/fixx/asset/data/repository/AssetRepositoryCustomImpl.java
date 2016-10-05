package nl.fixx.asset.data.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.QuerydslRepositorySupport;

import nl.fixx.asset.data.domain.Asset;

public class AssetRepositoryCustomImpl extends QuerydslRepositorySupport implements AssetRepositoryCustom{
    
    private MongoOperations operations;
    
    public AssetRepositoryCustomImpl(MongoOperations operations) {
	super(operations);
	this.operations = operations;
    }

    @Override
    public List<Asset> findAllExpensiveAssets(BigDecimal price, Asset asset) {
	//example to to implemented
	return null;
    }

}
