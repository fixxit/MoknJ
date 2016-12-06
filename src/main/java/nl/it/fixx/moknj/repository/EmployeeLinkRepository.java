package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.repository.custom.EmployeeLinkRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author adriaan
 */
@Repository
public interface EmployeeLinkRepository extends MongoRepository<EmployeeLink, String>,
        EmployeeLinkRepositoryCustom {

}
