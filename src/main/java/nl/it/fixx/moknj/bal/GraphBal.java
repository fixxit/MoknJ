/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.bal;

import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.repository.GraphRepository;
import nl.it.fixx.moknj.repository.MenuRepository;
import nl.it.fixx.moknj.repository.TemplateRepository;
import nl.it.fixx.moknj.repository.UserRepository;
import nl.it.fixx.moknj.security.OAuth2SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adriaan
 */
public class GraphBal implements BusinessAccessLayer {

    private static final Logger LOG = LoggerFactory.getLogger(GraphBal.class);

    private final MenuRepository menuRep;
    private final TemplateRepository templateRep;
    private final GraphRepository graphRep;
    private final UserRepository userRep;

    public GraphBal(MenuRepository menuRep, TemplateRepository templateRep,
            GraphRepository graphRep, UserRepository userRep) {
        this.menuRep = menuRep;
        this.templateRep = templateRep;
        this.graphRep = graphRep;
        this.userRep = userRep;
    }

    /**
     * Checks if the user is creator or has ALL_ACCESS assigned to his user
     * rights.
     *
     * @param graph to check for user access.
     * @param token token is used to determine user
     *
     * @return boolean
     */
    public boolean checkEditAccess(Graph graph, String token) {
        try {
            User user = userRep.findByUserName(OAuth2SecurityConfig.getUserForToken(token));
            return (user.getId().equals(graph.getCreatorId())
                    || user.getAuthorities().contains(ALL_ACCESS.toString()));
        } catch (Exception e) {
            LOG.error("Error when trying to check user access...");
            LOG.error("Exception:", e);
        }
        return false;
    }

    /**
     * Check if the user has view access. This will not check ALL_ACCESS as only
     * users to see graph is user who created it and users who are assigned to
     * the graph
     *
     * @param graph
     * @param token
     * @return boolean
     */
    public boolean checkViewAccess(Graph graph, String token) {
        try {
            User user = userRep.findByUserName(OAuth2SecurityConfig.getUserForToken(token));
            return (user.getId().equals(graph.getCreatorId())
                    || (user.getAuthorities() != null
                    && user.getAuthorities().contains(ALL_ACCESS.name()))
                    || (graph.getAccessUserIds() != null
                    && graph.getAccessUserIds().contains(user.getId())));
        } catch (Exception e) {
            LOG.error("Error when trying to check user access...");
            LOG.error("Exception:", e);
        }
        return false;
    }

}
