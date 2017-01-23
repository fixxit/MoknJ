/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.domain.core.graph;

import org.springframework.data.annotation.Id;

/**
 * This class is used to save user specific home graphs. For every graph a user
 * adds to his home screen a record of GraphUserView will be created.
 *
 * @author adriaan
 */
public class GraphUser {

    @Id
    private String id;
    private String graphId;
    private String userId;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the graphId
     */
    public String getGraphId() {
        return graphId;
    }

    /**
     * @param graphId the graphId to set
     */
    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
