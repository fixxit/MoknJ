/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.domain.core.graph;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.it.fixx.moknj.bal.AssetBal;
import nl.it.fixx.moknj.bal.EmployeeBal;
import nl.it.fixx.moknj.bal.EntityBal;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.global.GlobalMenuType;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.core.util.DateUtil;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.employee.Employee;
import nl.it.fixx.moknj.repository.AssetRepository;
import nl.it.fixx.moknj.repository.EmployeeRepository;
import nl.it.fixx.moknj.repository.MenuRepository;
import nl.it.fixx.moknj.repository.TemplateRepository;
import nl.it.fixx.moknj.repository.UserRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author adriaan
 */
public class GraphBuilder {

    private final AssetRepository assetRep;
    private final MenuRepository menuRep;
    private final TemplateRepository templateRep;
    private final EmployeeRepository employeeRep;
    private final UserRepository userRep;

    public GraphBuilder(AssetRepository assetRep, MenuRepository menuRep, TemplateRepository templateRep, EmployeeRepository employeeRep, UserRepository userRep) {
        this.assetRep = assetRep;
        this.menuRep = menuRep;
        this.templateRep = templateRep;
        this.employeeRep = employeeRep;
        this.userRep = userRep;
    }

    private final String FMT_CREATED_DATE = "yyyy-MM-dd HH:mm";
    private final String FMT_FILTER_DATE = "yyyy-MM-dd";

