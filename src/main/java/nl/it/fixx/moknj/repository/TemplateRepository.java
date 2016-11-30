package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.domain.core.template.Template;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import nl.it.fixx.moknj.repository.custom.TemplateRepositoryCustom;

/**
 *
 * @author adriaan
 */
@Repository
public interface TemplateRepository extends MongoRepository<Template, String>, TemplateRepositoryCustom {

}
