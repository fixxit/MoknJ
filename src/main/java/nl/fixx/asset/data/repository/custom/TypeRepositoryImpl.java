/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.repository.custom;

import java.util.List;
import nl.fixx.asset.data.domain.AssetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author adriaan
 */
public class TypeRepositoryImpl implements TypeRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    @Override
    public boolean existsByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        query.with(new Sort(Sort.Direction.DESC, "name"));
        List<AssetType> results = operations.find(query, AssetType.class);
        if (results != null) {
            return results.size() > 0;
        }
        return false;
    }
}
