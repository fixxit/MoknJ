package nl.it.fixx.moknj.bal.core.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nl.it.fixx.moknj.bal.BalBase;
import nl.it.fixx.moknj.bal.core.menu.MenuCoreBal;
import nl.it.fixx.moknj.bal.core.template.TemplateCoreBal;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphType;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphData;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.exception.BalException;
import nl.it.fixx.moknj.repository.GraphRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author adriaan
 */
@Service
public class GraphCoreBalImpl extends BalBase<GraphRepository> implements GraphCoreBal {

    private static final Logger LOG = LoggerFactory.getLogger(GraphCoreBalImpl.class);

    private final GraphDisplayBal graphBuilder;
    private final UserCoreBal userBal;
    private final TemplateCoreBal templateBal;
    private final MenuCoreBal menuBal;

    @Autowired
    public GraphCoreBalImpl(GraphDisplayBal graphBuilder, UserCoreBal userBal, TemplateCoreBal templateBal, MenuCoreBal menuBal, GraphRepository repository) {
        super(repository);
        this.graphBuilder = graphBuilder;
        this.userBal = userBal;
        this.templateBal = templateBal;
        this.menuBal = menuBal;
    }

    /**
     * Saves the graph... Sets the creator id by the token. Only a user with
     * correct token can access this graph.
     *
     * @param payload
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public Graph save(Graph payload, String token) throws Exception {
        try {
            // For updates if the type has a id then bypass the exists
            boolean bypassExists = false;
            if (payload.getId() != null && !payload.getId().trim().isEmpty()) {
                bypassExists = true;
                Graph dbGraph = repository.findOne(payload.getId());
                payload.setCreatorId(dbGraph.getCreatorId());
            } else {
                User user = userBal.getUserByToken(token);
                if (user != null && user.isSystemUser()) {
                    payload.setCreatorId(user.getId());
                }
            }

            if (!repository.existsByName(payload.getName()) || bypassExists) {
                Graph graph = repository.save(payload);
                return graph;
            } else {
                throw new BalException("Graph with the name " + payload.getName() + " exists");
            }
        } catch (BalException e) {
            LOG.error("Could not save the graph setup", e);
            throw e;
        }
    }

    /**
     * Gets all the graphs templates saved, this is used for the edit screen.
     * filtered by user access token.
     *
     * @param token
     * @return list of graphs
     * @throws java.lang.Exception
     */
    @Override
    public List<Graph> getAll(String token) throws Exception {
        try {
            User user = userBal.getUserByToken(token);
            Set<Graph> graphs = new HashSet();
            if (user != null) {
                List<Graph> savedGraphs = repository.findAll();
                for (Graph graph : savedGraphs) {
                    // check if user has access to view and edit this graph template
                    if (user.getId().equals(graph.getCreatorId())) {
                        if (graph.getTemplateId() != null) {
                            Template template = templateBal.getTemplateById(graph.getTemplateId());
                            if (template != null) {
                                graph.setTemplate(template.getName());
                            }
                        }

                        graph.setType(graph.getGraphType().getName());
                        graph.setView(graph.getGraphView().getDisplayName());

                        Menu menu = menuBal.getMenuById(graph.getMenuId());
                        if (menu != null) {
                            graph.setMenu(menu.getName());
                        }
                        graphs.add(graph);
                    }
                }
            }
            return graphs.stream().collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Could not get all saved graph templates", e);
            throw e;
        }
    }

    /**
     * Get the graph data for user, this graph is checked against the creator id
     * which is the user id. The user id is determined from the access token.
     *
     * @param graphId
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public GraphData getGraphData(String graphId, String token) throws Exception {
        try {
            User user = userBal.getUserByToken(token);
            Graph graph = repository.findOne(graphId);
            if (user.getId().equals(graph.getCreatorId())) {
                return graphBuilder.buildGraphData(graph, token);
            }
        } catch (Exception e) {
            LOG.error("Could no get graph data", e);
            throw e;
        }
        return null;
    }

    /**
     * Gets all graphs created by this user. The user id is determined from the
     * access toke
     *
     * @param token
     * @return list of graph data
     * @throws Exception
     */
    @Override
    public Map<String, List<GraphData>> getAllGraphData(String token) throws Exception {
        try {
            Map<String, List<GraphData>> allDataSets = new HashMap<>();
            for (Graph graph : getAll(token)) {
                GraphData data = getGraphData(graph.getId(), token);
                if (data != null) {
                    GlobalGraphType type = graph.getGraphType();
                    List<GraphData> dataSet = allDataSets.get(type.getProperty());
                    if (dataSet == null) {
                        dataSet = new ArrayList<>();
                    }

                    dataSet.add(data);
                    allDataSets.put(type.getProperty(), dataSet);
                }
            }
            return allDataSets;
        } catch (Exception e) {
            LOG.error("Could no get all the user graphs data", e);
            throw e;
        }
    }

    /**
     * Deletes the graph only if the user has access to it. Which he is the
     * creator.
     *
     * @param id
     * @param token
     * @throws Exception
     */
    @Override
    public void delete(String id, String token) throws Exception {
        try {
            if (repository.exists(id)) {
                Graph graph = repository.findOne(id);
                User user = userBal.getUserByToken(token);
                if (graph.getCreatorId().equals(user.getId())
                        || user.getAuthorities().contains(ALL_ACCESS.toString())) {
                    repository.delete(id);
                } else {
                    throw new BalException("Delete failed. "
                            + "This user is not the creator"
                            + " of this graph template.");
                }
            } else {
                throw new BalException("No graph by id[" + id + "] exists");
            }
        } catch (BalException e) {
            LOG.error("Could not delete this graph[" + id + "]", e);
            throw e;
        }
    }
}
