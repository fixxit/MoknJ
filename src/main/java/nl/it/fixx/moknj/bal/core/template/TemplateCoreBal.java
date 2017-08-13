package nl.it.fixx.moknj.bal.core.template;

import java.util.List;
import nl.it.fixx.moknj.domain.core.template.Template;

public interface TemplateCoreBal {

    public Template saveTemplate(Template payload) throws Exception;

    public Template getTemplateById(String id);

    public boolean exists(String id);

    public String getDispayName(Template template) throws Exception;

    public String getDispayName(String templateId) throws Exception;

    public List<Template> getAllTemplates() throws Exception;

    public void deleteTemplate(String id, boolean cascade) throws Exception;
}
