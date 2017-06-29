package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;

/**
 *
 * @author adriaan
 */
public interface EmployeeLinkRepositoryCustom {

    List<EmployeeLink> getAllByEmployeeId(String id);

}
