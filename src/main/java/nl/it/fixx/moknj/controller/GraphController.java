package nl.it.fixx.moknj.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.it.fixx.moknj.bal.core.graph.GraphBal;
import nl.it.fixx.moknj.bal.core.template.TemplateCoreBal;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphDate;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphFocus;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphType;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphView;
import nl.it.fixx.moknj.domain.core.global.GlobalTemplateType;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphDate;
import nl.it.fixx.moknj.domain.core.graph.GraphFocus;
import nl.it.fixx.moknj.domain.core.graph.GraphType;
import nl.it.fixx.moknj.domain.core.graph.GraphView;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.response.GraphResponse;
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
    private GraphBal graphBal;
    @Autowired
    private TemplateCoreBal templateBal;

    /**
     * Saves the graph... Sets the creator id by the token. Only a user with
     * correct token can access this graph.
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
        try {
            response.setGraphTemplate(graphBal.save(payload, access_token));
            response.setSuccess(true);
            response.setMessage("Saved graph successfully");

        } catch (Exception ex) {
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
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/datetypes/{id}", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getDateTypes(@PathVariable String id) throws Exception {
        GraphResponse response = new GraphResponse();
        GlobalGraphDate[] types = GlobalGraphDate.values();
        Template template = templateBal.getTemplateById(id);
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
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/focus/{id}", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getFocuses(@PathVariable String id) throws Exception {
        GraphResponse response = new GraphResponse();
        GlobalGraphFocus[] graphFocuses = GlobalGraphFocus.values();
        Template template = templateBal.getTemplateById(id);
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
     */
    @RequestMapping(value = "/all/saved", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getAllSavedGraphs(@RequestParam String access_token) {
        GraphResponse response = new GraphResponse();
        try {
            response.setSavedGraphs(graphBal.getAll(access_token));
            response.setSuccess(true);
            response.setMessage("found all user graph templates");
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    /**
     * Gets all the graphs data. Used for display of graphs.
     *
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/all/data", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse getAllGraphsData(@RequestParam String access_token) {
        GraphResponse response = new GraphResponse();
        try {
            response.setAllGraphsData(graphBal.getAllGraphData(access_token));
            response.setSuccess(true);
            response.setMessage("found all user graphs data");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
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
    GraphResponse getGraphData(@PathVariable String id,
            @RequestParam String access_token) throws Exception {
        GraphResponse response = new GraphResponse();
        try {
            response.setGraphData(graphBal.getGraphData(id, access_token));
            response.setSuccess(true);
            response.setMessage("Graph data found");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * To do add delete method for getGlobalFields.
     *
     * @param id
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public @ResponseBody
    GraphResponse delete(@PathVariable String id, @RequestParam String access_token) {
        GraphResponse response = new GraphResponse();
        try {
            graphBal.delete(id, access_token);
            response.setSuccess(true);
            response.setMessage("Graph successfully deleted from system");
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

}
