package nl.it.fixx.moknj.builders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.it.fixx.moknj.bal.AssetBal;
import nl.it.fixx.moknj.bal.EmployeeBal;
import nl.it.fixx.moknj.bal.LinkBal;
import nl.it.fixx.moknj.bal.MainAccessBal;
import nl.it.fixx.moknj.bal.RecordBal;
import nl.it.fixx.moknj.bal.UserBal;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphDate;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphView;
import nl.it.fixx.moknj.domain.core.global.GlobalMenuType;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphData;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.service.SystemContext;
import nl.it.fixx.moknj.util.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to group the graph data for display in the UI
 *
 * @author adriaan
 */
public class GraphBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(GraphBuilder.class);

    private final String token;
    private final SystemContext context;

    private DateTime endDate;
    private DateTime startDate;

    private static final String MDL_ASSET_DEFAULT = "Asset";
    private static final String MDL_EMPLOYEE_DEFAULT = "Employee";
    private static final String MDL_ASSET_STATUS_IN = "In";
    private static final String MDL_ASSET_STATUS_OUT = "Out";

    public GraphBuilder(SystemContext context, String token) {
        this.context = context;
        this.token = token;
    }

    private final String FMT_CREATED_DATE = "yyyy-MM-dd HH:mm";
    private final String FMT_FILTER_DATE = "yyyy-MM-dd";
    private final String FMT_MONTH_NAME = "MMM";
    private final String FMT_DAY_NAME = "EEE";

    /**
     * Generates the graph data!
     *
     * @param graphInfo
     * @return
     * @throws Exception
     */
    public GraphData buildGraphData(Graph graphInfo) throws Exception {
        try {
            GraphData data = new GraphData();
            // get all records for template.
            if (graphInfo != null) {
//                LOG.info("======================================================");
//                LOG.info("Graph Name : " + graphInfo.getName());
                // Business access layer.
                RecordBal recordBal = null;
                Menu menu = new MainAccessBal(context).getMenu(graphInfo.getMenuId(), token);
                for (Template template : menu.getTemplates()) {
                    if (template.getId().equals(graphInfo.getTemplateId())) {
                        if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                            recordBal = new AssetBal(context);
                        } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                            recordBal = new EmployeeBal(context);
                        }
                        /**
                         * below is where the date logic is generated or x -
                         * axis focus labels. This is where you will start
                         * seeing dragons beware of purple!
                         */
                        // X-AXIS Label Logic
                        if (recordBal != null) {
                            List records = recordBal.getAll(template.getId(), menu.getId(), token);

                            // duplicate records for all entries which have the same id.
                            // Basically a left join...
                            if (GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {

                                List<Asset> inOutAssetJoinList = new ArrayList<>();
                                for (Object record : records) {
                                    if (record instanceof Asset) {
                                        Asset asset = (Asset) record;
                                        // gets all the checked in/out records for asset.
                                        List<AssetLink> links
                                                = new LinkBal(context).
                                                        getAllAssetLinksByAssetId(
                                                                asset.getId(),
                                                                menu.getId(),
                                                                template.getId(),
                                                                token);

                                        links.forEach((link) -> {
                                            // create new instance of asset... prototype pattern would be nice here...
                                            Asset newAsset = new Asset();
                                            if (link.getAssetId().equals(asset.getId())) {
                                                DateTime date = DateUtil.parseJavaScriptDateTime(link.getDate());
                                                String freeDate = new SimpleDateFormat(
                                                        FMT_CREATED_DATE
                                                ).format(date.plusDays(1).toDate());
                                                newAsset.setFreeDate(freeDate);
                                                newAsset.setFreeValue(link.isChecked() ? MDL_ASSET_STATUS_OUT : MDL_ASSET_STATUS_IN);
                                                newAsset.setCreatedBy(asset.getCreatedBy());
                                                newAsset.setCreatedDate(asset.getCreatedDate());
                                                newAsset.setDetails(asset.getDetails());
                                                newAsset.setLastModifiedBy(asset.getLastModifiedBy());
                                                newAsset.setLastModifiedDate(asset.getLastModifiedDate());
                                                newAsset.setMenuScopeIds(asset.getMenuScopeIds());
                                                newAsset.setResourceId(asset.getResourceId());
                                                newAsset.setTypeId(asset.getTypeId());
                                                inOutAssetJoinList.add(newAsset);
                                            }
                                        });
                                    }
                                }
                                records = inOutAssetJoinList;
                            }

                            // This is used to bypass the filterdate and filter rule logic
                            boolean bypassDateRulle = false;
                            if (GlobalGraphDate.GBL_FOCUS_NO_DATE_RULE.equals(graphInfo.getGraphDateType())) {
                                if (!GlobalGraphView.GBL_OFTD.equals(graphInfo.getGraphView())) {
                                    bypassDateRulle = true;
                                }
                            }

                            if (!bypassDateRulle) {
                                endDate = DateUtil.parseJavaScriptDateTime(graphInfo.getGraphDate());
                                startDate = new DateTime();
                                //javascript is one day off todo with date type UCT
                                endDate = endDate.plusDays(1);
                            } else {
                                startDate = new DateTime();
                                endDate = new DateTime();
                            }

                            List<String> xAxisLabels = new ArrayList<>();
                            boolean xAxisSwapped = false;

                            if (null != graphInfo.getGraphView()) {
                                switch (graphInfo.getGraphView()) {
                                    case GBL_MTMTFY: {
                                        xAxisLabels = DateUtil.geMonthsForYear(endDate.getYear());
                                        initialiseYearDates();
                                    }
                                    break;
                                    case GBL_MTMFD: {
                                        xAxisLabels = DateUtil.geMonthsForDate(endDate);
                                        initialiseMonthDates();
                                    }
                                    break;
                                    case GBL_DOWFY: {
                                        xAxisLabels = DateUtil.getDaysOfWeek(endDate);
                                        initialiseYearDates();
                                    }
                                    break;
                                    case GBL_DOWFM: {
                                        xAxisLabels = DateUtil.getDaysOfWeek(endDate);
                                        initialiseMonthDates();
                                    }
                                    break;
                                    case GBL_OFTD:
                                        DateTime today = new DateTime();
                                        xAxisLabels.add(today.toLocalDate().toString(FMT_FILTER_DATE));
                                        // Set end and start date to today/current date
                                        endDate = today;
                                        startDate = today;
                                        break;
                                    case GBL_SMTEOM: {
                                        getXAxisLabels(xAxisLabels, graphInfo, template);
                                        xAxisSwapped = true;
                                        initialiseMonthDates();
                                    }
                                    break;
                                    case GBL_SYTEOY: {
                                        getXAxisLabels(xAxisLabels, graphInfo, template);
                                        xAxisSwapped = true;
                                        initialiseYearDates();
                                    }
                                    break;
                                    case GBL_SYTD: {
                                        // SWOPS THE data set for x axis labels
                                        getXAxisLabels(xAxisLabels, graphInfo, template);
                                        xAxisSwapped = true;

                                        String startDateStr = endDate.getYear() + "-01-01";
                                        LocalDate localstratDate = new LocalDate(startDateStr);
                                        startDate = localstratDate.toDateTimeAtStartOfDay();
                                    }
                                    break;
                                    default:
                                        break;
                                }
                            }

//                            LOG.info("Type : " + graphInfo.getGraphView().getDisplayName());
//                            LOG.info("End Date : " + endDate.toString(FMT_FILTER_DATE));
//                            LOG.info("Start Date : " + startDate.toString(FMT_FILTER_DATE));
                            String endDateStr = new SimpleDateFormat(FMT_FILTER_DATE).format(endDate.toDate());
                            DateTime endDateRule = DateUtil.parseDate(endDateStr, FMT_FILTER_DATE);

                            String startDateStr = new SimpleDateFormat(FMT_FILTER_DATE).format(startDate.toDate());
                            DateTime startDateRule = DateUtil.parseDate(startDateStr, FMT_FILTER_DATE);

                            Set<String> yAxisLabels = new HashSet<>();

                            if (null != graphInfo.getGraphFocus()) {
                                switch (graphInfo.getGraphFocus()) {
                                    case GBL_FOCUS_CREATED_BY:
                                        for (User user : new UserBal(context).getAll()) {
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
                                                    if (GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                                                        for (String value : drpValues) {
                                                            yAxisLabels.add(value + "-" + MDL_ASSET_STATUS_IN);
                                                            yAxisLabels.add(value + "-" + MDL_ASSET_STATUS_OUT);
                                                        }
                                                    } else {
                                                        for (String value : drpValues) {
                                                            yAxisLabels.add(value);
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    case GBL_FOCUS_IN_AND_OUT:
                                        yAxisLabels.add(MDL_ASSET_STATUS_IN);
                                        yAxisLabels.add(MDL_ASSET_STATUS_OUT);
                                        break;
                                    case GBL_FOCUS_DEFAULT:
                                        if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                                            yAxisLabels.add(MDL_ASSET_DEFAULT);
                                        } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                                            yAxisLabels.add(MDL_EMPLOYEE_DEFAULT);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }

                            if (xAxisSwapped) {
                                yAxisLabels = new HashSet<>();
                                if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                                    yAxisLabels.add(MDL_ASSET_DEFAULT);
                                } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                                    yAxisLabels.add(MDL_EMPLOYEE_DEFAULT);
                                }
                            }

                            int[][] yxData = new int[yAxisLabels.size()][xAxisLabels.size()];
                            String[] yAxis = yAxisLabels.toArray(new String[yAxisLabels.size()]);
                            String[] xAxis = xAxisLabels.toArray(new String[xAxisLabels.size()]);

                            for (Object record : records) {
                                Record recodValue = (Record) record;

                                // y- axis value should come here
                                String yAxisValue = null;
                                if (null != graphInfo.getGraphFocus()) {
                                    switch (graphInfo.getGraphFocus()) {
                                        case GBL_FOCUS_CREATED_BY:
                                            String createdBy = recodValue.getCreatedBy();
                                            yAxisValue = createdBy;
                                            break;
                                        case GBL_FOCUS_FREE_FIELD:
                                            for (FieldValue field : recodValue.getDetails()) {
                                                if (field.getId().equals(graphInfo.getFreefieldId())) {
                                                    String value = field.getValue();
                                                    if (GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                                                        if (MDL_ASSET_STATUS_IN.equals(recodValue.getFreeValue())) {
                                                            yAxisValue = value + "-" + MDL_ASSET_STATUS_IN;
                                                        } else if (MDL_ASSET_STATUS_OUT.equals(recodValue.getFreeValue())) {
                                                            yAxisValue = value + "-" + MDL_ASSET_STATUS_OUT;
                                                        }
                                                    } else {
                                                        yAxisValue = value;
                                                    }

                                                    break;
                                                }
                                            }
                                            break;
                                        case GBL_FOCUS_IN_AND_OUT:
                                            // use record object as value (asset) lost its
                                            // asset fields in casting.
                                            if (record instanceof Asset) {
                                                Asset asset = (Asset) record;
                                                // null check for assignment
                                                // null == in
                                                // not null == out
                                                if (GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                                                    yAxisValue = asset.getFreeValue();
                                                } else if (!GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                                                    if (asset.getResourceId() != null
                                                            && !asset.getResourceId().trim().isEmpty()) {
                                                        yAxisValue = MDL_ASSET_STATUS_OUT;
                                                    } else {
                                                        yAxisValue = MDL_ASSET_STATUS_IN;
                                                    }
                                                }
                                            }
                                            break;
                                        case GBL_FOCUS_DEFAULT:
                                        default:
                                            if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                                                yAxisValue = MDL_ASSET_DEFAULT;
                                            } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                                                yAxisValue = MDL_EMPLOYEE_DEFAULT;
                                            }
                                            break;
                                    }
                                }

                                if (yAxisValue == null) {
                                    if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                                        yAxisValue = MDL_ASSET_DEFAULT;
                                    } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                                        yAxisValue = MDL_EMPLOYEE_DEFAULT;
                                    }
                                }

                                // Calculate date filter
                                DateTime recodDateTime = new DateTime();
                                if (null != graphInfo.getGraphDateType()) {
                                    switch (graphInfo.getGraphDateType()) {
                                        case GBL_FOCUS_LAST_MODIFIED:
                                            recodDateTime = DateUtil.parseDate(recodValue.getLastModifiedDate(), FMT_CREATED_DATE);
                                            break;
                                        case GBL_FOCUS_ASSET_IN_OUT_DATE:
                                            recodDateTime = DateUtil.parseDate(recodValue.getFreeDate(), FMT_CREATED_DATE);
                                            break;
                                        case GBL_FOCUS_CREATED_DATE:
                                            recodDateTime = DateUtil.parseDate(recodValue.getCreatedDate(), FMT_CREATED_DATE);
                                            break;
                                        case GBL_FOCUS_FREE_FIELD:
                                            for (FieldValue field : recodValue.getDetails()) {
                                                if (field.getId().equals(graphInfo.getFreeDateFieldId())) {
                                                    recodDateTime = DateUtil.parseJavaScriptDateTime(field.getValue());
                                                    break;
                                                }
                                            }
                                            break;
                                        default:
                                            recodDateTime = DateUtil.parseDate(recodValue.getCreatedDate(), FMT_CREATED_DATE);
                                            break;
                                    }
                                } else {
                                    recodDateTime = DateUtil.parseDate(recodValue.getCreatedDate(), FMT_CREATED_DATE);
                                }

                                String recordDateStr = new SimpleDateFormat(FMT_FILTER_DATE).format(recodDateTime.toDate());
                                DateTime recordDate = DateUtil.parseDate(recordDateStr, FMT_FILTER_DATE);
//                                LOG.info("recordDate : " + recordDate.toString(FMT_FILTER_DATE));

                                // Check if view date and record date is in correct range
                                // AKA date rule implementation.
                                if ( // Before Date comparison (End Date)
                                        (recordDate.isBefore(endDateRule)
                                        || recordDate.isEqual(endDateRule))
                                        && // After Date comparison (Start Date)
                                        (recordDate.isAfter(startDateRule)
                                        || recordDate.isEqual(startDateRule))
                                        // Bypass date
                                        || bypassDateRulle) {

                                    LocalDate local = recordDate.toLocalDate();

                                    String xAxisValue = "";
                                    if (null != graphInfo.getGraphView()) {
                                        switch (graphInfo.getGraphView()) {
                                            case GBL_OFTD:
                                                xAxisValue = local.toString(FMT_FILTER_DATE);
                                                break;
                                            case GBL_MTMTFY:
                                            case GBL_MTMFD:
                                                xAxisValue = local.toString(FMT_MONTH_NAME);
                                                break;
                                            case GBL_DOWFY:
                                            case GBL_DOWFM:
                                                xAxisValue = local.toString(FMT_DAY_NAME);
                                                break;
                                            default:
                                                // SWAP DATA HERE
                                                xAxisValue = yAxisValue;
                                                if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                                                    yAxisValue = MDL_ASSET_DEFAULT;
                                                } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                                                    yAxisValue = MDL_EMPLOYEE_DEFAULT;
                                                }
                                                break;
                                        }
                                    }

                                    for (int y = 0; y < yAxis.length; y++) {
                                        String yAxisFocus = yAxis[y];
//                                        LOG.info("yAxisFocus : " + yAxisFocus);
//                                        LOG.info("yAxisValue : " + yAxisValue);
                                        if (yAxisFocus.equals(yAxisValue)) {
                                            int[] dataSet = yxData[y];
                                            for (int x = 0; x < xAxis.length; x++) {
                                                String xAxisFocus = xAxis[x];
//                                                LOG.info("xAxisFocus : " + xAxisFocus);
//                                                LOG.info("xAxisValue : " + xAxisValue);
                                                if (xAxisFocus.equals(xAxisValue)) {
                                                    dataSet[x] += 1;
                                                    yxData[y] = dataSet;
                                                }
                                            }
                                        }
                                    }
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
                }
            }
            return data;
        } catch (Exception e) {
            LOG.error("error while get graph data", e);
            throw e;
        }
    }

    private void getXAxisLabels(List<String> xAxis, Graph graphInfo, Template temp) throws Exception {
        try {
            if (null != graphInfo.getGraphFocus()) {
                switch (graphInfo.getGraphFocus()) {
                    case GBL_FOCUS_CREATED_BY:
                        new UserBal(context).getAll().stream().filter((user) -> (user.isSystemUser())).forEach((user) -> {
                            xAxis.add(user.getUserName());
                        });
                        break;
                    case GBL_FOCUS_IN_AND_OUT:
                        xAxis.add(MDL_ASSET_STATUS_IN);
                        xAxis.add(MDL_ASSET_STATUS_OUT);
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
                                    List<String> xlabels = (List<String>) json.get(name);

                                    if (GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                                        xlabels.forEach((value) -> {
                                            xAxis.add(value + "-" + MDL_ASSET_STATUS_IN);
                                            xAxis.add(value + "-" + MDL_ASSET_STATUS_OUT);
                                        });
                                    } else {
                                        xlabels.forEach((value) -> {
                                            xAxis.add(value);
                                        });
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    case GBL_FOCUS_DEFAULT:
                        Menu menu = new MainAccessBal(context).getMenu(graphInfo.getMenuId(), token);
                        if (GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType())) {
                            xAxis.add(MDL_ASSET_DEFAULT);
                        } else if (GlobalMenuType.GBL_MT_EMPLOYEE.equals(menu.getMenuType())) {
                            xAxis.add(MDL_EMPLOYEE_DEFAULT);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            LOG.error("error while get x axis", e);
            throw e;
        }
    }

    /**
     * Initializes the start date and end date for a year
     *
     * @param endDate
     * @param startDate
     */
    private void initialiseYearDates() throws Exception {
        try {
            String endDateStr = endDate.getYear() + "-12-31";
            LocalDate localDateEnd = new LocalDate(endDateStr);
            endDate = localDateEnd.toDateTimeAtStartOfDay();

            String startDateStr = endDate.getYear() + "-01-01";
            LocalDate localstratDate = new LocalDate(startDateStr);
            startDate = localstratDate.toDateTimeAtStartOfDay();
        } catch (Exception e) {
            LOG.error("error while get graph year dates", e);
            throw e;
        }
    }

    /**
     * Initializes the start date and end date for a month
     *
     * @param endDate
     * @param startDate
     */
    private void initialiseMonthDates() throws Exception {
        try {
            String endDateStr = endDate.getYear()
                    + "-" + endDate.getMonthOfYear()
                    + "-" + endDate.dayOfMonth().getMaximumValue();
            LocalDate localEndDate = new LocalDate(endDateStr);
            endDate = localEndDate.toDateTimeAtStartOfDay();

            String startDateStr = endDate.getYear()
                    + "-" + endDate.getMonthOfYear()
                    + "-" + endDate.dayOfMonth().getMinimumValue();
            LocalDate localStartDate = new LocalDate(startDateStr);
            startDate = localStartDate.toDateTimeAtStartOfDay();
        } catch (Exception e) {
            LOG.error("error while month dates", e);
            throw e;
        }
    }

}
