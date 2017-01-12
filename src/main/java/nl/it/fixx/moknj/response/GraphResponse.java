package nl.it.fixx.moknj.response;

import java.util.List;
import java.util.Map;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphData;
import nl.it.fixx.moknj.domain.core.graph.GraphFocus;
import nl.it.fixx.moknj.domain.core.graph.GraphType;
import nl.it.fixx.moknj.domain.core.graph.GraphView;

/**
 *
 * @author adriaan
 */
public class GraphResponse extends Response {

    private Graph graphTemplate;
    private GraphData graphData;
    private List<GraphType> graphTypes;
    private List<GraphView> graphViews;
    private List<GraphFocus> graphFocuses;
    private List<Graph> savedGraphs;
    private Map<String, List<GraphData>> allGraphsData;

    /**
     * @return the graphTemplate
     */
    public Graph getGraphTemplate() {
        return graphTemplate;
    }

    /**
     * @param graphTemplate the graphTemplate to set
     */
    public void setGraphTemplate(Graph graphTemplate) {
        this.graphTemplate = graphTemplate;
    }

    /**
     * @return the graphData
     */
    public GraphData getGraphData() {
        return graphData;
    }

    /**
     * @param graphData the graphData to set
     */
    public void setGraphData(GraphData graphData) {
        this.graphData = graphData;
    }

    /**
     * @return the graphTypes
     */
    public List<GraphType> getGraphTypes() {
        return graphTypes;
    }

    /**
     * @param graphTypes the graphTypes to set
     */
    public void setGraphTypes(List<GraphType> graphTypes) {
        this.graphTypes = graphTypes;
    }

    /**
     * @return the graphViews
     */
    public List<GraphView> getGraphViews() {
        return graphViews;
    }

    /**
     * @param graphViews the graphViews to set
     */
    public void setGraphViews(List<GraphView> graphViews) {
        this.graphViews = graphViews;
    }

    /**
     * @return the savedGraphs
     */
    public List<Graph> getSavedGraphs() {
        return savedGraphs;
    }

    /**
     * @param savedGraphs the savedGraphs to set
     */
    public void setSavedGraphs(List<Graph> savedGraphs) {
        this.savedGraphs = savedGraphs;
    }

    /**
     * @return the graphFocuses
     */
    public List<GraphFocus> getGraphFocuses() {
        return graphFocuses;
    }

    /**
     * @param graphFocuses the graphFocuses to set
     */
    public void setGraphFocuses(List<GraphFocus> graphFocuses) {
        this.graphFocuses = graphFocuses;
    }

    /**
     * @return the allGraphsData
     */
    public Map<String, List<GraphData>> getAllGraphsData() {
        return allGraphsData;
    }

    /**
     * @param allGraphsData the allGraphsData to set
     */
    public void setAllGraphsData(Map<String, List<GraphData>> allGraphsData) {
        this.allGraphsData = allGraphsData;
    }

    @Override
    public String toString() {
        return "GraphResponse{" + "graphTemplate=" + graphTemplate + ", graphData=" + graphData + ", graphTypes=" + graphTypes + ", graphViews=" + graphViews + ", graphFocuses=" + graphFocuses + ", savedGraphs=" + savedGraphs + ", allGraphsData=" + allGraphsData + '}';
    }
}
