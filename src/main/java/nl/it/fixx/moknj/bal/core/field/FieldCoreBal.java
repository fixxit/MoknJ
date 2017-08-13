package nl.it.fixx.moknj.bal.core.field;

import java.util.List;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;

public interface FieldCoreBal {

    FieldDetail get(String id);

    void save(List<FieldDetail> fields);
}
