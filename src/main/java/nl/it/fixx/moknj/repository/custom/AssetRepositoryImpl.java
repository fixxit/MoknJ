package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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

    public List<Asset> getAllByTypeId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("typeId").is(id));
        query.with(new Sort(Sort.Direction.DESC, "lastModifiedDate"));
        return operations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getAllByResourceId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("reourceId").is(id));
        query.with(new Sort(Sort.Direction.DESC, "lastModifiedDate"));
        return operations.find(query, Asset.class);
    }

}
