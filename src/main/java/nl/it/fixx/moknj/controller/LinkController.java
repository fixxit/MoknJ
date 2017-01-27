/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.controller;

import nl.it.fixx.moknj.bal.LinkBal;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.repository.RepositoryFactory;
import nl.it.fixx.moknj.response.LinkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger LOG = LoggerFactory.getLogger(LinkController.class);
    @Autowired
    private RepositoryFactory factory;

    @RequestMapping(value = "/asset/{menuId}/{templateId}/add", method = RequestMethod.POST)
    public LinkResponse addAssetLink(@RequestBody AssetLink payload,
            @PathVariable String templateId,
            @PathVariable String menuId,
            @RequestParam String access_token) {
        LinkResponse response = new LinkResponse();
        try {
            LinkBal bal = new LinkBal(factory);

            LOG.info("templateId : " + templateId);
            LOG.info("menuId : " + menuId);

            AssetLink link = bal.linkAssetToUser(menuId, templateId,
                    payload, access_token);
            response.setLink(link);
            response.setSuccess(link != null);
            response.setMessage("Saved link successfully");
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    /**
     * Get getAllAssetLinks links
     *
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/asset/all", method = RequestMethod.POST)
    public LinkResponse getAllAssetLinks(@RequestParam String access_token) {
        final LinkResponse response = new LinkResponse();
        try {
            LinkBal bal = new LinkBal(factory);
            response.setLinks(bal.getAllAssetLinks(access_token));
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    /**
     * Get getAllAssetLinks links
     *
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/employee/all", method = RequestMethod.POST)
    public LinkResponse getAllEmployeeLinks(@RequestParam String access_token) {
        LinkResponse response = new LinkResponse();
        try {
            LinkBal bal = new LinkBal(factory);
            response.setEmployeeLinks(bal.getAllEmployeeLinks(access_token));
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    /**
     * Get list of link entries for the asset id
     *
     * @param id
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/employee/all/{id}/employee", method = RequestMethod.POST)
    public LinkResponse getAllEmployeeLinksByEmployeeId(@PathVariable String id, @RequestParam String access_token) {
        LinkResponse response = new LinkResponse();
        try {
            LinkBal bal = new LinkBal(factory);
            response.setEmployeeLinks(bal.getAllEmployeeLinksForEmployee(id, access_token));
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    /**
     * Get list of link entries for the asset id
     *
     * @param id
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/asset/all/{id}/asset", method = RequestMethod.POST)
    public LinkResponse getAllAssetLinksByAssetId(@PathVariable String id, @RequestParam String access_token) {
        LinkResponse response = new LinkResponse();
        try {
            LinkBal bal = new LinkBal(factory);
            response.setLinks(bal.getAllAssetLinksByAssetId(id, access_token));
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    /**
     * Get list of link entries for the resource.
     *
     * @param id
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/asset/all/{id}/resource", method = RequestMethod.POST)
    public LinkResponse getAllAssetLinksByRecourceId(@PathVariable String id, @RequestParam String access_token) {
        LinkResponse response = new LinkResponse();
        try {
            LinkBal bal = new LinkBal(factory);
            response.setLinks(bal.getAllAssetLinksByResourceId(id, access_token));
            response.setSuccess(true);
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

}