    public GraphData generateGraph(Graph graphInfo) throws Exception {

        GraphData data = new GraphData();
        // get all records for template.
        if (graphInfo != null) {
//            System.out.println("======================================================");
//            System.out.println("Graph Name : " + graphInfo.getName());
            // Business access layer.
            EntityBal bal = null;
            Menu menu = menuRep.findOne(graphInfo.getMenuId());
            Template template = templateRep.findOne(graphInfo.getTemplateId());

            if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                bal = new AssetBal(assetRep, menuRep);
            } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                bal = new EmployeeBal(employeeRep, menuRep);
            }
            /**
             * below is where the date logic is generated or x - axis focus
             * labels. This is where you will start seeing dragons beware of
             * purple!
             */
            // X-AXIS Label Logic
            if (bal != null) {
                List records = bal.getAll(graphInfo.getTemplateId(), graphInfo.getMenuId());
                DateTime datetime = DateUtil.parseJavaScriptDateTime(graphInfo.getGraphDate());
                List<String> xAxisLabels = new ArrayList<>();

                boolean xAxisSwapped = false;

                if (null != graphInfo.getGraphView()) {
                    switch (graphInfo.getGraphView()) {
                        case GBL_MTMTFY:
                            xAxisLabels = DateUtil.geMonthsForYear(datetime.getYear());
                            break;
                        case GBL_MTMFD:
                            xAxisLabels = DateUtil.geMonthsForDate(datetime);
                            break;
                        case GBL_DOWFY:
                        case GBL_DOWFM:
                            xAxisLabels = DateUtil.getDaysOfWeek(datetime);
                            break;
                        case GBL_SMTEOM:
                        case GBL_SYTEOY:
                        case GBL_SYTD:
                        case GBL_OFTD:
                            // SWOPS THE data set for x axis labels
                            getXAxisLabels(xAxisLabels, graphInfo, template);
                            xAxisSwapped = true;
                            break;
                        default:
                            break;
                    }
                }

                Set<String> yAxisLabels = new HashSet<>();

                if (null != graphInfo.getGraphFocus()) {
                    switch (graphInfo.getGraphFocus()) {
                        case GBL_FOCUS_CREATED_BY:
                            for (User user : userRep.findAll()) {
                                if (user.isSystemUser()) {
                                    yAxisLabels.add(user.getUserName());
                                }
                            }
                            break;
                        case GBL_FOCUS_FREE_FIELD:
                            if (graphInfo.getFreefieldId() != null) {
                                for (FieldDetail field : template.getDetails()) {
                                    if (field.getId().equals(graphInfo.getFreefieldId())) {
                                        String drpValue = field.getName();
                                        int n = drpValue.indexOf(":");
                                        String name = drpValue.substring(0, n - 1);
                                        String array = drpValue.substring(n + 1, drpValue.length());
                                        String jsonStr = "{\"" + name + "\":" + array + "}";
                                        JSONObject json = (JSONObject) JSONValue.parse(jsonStr);
                                        List<String> drpValues = (List<String>) json.get(name);
                                        for (String value : drpValues) {
                                            yAxisLabels.add(value);
                                        }
                                        break;
                                    }
                                }
                            }
                            break;
                        case GBL_FOCUS_IN_AND_OUT:
                            yAxisLabels.add("IN");
                            yAxisLabels.add("Out");
                            break;
                        case GBL_FOCUS_DEFAULT:
                            if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                                yAxisLabels.add("Assets");
                            } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                                yAxisLabels.add("Employee");
                            }
                            break;
                        default:
                            break;
                    }
                }

                if (xAxisSwapped) {
                    yAxisLabels = new HashSet<>();
                    if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                        yAxisLabels.add("Assets");
                    } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                        yAxisLabels.add("Employee");
                    }
                }

                int[][] yxData = new int[yAxisLabels.size()][xAxisLabels.size()];
                String[] yAxis = yAxisLabels.toArray(new String[yAxisLabels.size()]);
                String[] xAxis = xAxisLabels.toArray(new String[xAxisLabels.size()]);

                for (Object record : records) {
                    if (record instanceof Employee) {

                        Employee employee = (Employee) record;
                        String yAxisValue = null;
                        if (null != graphInfo.getGraphFocus()) {
                            switch (graphInfo.getGraphFocus()) {
                                case GBL_FOCUS_CREATED_BY:
                                    String createdBy = employee.getCreatedBy();
                                    yAxisValue = createdBy;
                                    break;
                                case GBL_FOCUS_FREE_FIELD:
                                    for (FieldValue field : employee.getDetails()) {
                                        if (field.getId().equals(graphInfo.getFreefieldId())) {
                                            yAxisValue = field.getValue();
                                            break;
                                        }
                                    }
                                    break;
                                case GBL_FOCUS_DEFAULT:
                                default:
                                    yAxisValue = "Employee";
                                    break;
                            }
                        }

                        if (yAxisValue == null) {
                            // if for some reason the y axis is null set default.
                            yAxisValue = "Employee";
                        }

                        DateTime createdDate = DateUtil.parseDate(employee.getCreatedDate(), FMT_CREATED_DATE);
                        String strDate = new SimpleDateFormat(FMT_FILTER_DATE).format(createdDate.toDate());
                        DateTime filterDate = DateUtil.parseDate(strDate, FMT_FILTER_DATE);
                        LocalDate local = filterDate.toLocalDate();

                        String xAxisValue = "";
                        if (null != graphInfo.getGraphView()) {
                            switch (graphInfo.getGraphView()) {
                                case GBL_MTMTFY:
                                case GBL_MTMFD:
                                    xAxisValue = local.toString("MMM");
                                    break;
                                case GBL_DOWFY:
                                case GBL_DOWFM:
                                    xAxisValue = local.toString("EEE");
                                    break;
                                default:
                                    xAxisValue = yAxisValue;
                                    yAxisValue = "Employee";
                                    break;
                            }
                        }

                        for (int y = 0; y < yAxis.length; y++) {
                            String yAxisFocus = yAxis[y];
//                            System.out.println("yAxisFocus : " + yAxisFocus);
//                            System.out.println("yAxisValue : " + yAxisValue);
                            if (yAxisFocus.equals(yAxisValue)) {
                                int[] dataSet = yxData[y];
                                for (int x = 0; x < xAxis.length; x++) {
                                    String xAxisFocus = xAxis[x];
                                    if (xAxisFocus.equals(xAxisValue)) {
                                        dataSet[x] += 1;
                                        yxData[y] = dataSet;
                                    }
                                }
                            }
                        }

                    } else if (record instanceof Asset) {
                        Asset asset = (Asset) record;

                    }
                }

                // Set grpah data for page display
                data.setGraphTitle(graphInfo.getName());
                data.setGraphData(yxData);
                data.setXlabels(xAxis);
                data.setYlabels(yAxis);
                return data;

            }
        }

        return data;
    }

    private void getXAxisLabels(List<String> xAxis, Graph graphInfo, Template temp) {
        if (null != graphInfo.getGraphFocus()) {
            switch (graphInfo.getGraphFocus()) {
                case GBL_FOCUS_CREATED_BY:
                    userRep.findAll().stream().filter((user) -> (user.isSystemUser())).forEach((user) -> {
                        xAxis.add(user.getUserName());
                    });
                    break;
                case GBL_FOCUS_IN_AND_OUT:
                    xAxis.add("IN");
                    xAxis.add("OUT");
                    break;
                case GBL_FOCUS_FREE_FIELD:
                    if (graphInfo.getFreefieldId() != null) {
                        for (FieldDetail field : temp.getDetails()) {
                            if (field.getId().equals(graphInfo.getFreefieldId())) {
                                String drpValue = field.getName();
                                int n = drpValue.indexOf(":");
                                String name = drpValue.substring(0, n - 1);
                                String array = drpValue.substring(n + 1, drpValue.length());
                                String jsonStr = "{\"" + name + "\":" + array + "}";
                                JSONObject json = (JSONObject) JSONValue.parse(jsonStr);
                                List<String> list = (List<String>) json.get(name);
                                xAxis.addAll(list);
                                break;
                            }
                        }
                    }
                    break;
                case GBL_FOCUS_DEFAULT:
                    Menu menu = menuRep.findOne(graphInfo.getMenuId());
                    if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                        xAxis.add("Assets");
                    } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                        xAxis.add("Employee");
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
