/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author adriaan
 */
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    @Override
    public boolean existsByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        query.with(new Sort(Sort.Direction.DESC, "name"));
        List<Menu> results = operations.find(query, Menu.class);
        if (results != null) {
            return results.size() > 0;
        }
        return false;
    }

}
