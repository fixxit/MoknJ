/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.core.access.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author adriaan
 */
public class AccessRepositoryImpl implements AccessRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    public AccessRepositoryImpl() {
    }

    /**
     *
     * @param userId
     * @param menuId
     * @param templateId
     * @return
     * @throws Exception
     */
    @Override
    public boolean hasAccess(String userId, String menuId, String templateId) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId)
                .andOperator(Criteria.where("menuId").is(menuId),
                        Criteria.where("templateId").is(templateId))
        );

        Access access = operations.findOne(query, Access.class);
        return access != null
                && access.getId() != null
                && !access.getId().trim().isEmpty();
    }

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<Access> getAccessList(String userId) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return operations.findAll(Access.class);
    }

}
