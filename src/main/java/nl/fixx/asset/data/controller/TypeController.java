/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.controller;

import nl.fixx.asset.data.domain.AssetType;
import nl.fixx.asset.data.info.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import nl.fixx.asset.data.repository.TypeRepository;

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
    public Response addAsset(@RequestBody AssetType payload) {
        System.out.println(payload.toString());
        Response response = new Response();
        response.setAction("POST");
        response.setMethod("/add");
        if (payload != null) {
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
            } catch (IllegalArgumentException ex) {
                response.setSuccess(false);
                response.setMessage(ex.getMessage());
            }
        } else {
            response.setSuccess(false);
            response.setMessage("Type is null. Aborting insert due to empty type!");
        }

        return response;
    }

}
