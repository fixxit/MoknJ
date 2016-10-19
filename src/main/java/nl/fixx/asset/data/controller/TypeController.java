/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.controller;

import java.util.ArrayList;
import nl.fixx.asset.data.domain.AssetFieldType;
import nl.fixx.asset.data.domain.AssetType;
import nl.fixx.asset.data.info.TypeResponse;
import nl.fixx.asset.data.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adriaan
 */
@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/asset/type")
public class TypeController {

    @Autowired
    private TypeRepository resp;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public TypeResponse add(@RequestBody AssetType payload) {
        System.out.println(payload.toString());
        TypeResponse response = new TypeResponse();
        response.setAction("POST");
        response.setMethod("/add");

        if (payload.getDetails() == null
                && payload.getDetails().isEmpty()) {
            response.setMessage("No field types recieved to save. "
                    + "Aborting insert due to empty type!");
            return response;
        }

        try {
            AssetType type = this.resp.save(payload);
            response.setSuccess(type != null);
            response.setMessage("Saved type[" + type.getId() + "]");
            response.setType(type);
        } catch (IllegalArgumentException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    @RequestMapping(value = "/fields", method = RequestMethod.POST)
    public TypeResponse types() {
        TypeResponse response = new TypeResponse();
        AssetFieldType[] fieldTypes = AssetFieldType.values();
        ArrayList<String> types = new ArrayList<>();
        for (AssetFieldType type : fieldTypes) {
            types.add(type.name());
        }
        response.setFieldTypes(types);
        return response;
    }

}
