/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.controller;

import nl.fixx.asset.data.domain.AssetLink;
import nl.fixx.asset.data.info.LinkResponse;
import nl.fixx.asset.data.repository.AssetLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/asset/link")
public class LinkController {

    @Autowired
    private AssetLinkRepository auditRep; // Asset Audit Repository

    LinkResponse response = new LinkResponse();

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public LinkResponse add(@RequestBody AssetLink payload) {
        try {
            response.setLink(this.auditRep.save(payload));
            response.setSuccess(response.getLink() != null);
            response.setMessage("Saved type[" + response.getLink().getId() + "]");
        } catch (IllegalArgumentException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }
        return response;
    }

    /**
     * Get all links
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public LinkResponse all() {
        response.setLinks(auditRep.findAll(new Sort(Sort.Direction.ASC, "date")));
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
        AssetLink search = new AssetLink();
        search.setAssetId(id);
        ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().withMatcher("assetId",
                ExampleMatcher.GenericPropertyMatchers.ignoreCase());
        Example<AssetLink> example = Example.<AssetLink>of(search, NAME_MATCHER);
        response.setLinks(auditRep.findAll(example, new Sort(Sort.Direction.ASC, "date")));
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
        AssetLink search = new AssetLink();
        search.setResourceId(id);
        ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().withMatcher("resourceId",
                ExampleMatcher.GenericPropertyMatchers.ignoreCase());
        Example<AssetLink> example = Example.<AssetLink>of(search, NAME_MATCHER);
        response.setLinks(auditRep.findAll(example, new Sort(Sort.Direction.ASC, "date")));
        return response;
    }

}
