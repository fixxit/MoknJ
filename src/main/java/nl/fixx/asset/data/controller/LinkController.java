/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import nl.fixx.asset.data.domain.Asset;
import nl.fixx.asset.data.domain.AssetLink;
import nl.fixx.asset.data.domain.Resource;
import nl.fixx.asset.data.info.LinkResponse;
import nl.fixx.asset.data.repository.AssetLinkRepository;
import nl.fixx.asset.data.repository.AssetRepository;
import nl.fixx.asset.data.repository.ResourceRepository;
import nl.fixx.asset.data.security.OAuth2SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adriaan
 */
@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/link")
public class LinkController {

    @Autowired
    private AssetLinkRepository auditRep; // Asset Audit Repository
    @Autowired
    private AssetRepository assetRep;// main asset Repository
    @Autowired
    private ResourceRepository resourceRep;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public LinkResponse add(@RequestBody AssetLink payload, @RequestParam String access_token) {
        final LinkResponse response = new LinkResponse();
        try {
            String user = getUserName(access_token);
            if (user != null && !user.trim().isEmpty()) {
                payload.setCreatedBy(user);
            } else {
                response.setSuccess(false);
                response.setMessage("Could not find system user for this token");
                return response;
            }

            payload.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            response.setLink(this.auditRep.save(payload));

            // add resource id to asset depending on isChecked
            if (payload.getAssetId() != null) {
                Asset dbAsset = assetRep.findOne(payload.getAssetId());
                if (payload.isChecked()) {
                    dbAsset.setResourceId(payload.getResourceId());
                } else {
                    dbAsset.setResourceId(null);
                }

                dbAsset.setLastModifiedBy(user);
                dbAsset.setLastModifiedDate(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                assetRep.save(dbAsset);
            }

            response.setSuccess(response.getLink() != null);
            response.setMessage("Saved type[" + response.getLink().getId() + "]");
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    private String getUserName(String access_token) throws Exception {
        String username = null;
        Resource user = resourceRep.findByUserName(OAuth2SecurityConfig.getUserForToken(access_token));
        if (user != null && user.isSystemUser()) {
            String fullname = user.getFirstName() + " " + user.getSurname();
            if (!fullname.trim().isEmpty()) {
                username = fullname;
            } else {
                username = user.getUserName();
            }
        }
        return username;
    }

    /**
     * Get all links
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public LinkResponse all() {
        final LinkResponse response = new LinkResponse();
        response.setLinks(auditRep.findAll(new Sort(Sort.Direction.DESC, "date")));
        return response;
    }

    /**
     * Get list of link entries for the asset id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/all/{id}/asset", method = RequestMethod.POST)
    public LinkResponse allByAssetId(@PathVariable String id) {
        final LinkResponse response = new LinkResponse();
        response.setLinks(auditRep.getAllByAssetId(id));
        return response;
    }

    /**
     * Get list of link entries for the resource.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/all/{id}/resource", method = RequestMethod.POST)
    public LinkResponse allByResourceId(@PathVariable String id) {
        final LinkResponse response = new LinkResponse();
        response.setLinks(auditRep.getAllByResourceId(id));
        return response;
    }

}
