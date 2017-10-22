package nl.it.fixx.moknj.bal.core.template;

import java.util.List;
import nl.it.fixx.moknj.bal.BalBase;
import nl.it.fixx.moknj.bal.core.field.FieldCoreBal;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateCoreBalImpl extends BalBase<TemplateRepository> implements TemplateCoreBal {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateCoreBalImpl.class);
    private final FieldCoreBal fieldBal;

    @Autowired
    public TemplateCoreBalImpl(TemplateRepository templateRepo, FieldCoreBal fieldBal) {
        super(templateRepo);
        this.fieldBal = fieldBal;
    }

    /**
     * Saves template and saves inserts a new field detail in field detail
     * table.
     *
     * @param payload Template
     * @return Template
     */
    @Override
    public Template saveTemplate(Template payload) {
        try {
            if (payload.getDetails() == null && payload.getDetails().isEmpty()) {
                throw new BalException("No field types recieved to save. " + "Aborting insert due to empty type!");
            }

            // For updates if the type has a id then bypass the exists
            boolean bypassExists = (payload.getId() != null);

            boolean exists = repository.existsByName(payload.getName());
            if (!exists || bypassExists) {
                fieldBal.save(payload.getDetails());
                Template template = this.repository.save(payload);
                return template;
            } else {
                throw new BalException("Template by name " + payload.getName() + " exists");
            }
        } catch (BalException e) {
            LOG.error("Error saving template[" + payload + "]", e);
            throw e;
        }
    }

    /**
     * Gets template by UUID.
     *
     * @param id
     * @return template.
     */
    @Override
    public Template getTemplateById(String id) {
        if (id == null || id.isEmpty()) {
            LOG.debug("No template id recieved to find template");
            throw new BalException("No template found, no template id provided!");
        }

        Template template = repository.findOne(id);
        if (template == null) {
            LOG.debug("no template found for id[" + id + "]");
            throw new BalException("No template found by this id[" + id + "]");
        }
        return template;
    }

    @Override
    public boolean exists(String id) {
        return repository.exists(id);
    }

    /**
     * Returns the template display name.
     *
     * @param template
     * @return
     */
    @Override
    public String getDispayName(Template template) {
        if (template == null) {
            throw new BalException("No template object provided");
        }

        return template.getName();
    }

    /**
     * Returns the the template display name by template UUID.
     *
     * @param templateId
     * @return
     */
    @Override
    public String getDispayName(String templateId) {
        return getDispayName(getTemplateById(templateId));
    }

    /**
     * Gets all the menu's
     *
     * @return list of menus.
     */
    @Override
    public List<Template> getAllTemplates() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            LOG.error("error getting all menus", e);
            throw e;
        }
    }

    /**
     * Deletes the template.
     *
     * @param id
     * @param cascade
     */
    @Override
    public void deleteTemplate(String id, boolean cascade) {
        try {
            Template template = getTemplateById(id);
            if (!cascade) {
                LOG.debug("hiding template[" + template.getName() + "]");
                template.setHidden(true);
                this.saveTemplate(template);
            } else {
                LOG.debug("deleting template[" + template.getName() + "]");
                this.repository.delete(id);
            }
        } catch (Exception e) {
            LOG.error("error getting all menus", e);
            throw e;
        }
    }

}
