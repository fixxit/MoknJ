package nl.it.fixx.moknj.controller;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.bal.core.access.MainAccessCoreBal;
import nl.it.fixx.moknj.bal.core.template.TemplateCoreBal;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldType;
import nl.it.fixx.moknj.domain.core.global.GlobalFieldType;
import nl.it.fixx.moknj.domain.core.global.GlobalTemplateType;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.template.TemplateType;
import nl.it.fixx.moknj.response.TemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/type")
public class TemplateController {

    @Autowired
    private MainAccessCoreBal mainAccessBal;
    @Autowired
    private TemplateCoreBal templateBal;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse add(@RequestBody Template payload, @RequestParam String access_token) {
        TemplateResponse response = new TemplateResponse();
        Template template = mainAccessBal.saveTemplate(payload, access_token);
        response.setSuccess(template != null);
        response.setMessage("Saved " + template.getName());
        response.setType(template);
        return response;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse get(@PathVariable String id, @RequestParam String access_token) {
        TemplateResponse response = new TemplateResponse();
        response.setType(mainAccessBal.getTemplate(id, access_token));
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse all(@RequestParam String access_token) {
        TemplateResponse response = new TemplateResponse();
        response.setTypes(mainAccessBal.getAllTemplatesForToken(access_token));
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/hidden", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse hidden(@RequestParam String access_token) {
        TemplateResponse response = new TemplateResponse();
        List<Template> templates = templateBal.getAllTemplates();
        List<Template> types = new ArrayList<>();
        templates.stream().filter((type) -> (type.isHidden())).forEach((type) -> {
            types.add(type);
        });
        response.setTypes(types);
        response.setSuccess(true);
        return response;
    }

    /**
     * change template state to visible...
     *
     * @param id
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/unhide/{id}", method = RequestMethod.POST)
    public TemplateResponse unhide(@PathVariable String id,
            @RequestParam String access_token) {
        TemplateResponse response = new TemplateResponse();
        Template template = templateBal.getTemplateById(id);
        template.setHidden(false);
        mainAccessBal.saveTemplate(template, access_token);
        response.setMessage("Template [" + template.getName() + "] "
                + " set to visible!");
        response.setType(template);
        response.setSuccess(true);
        return response;
    }

    /**
     * To do add delete method for getGlobalFields.
     *
     * @param id
     * @param cascade
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse delete(@PathVariable String id,
            @RequestParam boolean cascade,
            @RequestParam String access_token) {
        TemplateResponse response = new TemplateResponse();
        mainAccessBal.deleteTemplate(id, cascade, access_token);
        response.setSuccess(true);
        response.setMessage("Deleted template");
        return response;
    }

    @RequestMapping(value = "/fields", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse getFields() {
        TemplateResponse response = new TemplateResponse();
        GlobalFieldType[] fieldTypes = GlobalFieldType.values();
        List<FieldType> types = new ArrayList<>();
        for (GlobalFieldType type : fieldTypes) {
            types.add(new FieldType(type.name(), type.getName()));
        }
        response.setFieldTypes(types);
        return response;
    }

    @RequestMapping(value = "/templates", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse getTemplates() {
        TemplateResponse response = new TemplateResponse();
        GlobalTemplateType[] moduleTypes = GlobalTemplateType.values();
        List<TemplateType> types = new ArrayList<>();
        for (GlobalTemplateType type : moduleTypes) {
            types.add(new TemplateType(type.name(), type.getName()));
        }
        response.setTemplateTypes(types);
        return response;
    }

    @RequestMapping(value = "/template/dropdown/{id}/fields", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse getTemplateDropdownFields(@PathVariable String id,
            @RequestParam String access_token) {
        TemplateResponse response = new TemplateResponse();
        Template template = mainAccessBal.getTemplate(id, access_token);
        List<FieldDetail> fields = new ArrayList<>();
        template.getDetails().stream().filter((field)
                -> (GlobalFieldType.GBL_INPUT_DRP_TYPE.equals(field.getType())))
                .forEach((field) -> {
                    fields.add(field);
                });
        response.setFields(fields);
        return response;
    }

    @RequestMapping(value = "/template/date/{id}/fields", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse getTemplateDateFieldFields(@PathVariable String id,
            @RequestParam String access_token) {
        TemplateResponse response = new TemplateResponse();
        Template template = mainAccessBal.getTemplate(id, access_token);
        List<FieldDetail> fields = new ArrayList<>();
        template.getDetails().stream().filter((field)
                -> (GlobalFieldType.GBL_INPUT_DAT_TYPE.equals(field.getType())))
                .forEach((field) -> {
                    fields.add(field);
                });
        response.setFields(fields);
        return response;
    }

}
