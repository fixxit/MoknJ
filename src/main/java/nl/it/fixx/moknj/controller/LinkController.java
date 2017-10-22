package nl.it.fixx.moknj.controller;

import nl.it.fixx.moknj.bal.module.link.impl.AssetLinkBal;
import nl.it.fixx.moknj.bal.module.link.impl.EmployeeLinkBal;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.response.LinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/link")
public class LinkController {

    private final AssetLinkBal assetLinkBal;
    private final EmployeeLinkBal employeeLinkBal;

    @Autowired
    public LinkController(AssetLinkBal assetLinkBal, EmployeeLinkBal employeeLinkBal) {
        this.assetLinkBal = assetLinkBal;
        this.employeeLinkBal = employeeLinkBal;
    }

    @RequestMapping(value = "/asset/{menuId}/{templateId}/add", method = RequestMethod.POST)
    public LinkResponse addAssetLink(@RequestBody AssetLink payload,
            @PathVariable String templateId,
            @PathVariable String menuId,
            @RequestParam String access_token) {
        LinkResponse response = new LinkResponse();
        response.setLink(assetLinkBal.linkAssetToUser(menuId, templateId,
                payload, access_token));
        response.setSuccess(true);
        response.setMessage("Saved link successfully");
        return response;
    }

    /**
     * Get getAllLinks links
     *
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/asset/all", method = RequestMethod.POST)
    public LinkResponse getAllAssetLinks(@RequestParam String access_token) {
        final LinkResponse response = new LinkResponse();
        response.setLinks(assetLinkBal.getAllLinks(access_token));
        response.setSuccess(true);
        return response;
    }

    /**
     * Get getAllLinks links
     *
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/employee/all", method = RequestMethod.POST)
    public LinkResponse getAllEmployeeLinks(@RequestParam String access_token) {
        LinkResponse response = new LinkResponse();
        response.setEmployeeLinks(employeeLinkBal.getAllLinks(access_token));
        response.setSuccess(true);
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
        response.setEmployeeLinks(employeeLinkBal.getAllLinksByRecordId(id, access_token));
        response.setSuccess(true);
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
        response.setLinks(assetLinkBal.getAllLinksByRecordId(id, access_token));
        response.setSuccess(true);
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
        response.setLinks(assetLinkBal.getAllAssetLinksByResourceId(id, access_token));
        response.setSuccess(true);
        return response;
    }

}
