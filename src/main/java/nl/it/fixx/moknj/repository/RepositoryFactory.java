/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author adriaan
 */
@Service
public class RepositoryFactory {

    @Autowired
    private UserRepository userRep;
    @Autowired
    private AssetRepository assetRep;
    @Autowired
    private EmployeeRepository employeeRep;
    @Autowired
    private AssetLinkRepository assetLinkRep;
    @Autowired
    private EmployeeLinkRepository employeeLinkRep;
    @Autowired
    private AccessRepository accessRep;
    @Autowired
    private MenuRepository menuRep;
    @Autowired
    private TemplateRepository templateRep;
    @Autowired
    private FieldDetailRepository fieldDetailRep;
    @Autowired
    private GraphRepository graphRep;

    /**
     * @return the userRep
     */
    public UserRepository getUserRep() {
        return userRep;
    }

    /**
     * @return the assetRep
     */
    public AssetRepository getAssetRep() {
        return assetRep;
    }

    /**
     * @return the assetLinkRep
     */
    public AssetLinkRepository getAssetLinkRep() {
        return assetLinkRep;
    }

    /**
     * @return the employeeLinkRep
     */
    public EmployeeLinkRepository getEmployeeLinkRep() {
        return employeeLinkRep;
    }

    /**
     * @return the accessRep
     */
    public AccessRepository getAccessRep() {
        return accessRep;
    }

    /**
     * @return the menuRep
     */
    public MenuRepository getMenuRep() {
        return menuRep;
    }

    /**
     * @return the templateRep
     */
    public TemplateRepository getTemplateRep() {
        return templateRep;
    }

    /**
     * @return the fieldDetailRep
     */
    public FieldDetailRepository getFieldDetailRep() {
        return fieldDetailRep;
    }

    /**
     * @return the employeeRep
     */
    public EmployeeRepository getEmployeeRep() {
        return employeeRep;
    }

    /**
     * @return the graphRep
     */
    public GraphRepository getGraphRep() {
        return graphRep;
    }

    /**
     * @param graphRep the graphRep to set
     */
    public void setGraphRep(GraphRepository graphRep) {
        this.graphRep = graphRep;
    }

}
