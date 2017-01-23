package nl.it.fixx.moknj.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.it.fixx.moknj.builders.GraphBuilder;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphDate;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphFocus;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphType;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphView;
import nl.it.fixx.moknj.domain.core.global.GlobalTemplateType;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphData;
import nl.it.fixx.moknj.domain.core.graph.GraphDate;
import nl.it.fixx.moknj.domain.core.graph.GraphFocus;
import nl.it.fixx.moknj.domain.core.graph.GraphType;
import nl.it.fixx.moknj.domain.core.graph.GraphView;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import static nl.it.fixx.moknj.domain.core.user.UserAuthority.ALL_ACCESS;
import nl.it.fixx.moknj.repository.AssetLinkRepository;
import nl.it.fixx.moknj.repository.AssetRepository;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import nl.it.fixx.moknj.repository.GraphRepository;
import nl.it.fixx.moknj.repository.MenuRepository;
import nl.it.fixx.moknj.repository.TemplateRepository;
import nl.it.fixx.moknj.repository.UserRepository;
import nl.it.fixx.moknj.response.GraphResponse;
import nl.it.fixx.moknj.security.OAuth2SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adriaan
 */
@CrossOrigin // added for cors, allow access from another web server
@RestController
@RequestMapping(value = "/graph")
public class GraphController {

    @Autowired
    private AssetRepository assetRep;
    @Autowired
    private MenuRepository menuRep;
    @Autowired
    private TemplateRepository templateRep;
    @Autowired
    private GraphRepository graphRep;
    @Autowired
    private EmployeeRepository employeeRep;
    @Autowired
    private UserRepository userRep;
    @Autowired
    private AssetLinkRepository auditRep;

