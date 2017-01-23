package nl.it.fixx.moknj.bal;

import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adriaan
 */
public class TemplateBal implements BusinessAccessLayer {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateBal.class);
    private final TemplateRepository templateRep;

    public TemplateBal(TemplateRepository templateRep) {
        this.templateRep = templateRep;
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
                LOG.info("No template id recieved to find template");
                throw new Exception("No template found, no template id provided!");
            }

            Template template = templateRep.findOne(id);
            if (template == null) {
                LOG.info("no template found for id[" + id + "]");
                throw new Exception("No template found by this id[" + id + "]");
            }
            return template;
        } catch (Exception e) {
            LOG.info("Error on Template Bal", e);
            throw new Exception("Exception trying to retrieve template", e);
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
}
