/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.domain.AssetFieldType;
import nl.fixx.asset.data.domain.AssetLink;
import nl.fixx.asset.data.domain.AssetType;
import nl.fixx.asset.data.domain.FieldType;
import nl.fixx.asset.data.info.TypeResponse;
import nl.fixx.asset.data.repository.AssetFieldDetailRepository;
import nl.fixx.asset.data.repository.AssetLinkRepository;
import nl.fixx.asset.data.repository.AssetRepository;
import nl.fixx.asset.data.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.comparator.NullSafeComparator;
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
public class TypeController {

    @Autowired
    private TypeRepository typeRep;

    @Autowired
    private AssetFieldDetailRepository fieldDetailRep;

    @Autowired
    private AssetRepository assetRep;

    @Autowired
    private AssetLinkRepository auditRep;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    TypeResponse add(@RequestBody AssetType payload) {
        TypeResponse response = new TypeResponse();
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

                AssetType type = this.typeRep.save(payload);
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
    TypeResponse get(@PathVariable String id) {
        TypeResponse response = new TypeResponse();
        response.setType(typeRep.findOne(id));
        return response;
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public @ResponseBody
    TypeResponse all() {
        TypeResponse response = new TypeResponse();
        List<AssetType> templates = typeRep.findAll();
        List<AssetType> types = new ArrayList<>();

        templates.stream().filter((type) -> (!type.isHidden())).forEach((type) -> {
            types.add(type);
        });

        Collections.sort(types, (AssetType a1, AssetType a2) -> {
            return new NullSafeComparator<>(String::compareTo,
                    true).compare(a1.getIndex(), a2.getIndex());
        });

        response.setTypes(types);
        return response;
    }

    @RequestMapping(value = "/hidden", method = RequestMethod.POST)
    public @ResponseBody
    TypeResponse hidden() {
        TypeResponse response = new TypeResponse();
        List<AssetType> templates = typeRep.findAll();
        List<AssetType> types = new ArrayList<>();
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
    public TypeResponse unhide(@PathVariable String id) {
        TypeResponse response = new TypeResponse();
        try {
            AssetType template = typeRep.findOne(id);
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
     * To do add delete method for types.
     *
     * @param id
     * @param cascade
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody
    TypeResponse delete(@PathVariable String id, @RequestParam boolean cascade) {
        TypeResponse response = new TypeResponse();
        AssetType template = typeRep.findOne(id);
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
    TypeResponse types() {
        TypeResponse response = new TypeResponse();
        AssetFieldType[] fieldTypes = AssetFieldType.values();
        List<FieldType> types = new ArrayList<>();
        for (AssetFieldType type : fieldTypes) {
            types.add(new FieldType(type.name(), type.getType()));
        }
        response.setFieldTypes(types);
        return response;
    }

}
