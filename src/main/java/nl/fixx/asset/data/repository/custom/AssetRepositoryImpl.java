package nl.fixx.asset.data.repository.custom;

import java.util.List;
import nl.fixx.asset.data.domain.Asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

public class AssetRepositoryImpl implements AssetRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    public AssetRepositoryImpl() {
    }

    @Override
    public List<Asset> findAll() {
	List<Asset> assetList = operations.findAll(Asset.class);
	return assetList;
    }

}
