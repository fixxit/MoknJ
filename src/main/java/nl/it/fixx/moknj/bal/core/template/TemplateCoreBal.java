package nl.it.fixx.moknj.bal.core.template;

import java.util.List;
import nl.it.fixx.moknj.domain.core.template.Template;

public interface TemplateCoreBal {

    public Template saveTemplate(Template payload);

    public Template getTemplateById(String id);

    public boolean exists(String id);

    public String getDispayName(Template template);

    public String getDispayName(String templateId);

    public List<Template> getAllTemplates();

    public void deleteTemplate(String id, boolean cascade);
}