    /**
     *
     * @param payload
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse add(@RequestBody Graph payload,
            @RequestParam String access_token) {
        GraphResponse response = new GraphResponse();
        response.setAction("POST");
        response.setMethod("/graph/add");

        if (payload.getName() == null && payload.getName().isEmpty()) {
            response.setMessage("TO DO!!");//todo
            return response;
        }

        try {
            // For updates if the type has a id then bypass the exists
            boolean bypassExists = false;
            if (payload.getId() != null && !payload.getId().trim().isEmpty()) {
                bypassExists = true;
                Graph dbGraph = graphRep.findOne(payload.getId());
                payload.setCreatorId(dbGraph.getCreatorId());
            } else {
                User user = userRep.findByUserName(OAuth2SecurityConfig.getUserForToken(access_token));
                if (user != null && user.isSystemUser()) {
                    payload.setCreatorId(user.getId());
                }
            }

            boolean exists = graphRep.existsByName(payload.getName());
            if (!exists || bypassExists) {
                Graph graphTemplate = graphRep.save(payload);
                response.setSuccess(graphTemplate != null);
                response.setMessage("Saved graph[" + graphTemplate.getId() + "]");
                response.setGraphTemplate(graphTemplate);
            } else {
                response.setSuccess(false);
                response.setMessage("graph by name " + payload.getName() + " exists");
            }
        } catch (IllegalArgumentException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    /**
     * Gets all the modules allowed for menu item
     *
     * @return
     */
    @RequestMapping(value = "/types", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getTypes() {
        GraphResponse response = new GraphResponse();
        GlobalGraphType[] moduleTypes = GlobalGraphType.values();
        List<GraphType> menuTypes = new ArrayList<>();
        for (GlobalGraphType type : moduleTypes) {
            menuTypes.add(new GraphType(type.getName(), type.getProperty(), type.name()));
        }
        response.setGraphTypes(menuTypes);
        return response;
    }

    /**
     * Gets all the modules allowed for menu item
     *
     * @return
     */
    @RequestMapping(value = "/views", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getViews() {
        GraphResponse response = new GraphResponse();
        GlobalGraphView[] moduleTypes = GlobalGraphView.values();
        List<GraphView> menuTypes = new ArrayList<>();
        for (GlobalGraphView type : moduleTypes) {
            menuTypes.add(new GraphView(type.getDisplayName(), type.name()));
        }
        response.setGraphViews(menuTypes);
        return response;
    }

    /**
     * Gets all the modules allowed for menu item
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/datetypes/{id}", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getDateTypes(@PathVariable String id) {
        GraphResponse response = new GraphResponse();
        GlobalGraphDate[] types = GlobalGraphDate.values();
        Template template = templateRep.findOne(id);
        if (template != null) {
            List<GraphDate> graphDateOptions = new ArrayList<>();
            for (GlobalGraphDate type : types) {
                List<GlobalTemplateType> templates = Arrays.asList(type.getTemplates());
                if (templates.contains(template.getTemplateType())) {
                    graphDateOptions.add(new GraphDate(type.displayName(), type.name()));
                }
            }
            response.setGraphDates(graphDateOptions);
        }

        return response;
    }

    /**
     * Gets all the focuses this is filter by template id.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/focus/{id}", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getFocuses(@PathVariable String id) {
        GraphResponse response = new GraphResponse();
        GlobalGraphFocus[] graphFocuses = GlobalGraphFocus.values();
        Template template = templateRep.findOne(id);
        if (template != null) {
            List<GraphFocus> graphFocusOptions = new ArrayList<>();
            for (GlobalGraphFocus type : graphFocuses) {
                List<GlobalTemplateType> templates = Arrays.asList(type.getTemplates());
                if (templates.contains(template.getTemplateType())) {
                    graphFocusOptions.add(new GraphFocus(type.name(), type.displayName()));
                }
            }
            response.setGraphFocuses(graphFocusOptions);
        }
        return response;
    }

    /**
     * Gets all the modules allowed for menu item
     *
     * @param access_token
     * @return
     * @throws java.text.ParseException
     */
    @RequestMapping(value = "/all/saved", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getAllSavedGraphs(@RequestParam String access_token) throws ParseException {
        GraphResponse response = new GraphResponse();
        User user = userRep.findByUserName(OAuth2SecurityConfig.getUserForToken(access_token));
        List<Graph> graphs = graphRep.findAll();
        if (user != null) {
            for (Graph graph : graphs) {
                // check if user has access to edit the graph
                if (user.getId().equals(graph.getCreatorId())
                        || user.getAuthorities().contains(ALL_ACCESS.toString())) {
                    if (graph.getTemplateId() != null) {
                        Template template = templateRep.findOne(graph.getTemplateId());
                        if (template != null) {
                            graph.setTemplate(template.getName());
                        }
                    }

                    graph.setType(graph.getGraphType().getName());
                    graph.setView(graph.getGraphView().getDisplayName());

                    Menu menu = menuRep.findOne(graph.getMenuId());
                    if (menu != null) {
                        graph.setMenu(menu.getName());
                    }
                }
            }
        }

        response.setSavedGraphs(graphs);
        return response;
    }

    /**
     * Gets all the graphs data. Used for display of graphs.
     *
     * @param access_token
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/all/data", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getAllGraphsData(@RequestParam String access_token) throws Exception {
        GraphResponse response = new GraphResponse();
        Map<String, List<GraphData>> allDataSets = new HashMap<>();
        GraphBuilder builder = new GraphBuilder(assetRep, auditRep, menuRep, templateRep, employeeRep, userRep);
        User user = userRep.findByUserName(OAuth2SecurityConfig.getUserForToken(access_token));
        List<Graph> graphSetup = graphRep.findAll();

        System.out.println("user.getAuthorities() : " + user.getAuthorities());

        for (Graph graph : graphSetup) {
            System.out.println("graph.getAccessUserIds() : " + graph.getAccessUserIds());
            System.out.println("graph.getCreatorId() : " + graph.getCreatorId());
            System.out.println("user.getId() : " + graph.getId());
            if (user.getId().equals(graph.getCreatorId())
                    || (user.getAuthorities() != null
                    && user.getAuthorities().contains(ALL_ACCESS.toString()))
                    || (graph.getAccessUserIds() != null
                    && graph.getAccessUserIds().contains(user.getId()))) {

                GraphData data = builder.buildGraphData(graph);
                GlobalGraphType type = graph.getGraphType();

                List<GraphData> dataSet = allDataSets.get(type.getProperty());
                if (dataSet == null) {
                    dataSet = new ArrayList<>();
                }

                dataSet.add(data);
                allDataSets.put(type.getProperty(), dataSet);
            }
        }

        response.setAllGraphsData(allDataSets);
        return response;
    }

    /**
     * Gets all the graphs data. Used for display of graphs. todo!
     *
     * @param id
     * @param access_token
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/get/data/{id}", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getGraphData(@PathVariable String id, @RequestParam String access_token) throws Exception {
        GraphResponse response = new GraphResponse();
        User user = userRep.findByUserName(OAuth2SecurityConfig.getUserForToken(access_token));
        Graph graph = graphRep.findOne(id);
        if (user.getId().equals(graph.getCreatorId())
                || (user.getAuthorities() != null
                && user.getAuthorities().contains(ALL_ACCESS.name()))
                || (graph.getAccessUserIds() != null
                && graph.getAccessUserIds().contains(user.getId()))) {
            GraphBuilder builder = new GraphBuilder(assetRep, auditRep, menuRep, templateRep, employeeRep, userRep);
            response.setGraphData(builder.buildGraphData(graph));
        }
        return response;
    }

    /**
     * To do add delete method for getGlobalFields.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse delete(@PathVariable String id) {
        GraphResponse response = new GraphResponse();
        Graph graph = graphRep.findOne(id);
        try {
            graphRep.delete(graph);
            response.setSuccess(true);
            response.setMessage("Graph [" + graph.getName() + "] removed from home dashboard");
        } catch (IllegalArgumentException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

}
