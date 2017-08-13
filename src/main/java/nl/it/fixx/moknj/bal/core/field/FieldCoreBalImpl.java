package nl.it.fixx.moknj.bal.core.field;

import java.util.List;
import nl.it.fixx.moknj.bal.BalBase;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.FieldDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Field Detail Business Access Layer
 *
 * @author adriaan
 */
@Service
public class FieldCoreBalImpl extends BalBase<FieldDetailRepository> implements FieldCoreBal {

    private static final Logger LOG = LoggerFactory.getLogger(FieldCoreBalImpl.class);

    @Autowired
    public FieldCoreBalImpl(FieldDetailRepository fieldDetailRepo) {
        super(fieldDetailRepo);
    }

    @Override
    public FieldDetail get(String id) {
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

    @Override
    public void save(List<FieldDetail> fields) {
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
