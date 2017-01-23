/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.domain.modules.employee.EmployeeLink;
import nl.it.fixx.moknj.repository.AssetLinkRepository;
import nl.it.fixx.moknj.repository.AssetRepository;
import nl.it.fixx.moknj.repository.EmployeeLinkRepository;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import nl.it.fixx.moknj.repository.UserRepository;
import nl.it.fixx.moknj.response.LinkResponse;
import nl.it.fixx.moknj.security.OAuth2SecurityConfig;
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
    private AssetLinkRepository assetLinkRep;
    @Autowired
    private AssetRepository assetRep;
    @Autowired
    private UserRepository userRep;
    @Autowired
    private EmployeeLinkRepository employeeLinkRep;
    @Autowired
    private EmployeeRepository employeeRep;

    @Autowired
    private UserRepository resourceRep;

    @RequestMapping(value = "/asset/add", method = RequestMethod.POST)
    public LinkResponse addAssetLink(@RequestBody AssetLink payload, @RequestParam String access_token) {
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
            response.setLink(this.assetLinkRep.save(payload));

            // addAssetLink resource id to asset depending on isChecked
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

    /**
     * Get getAllAssetLinks links
     *
     * @return
     */
    @RequestMapping(value = "/asset/all", method = RequestMethod.POST)
    public LinkResponse getAllAssetLinks() {
        final LinkResponse response = new LinkResponse();
        response.setLinks(assetLinkRep.findAll(new Sort(Sort.Direction.DESC, "createdDate")));
        return response;
    }

    /**
     * Get getAllAssetLinks links
     *
     * @return
     */
    @RequestMapping(value = "/employee/all", method = RequestMethod.POST)
    public LinkResponse getAllEmployeeLinks() {
        final LinkResponse response = new LinkResponse();
        response.setEmployeeLinks(employeeLinkRep.findAll(new Sort(Sort.Direction.DESC, "createdDate")));
        for (EmployeeLink link : response.getEmployeeLinks()) {
            // gets the user edited the record.
            Employee dbEmployee = employeeRep.findOne(link.getEmployeeId());
            if (dbEmployee != null
                    && dbEmployee.getResourceId() != null) {
                User linkedUser = userRep.findById(dbEmployee.getResourceId());
                String fullname = linkedUser.getFirstName() + " " + linkedUser.getSurname();
                link.setUser(fullname);
            }
            link.setActionValue(link.getAction().getDisplayValue());
        }
        return response;
    }

    /**
     * Get list of link entries for the asset id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/employee/all/{id}/employee", method = RequestMethod.POST)
    public LinkResponse getAllEmployeeLinksByEmployeeId(@PathVariable String id) {
        final LinkResponse response = new LinkResponse();
        response.setEmployeeLinks(employeeLinkRep.getAllByEmployeeId(id));
        response.getEmployeeLinks().stream().map((link) -> {
            // gets the user edited the record.
            Employee dbEmployee = employeeRep.findOne(link.getEmployeeId());
            if (dbEmployee.getResourceId() != null) {
                User linkedUser = userRep.findById(dbEmployee.getResourceId());
                String fullname = linkedUser.getFirstName() + " " + linkedUser.getSurname();
                link.setUser(fullname);
            }
            return link;
        }).forEach((link) -> {
            link.setActionValue(link.getAction().getDisplayValue());
        });
        return response;
    }

    /**
     * Get list of link entries for the asset id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/asset/all/{id}/asset", method = RequestMethod.POST)
    public LinkResponse getAllAssetLinksByAssetId(@PathVariable String id) {
        final LinkResponse response = new LinkResponse();
        response.setLinks(assetLinkRep.getAllByAssetId(id));
        return response;
    }

    /**
     * Get list of link entries for the resource.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/asset/all/{id}/resource", method = RequestMethod.POST)
    public LinkResponse getAllAssetLinksByRecourceId(@PathVariable String id) {
        final LinkResponse response = new LinkResponse();
        response.setLinks(assetLinkRep.getAllByResourceId(id));
        return response;
    }

    private String getUserName(String access_token) throws Exception {
        String username = null;
        User user = resourceRep.findByUserName(OAuth2SecurityConfig.getUserForToken(access_token));
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

}
