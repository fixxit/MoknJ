package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author adriaan
 */
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    public EmployeeRepositoryImpl() {
    }

    @Override
    public List<Employee> getAllByTypeId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("typeId").is(id));
        query.with(new Sort(Sort.Direction.DESC, "lastModifiedDate"));
        return operations.find(query, Employee.class);
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> assetList = operations.findAll(Employee.class);
        return assetList;
    }

}
