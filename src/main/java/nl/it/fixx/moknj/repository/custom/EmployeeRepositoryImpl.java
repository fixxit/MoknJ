package nl.it.fixx.moknj.repository.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 *
 * @author adriaan
 */
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    @Autowired
    private MongoOperations operations;

    public EmployeeRepositoryImpl() {
    }

}
