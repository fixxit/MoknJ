package nl.it.fixx.moknj.bal;

import java.util.List;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.service.SystemContext;
import nl.it.fixx.moknj.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adriaan
 */
public class TemplateBal implements BusinessAccessLayer {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateBal.class);
    private final TemplateRepository tempRep;
    private final FieldBal fieldBal;

    public TemplateBal(SystemContext context) {
        this.tempRep = context.getRepository(TemplateRepository.class);
        this.fieldBal = new FieldBal(context);
    }

    /**
     * Saves template and saves inserts a new field detail in field detail
     * table.
     *
     * @param payload Template
     * @return Template
     * @throws Exception
     */
    public Template saveTemplate(Template payload) throws Exception {
        try {
            if (payload.getDetails() == null && payload.getDetails().isEmpty()) {
                throw new Exception("No field types recieved to save. " + "Aborting insert due to empty type!");
            }

            // For updates if the type has a id then bypass the exists
            boolean bypassExists = false;
            if (payload.getId() != null) {
                bypassExists = true;
            }

            boolean exists = tempRep.existsByName(payload.getName());
            if (!exists || bypassExists) {
                fieldBal.saveFields(payload.getDetails());
                Template template = this.tempRep.save(payload);
                return template;
            } else {
                throw new Exception("Template by name " + payload.getName() + " exists");
            }
        } catch (Exception e) {
            LOG.error("Error saving template[" + payload + "]", e);
            throw e;
        }
    }

    /**
     * Gets template by UUID.
     *
     * @param id
     * @return template.
     * @throws Exception
     */
    public Template getTemplateById(String id) throws Exception {
        try {
            if (id == null || id.isEmpty()) {
                LOG.debug("No template id recieved to find template");
                throw new Exception("No template found, no template id provided!");
            }

            Template template = tempRep.findOne(id);
            if (template == null) {
                LOG.debug("no template found for id[" + id + "]");
                throw new Exception("No template found by this id[" + id + "]");
            }
            return template;
        } catch (Exception e) {
            LOG.error("Error getting template by id", e);
            throw e;
        }
    }

    public boolean exists(String id) throws Exception {
        try {
            return tempRep.exists(id);
        } catch (Exception e) {
            LOG.error("Error getting template by id", e);
            throw e;
        }
    }

    /**
     * Returns the template display name.
     *
     * @param template
     * @return
     * @throws Exception
     */
    public String getDispayName(Template template) throws Exception {
        if (template == null) {
            throw new Exception("No template object provided");
        }

        return template.getName();
    }

    /**
     * Returns the the template display name by template UUID.
     *
     * @param templateId
     * @return
     * @throws Exception
     */
    public String getDispayName(String templateId) throws Exception {
        return getDispayName(getTemplateById(templateId));
    }

    /**
     * Gets all the menu's
     *
     * @return list of menus.
     * @throws Exception
     */
    public List<Template> getAllTemplates() throws Exception {
        try {
            return tempRep.findAll();
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
     * @throws Exception
     */
    public void deleteTemplate(String id, boolean cascade) throws Exception {
        try {
            Template template = getTemplateById(id);
            if (!cascade) {
                LOG.debug("hiding template[" + template.getName() + "]");
                template.setHidden(true);
                this.saveTemplate(template);
            } else {
                LOG.debug("deleting template[" + template.getName() + "]");
                this.tempRep.delete(id);
            }
        } catch (Exception e) {
            LOG.error("error getting all menus", e);
            throw e;
        }
    }
}
