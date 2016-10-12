package nl.fixx.asset.data.repository;

import java.math.BigDecimal;
import java.util.List;
import nl.fixx.asset.data.domain.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class AssetRepositoryImpl implements AssetRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    public AssetRepositoryImpl() {
    }

    @Override
    public List<Asset> findAllByPrice(BigDecimal price, Asset asset) {
        //example to to implemented
        return operations.find(query(where("").is("")), Asset.class);
    }

}
