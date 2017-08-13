package nl.it.fixx.moknj.bal.core.graph;

import java.util.List;
import java.util.Map;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphData;

public interface GraphCoreBal {

    public Graph save(Graph payload, String token) throws Exception;

    public List<Graph> getAll(String token) throws Exception;

    public GraphData getGraphData(String graphId, String token) throws Exception;

    public Map<String, List<GraphData>> getAllGraphData(String token) throws Exception;

    public void delete(String id, String token) throws Exception;
}
