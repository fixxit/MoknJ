package nl.it.fixx.moknj.bal;

import java.util.List;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.FieldDetailRepository;
import nl.it.fixx.moknj.service.SystemContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Field Detail Business Access Layer
 *
 * @author adriaan
 */
public class FieldBal extends RepositoryChain<FieldDetailRepository> {

    private static final Logger LOG = LoggerFactory.getLogger(FieldBal.class);

    public FieldBal(SystemContext context) {
        super(context.getRepository(FieldDetailRepository.class));
    }

    public FieldDetail getField(String id) throws Exception {
        try {
            if (id == null || id.isEmpty()) {
                LOG.debug("No Field id recieved to find Field");
                throw new BalException("No Field found, no Field id provided!");
            }

            FieldDetail field = repository.findOne(id);
            if (field == null) {
                LOG.debug("no Field found for id[" + id + "]");
                throw new BalException("No Field found by this id[" + id + "]");
            }
            return field;
        } catch (BalException e) {
            LOG.error("Error getting Field by id", e);
            throw e;
        }
    }

    public void saveFields(List<FieldDetail> fields) {
        try {
            fields.stream().forEach((detail) -> {
                repository.save(detail);
            });
        } catch (Exception e) {
            LOG.error("Error saving fields", e);
            throw e;
        }
    }

}
