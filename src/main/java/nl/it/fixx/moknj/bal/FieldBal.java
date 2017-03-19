package nl.it.fixx.moknj.bal;

import java.util.List;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.repository.FieldDetailRepository;
import nl.it.fixx.moknj.service.SystemContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Field Detail Business Access Layer
 *
 * @author adriaan
 */
public class FieldBal implements BusinessAccessLayer {

    private static final Logger LOG = LoggerFactory.getLogger(FieldBal.class);
    private final FieldDetailRepository fieldRep;

    public FieldBal(SystemContext context) {
        this.fieldRep = context.getRepository(FieldDetailRepository.class);
    }

    public FieldDetail getField(String id) throws Exception {
        try {
            if (id == null || id.isEmpty()) {
                LOG.debug("No Field id recieved to find Field");
                throw new Exception("No Field found, no Field id provided!");
            }

            FieldDetail field = fieldRep.findOne(id);
            if (field == null) {
                LOG.debug("no Field found for id[" + id + "]");
                throw new Exception("No Field found by this id[" + id + "]");
            }
            return field;
        } catch (Exception e) {
            LOG.error("Error getting Field by id", e);
            throw e;
        }
    }

    public void saveFields(List<FieldDetail> fields) {
        try {
            fields.stream().forEach((detail) -> {
                fieldRep.save(detail);
            });
        } catch (Exception e) {
            LOG.error("Error saving fields", e);
            throw e;
        }
    }

}
