package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author adriaan
 */
public class AssetLinkRepositoryImpl implements AssetLinkRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    @Override
    public List<AssetLink> getAllByResourceId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("resourceId").is(id));
        query.with(new Sort(Sort.Direction.DESC, "date"));
        return operations.find(query, AssetLink.class);
    }

    @Override
    public List<AssetLink> getAllByAssetId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("assetId").is(id));
        query.with(new Sort(Sort.Direction.DESC, "date"));
        return operations.find(query, AssetLink.class);
    }

}
