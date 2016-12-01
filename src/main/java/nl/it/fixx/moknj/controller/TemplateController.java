/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.controller;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.domain.core.field.FieldType;
import nl.it.fixx.moknj.domain.core.global.GlobalFieldType;
import nl.it.fixx.moknj.domain.core.global.GlobalTemplateType;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.template.TemplateType;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.repository.AssetLinkRepository;
import nl.it.fixx.moknj.repository.AssetRepository;
import nl.it.fixx.moknj.repository.FieldDetailRepository;
import nl.it.fixx.moknj.repository.TemplateRepository;
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

/**
 *
 * @author adriaan
 */
@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/type")
public class TemplateController {

    @Autowired
    private TemplateRepository typeRep;

    @Autowired
    private FieldDetailRepository fieldDetailRep;

    @Autowired
    private AssetRepository assetRep;

    @Autowired
    private AssetLinkRepository auditRep;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse add(@RequestBody Template payload) {
        TemplateResponse response = new TemplateResponse();
        response.setAction("POST");
        response.setMethod("/add");

        if (payload.getDetails() == null && payload.getDetails().isEmpty()) {
            response.setMessage("No field types recieved to save. " + "Aborting insert due to empty type!");
            return response;
        }

        try {
            // For updates if the type has a id then bypass the exists
            boolean bypassExists = false;
            if (payload.getId() != null) {
                bypassExists = true;
            }

            boolean exists = typeRep.existsByName(payload.getName());
            if (!exists || bypassExists) {
                payload.getDetails().stream().forEach((detail) -> {
                    this.fieldDetailRep.save(detail);
                });

                Template type = this.typeRep.save(payload);
                response.setSuccess(type != null);
                response.setMessage("Saved type[" + type.getId() + "]");
                response.setType(type);
            } else {
                response.setSuccess(false);
                response.setMessage("Asset type by name " + payload.getName() + " exists");
            }
        } catch (IllegalArgumentException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse get(@PathVariable String id) {
        TemplateResponse response = new TemplateResponse();
        response.setType(typeRep.findOne(id));
        return response;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse all() {
        TemplateResponse response = new TemplateResponse();
        List<Template> templates = typeRep.findAll();
        List<Template> types = new ArrayList<>();

        templates.stream().filter((type) -> (!type.isHidden())).forEach((type) -> {
            types.add(type);
        });

        response.setTypes(types);
        return response;
    }

    @RequestMapping(value = "/hidden", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse hidden() {
        TemplateResponse response = new TemplateResponse();
        List<Template> templates = typeRep.findAll();
        List<Template> types = new ArrayList<>();
        templates.stream().filter((type) -> (type.isHidden())).forEach((type) -> {
            types.add(type);
        });

        response.setTypes(types);
        return response;
    }

    /**
     * change template state to visible...
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/unhide/{id}", method = RequestMethod.POST)
    public TemplateResponse unhide(@PathVariable String id) {
        TemplateResponse response = new TemplateResponse();
        try {
            Template template = typeRep.findOne(id);
            template.setHidden(false);
            template = this.typeRep.save(template);
            response.setSuccess(template != null);
            response.setMessage("Template [" + template.getName() + "] "
                    + " set to visible!");
            response.setType(template);
        } catch (IllegalArgumentException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    /**
     * To do add delete method for getGlobalFields.
     *
     * @param id
     * @param cascade
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody
    TemplateResponse delete(@PathVariable String id, @RequestParam boolean cascade) {
        TemplateResponse response = new TemplateResponse();
        Template template = typeRep.findOne(id);
        if (!cascade) {
            try {
                template.setHidden(true);
                template = this.typeRep.save(template);
                response.setSuccess(template != null);
                response.setMessage("Template [" + template.getName() + "] "
                        + "is set to hidden");
                response.setType(template);
            } catch (IllegalArgumentException ex) {
                response.setSuccess(false);
                response.setMessage(ex.getMessage());
            }
        } else {
            try {
                List<Asset> assets = assetRep.getAllByTypeId(id);
                assets.stream().forEach((asset) -> {
                    List<AssetLink> links = auditRep.getAllByAssetId(asset.getId());
                    links.stream().forEach((link) -> {
                        auditRep.delete(link);
                    });
                    assetRep.delete(asset);
                });
                typeRep.delete(template);
                response.setSuccess(true);
                response.setMessage("Template [" + template.getName() + "] and "
                        + "all assets/audits relating to this template");
            } catch (IllegalArgumentException ex) {
                response.setSuccess(false);
                response.setMessage(ex.getMessage());
            }
        }

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

}
