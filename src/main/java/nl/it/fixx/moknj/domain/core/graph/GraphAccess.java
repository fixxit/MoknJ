package nl.it.fixx.moknj.domain.core.graph;

import java.util.List;
import org.springframework.data.annotation.Id;

/**
 * This class is used to determine edit access and view access Creators always
 * have edit access and users with admin access. The view access user id is used
 * for get graph data on home page, this allows admins/creators to add graphs to
 * users home page. Keep in mind that user authorities has higher tier than this
 * class so if ALL_ACCESS is present in the user access the creator id would not
 * matter.
 *
 * @author adriaan
 */
public class GraphAccess {

    @Id
    private String id;
    private String graphId;
    // Who created the graph... (auto view/edit access)
    private String creatorId;
    // User allowed to add to their view aka home page
    private List<String> viewAccessUserId;

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
     * @return the creatorId
     */
    public String getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId the creatorId to set
     */
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * @return the viewAccessUserId
     */
    public List<String> getViewAccessUserId() {
        return viewAccessUserId;
    }

    /**
     * @param viewAccessUserId the viewAccessUserId to set
     */
    public void setViewAccessUserId(List<String> viewAccessUserId) {
        this.viewAccessUserId = viewAccessUserId;
    }

}
