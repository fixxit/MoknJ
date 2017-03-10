package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.core.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author adriaan
 */
public class TemplateRepositoryImpl implements TemplateRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    @Override
    public boolean existsByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        query.with(new Sort(Sort.Direction.DESC, "name"));
        List<Template> results = operations.find(query, Template.class);
        if (results != null) {
            return results.size() > 0;
        }
        return false;
    }
}
