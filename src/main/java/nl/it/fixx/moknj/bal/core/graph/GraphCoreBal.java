package nl.it.fixx.moknj.bal.core.graph;

import java.util.List;
import java.util.Map;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphData;

public interface GraphCoreBal {

    public Graph save(Graph payload, String token);

    public List<Graph> getAll(String token);

    public GraphData getGraphData(String graphId, String token);

    public Map<String, List<GraphData>> getAllGraphData(String token);

    public void delete(String id, String token);
}
