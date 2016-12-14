package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author adriaan
 */
public class EmployeeLinkRepositoryImpl implements EmployeeLinkRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    public EmployeeLinkRepositoryImpl() {
    }

    @Override
    public List<EmployeeLink> getAllByEmployeeId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("employeeId").is(id));
        query.with(new Sort(Sort.Direction.DESC, "createdDate"));
        return operations.find(query, EmployeeLink.class);
    }

}
