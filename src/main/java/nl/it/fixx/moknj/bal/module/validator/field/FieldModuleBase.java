package nl.it.fixx.moknj.bal.module.validator.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.it.fixx.moknj.bal.BAL;
import nl.it.fixx.moknj.bal.core.FieldBal;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FieldModuleBase<DOMAIN extends Record, REPO extends RecordRepository<DOMAIN>>
        extends BAL<REPO> implements FieldModule {

    private static final String HAS_CHANGE = "has_changes";
    private static final String NO_CHANGE = "no_changes";

    private final Logger log;
    private final FieldBal fieldBal;
    private FieldModule nextFieldValidation;
    private Module module;

    public FieldModuleBase(REPO repository, FieldBal fieldBal, Class cls) {
        super(repository);
        this.fieldBal = fieldBal;
        this.log = LoggerFactory.getLogger(cls);
    }

    @Override
    public boolean hasNext() {
        return this.nextFieldValidation != null;
    }

    @Override
    public void setNextIn(FieldModule nextFieldValidation) {
        this.nextFieldValidation = nextFieldValidation;
    }

    @Override
    public void setModuleAllowed(Module module) {
        this.module = module;
    }

    @Override
    public void validate(String templateId, String menuId, Object record) {
        if (getModule().equals(module)) {
            validateFields(templateId, menuId, (DOMAIN) record);
        } else {
            if (nextFieldValidation != null) {
                nextFieldValidation.setModuleAllowed(module);
                nextFieldValidation.validate(templateId, menuId, record);
            }
        }
    }

    public void validateFields(String templateId, String menuId, DOMAIN record) {
        // checks if the original object differs from new saved object
        // stop the unique filter form check for duplicates on asset
        // which has not changed from the db version...
        String flag = (record.getId() != null
                && record.equals(repository.findOne(record.getId())))
                ? NO_CHANGE : HAS_CHANGE;

        Map<String, String> uniqueFields = new HashMap<>();
        Map<String, Boolean> unifieldIndicator = new HashMap<>();
        Map<String, List<String>> uniqueValues = new HashMap<>();

        // Get all the unique field ids
        List<FieldValue> fieldValues = record.getDetails();
        fieldValues.stream().forEach((field) -> {
            try {
                FieldDetail detail = fieldBal.get(field.getId());
                if (detail != null && detail.isUnique()) {
                    uniqueFields.put(field.getId(), detail.getName());
                }
            } catch (Exception ex) {
                log.error("Error getting field detail", ex);
            }
        });

        // Create a list of all the values for the unique assets
        for (DOMAIN rcd : repository.getAllByTypeId(templateId)) {
            // if statement below checks that if update asset does not check
            // it self to flag for duplication
            if (!rcd.getId().equals(record.getId())
                    && !NO_CHANGE.equals(flag)) {
                List<FieldValue> details = rcd.getDetails();
                details.stream().filter((field)
                        -> (uniqueFields.keySet().contains(field.getId()))).map((field)
                        -> {
                    if (uniqueValues.get(field.getId()) == null) {
                        uniqueValues.put(field.getId(), new ArrayList<>());
                    }
                    return field;
                }).filter((field)
                        -> (!uniqueValues.get(field.getId()).contains(field.getValue()))).forEach((field)
                        -> {
                    uniqueValues.get(field.getId()).add(field.getValue());
                });
            }
        }

        // check if fields to be saved for asset has duplicates
        if (!uniqueValues.isEmpty()) {
            fieldValues.stream().filter((field)
                    -> (uniqueValues.containsKey(field.getId()))).filter((field)
                    -> (uniqueValues.get(field.getId()).contains(field.getValue()))).forEach((field)
                    -> {
                unifieldIndicator.put(field.getId(), true);
            });
        }

        // generate duplication message
        if (!unifieldIndicator.isEmpty()) {
            String message = unifieldIndicator.size() > 1
                    ? "Non unique values for fields [" : "Non unique value for field ";
            message = unifieldIndicator.keySet().stream().map((typeId)
                    -> uniqueFields.get(typeId)).map((fieldName)
                    -> fieldName + ",").reduce(message, String::concat);
            if (message.endsWith(",")) {
                message = message.substring(0, message.length() - 1);
            }

            message += unifieldIndicator.size() > 1
                    ? "]. Please check values" : ". Please input new value";
            throw new BalException(message);
        }
    }
}
