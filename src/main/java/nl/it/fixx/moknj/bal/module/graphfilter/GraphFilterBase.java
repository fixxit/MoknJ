package nl.it.fixx.moknj.bal.module.graphfilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.bal.core.access.MainAccessCoreBal;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import static nl.it.fixx.moknj.domain.core.global.GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE;
import static nl.it.fixx.moknj.domain.core.global.GlobalGraphDate.GBL_FOCUS_NO_DATE_RULE;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphView;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphData;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.exception.BalException;
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
 * @param <DOMAIN>
 */
public abstract class GraphFilterBase<DOMAIN extends Record> implements GraphFilter {

    private static final Logger LOG = LoggerFactory.getLogger(GraphFilterBase.class);

    private DateTime endDate;
    private DateTime startDate;
    private GraphFilter nextIn;

    protected static final String IN = "In";
    protected static final String OUT = "Out";

    protected final MainAccessCoreBal mainAccessBal;
    protected final UserCoreBal userBal;

    public GraphFilterBase(MainAccessCoreBal mainAccessBal,
            UserCoreBal userBal) {
        this.mainAccessBal = mainAccessBal;
        this.userBal = userBal;
    }

    protected final String FMT_CREATED_DATE = "yyyy-MM-dd HH:mm";
    protected final String FMT_FILTER_DATE = "yyyy-MM-dd";
    protected final String FMT_MONTH_NAME = DateUtil.MONTH;
    protected final String FMT_DAY_NAME = DateUtil.DAY;

    @Override
    public void setNext(GraphFilter graphSearch) {
        this.nextIn = graphSearch;
    }

    @Override
    public boolean hasNext() {
        return nextIn != null;
    }

    @Override
    public GraphData execute(Graph graphInfo, String token) {
        if (valid(graphInfo, token)) {
            return filter(graphInfo, token);
        } else {
            if (hasNext()) {
                return nextIn.execute(graphInfo, token);
            }
        }
        return new GraphData();
    }

    public abstract List<DOMAIN> getData(Graph graphInfo, Menu menu, Template template, String token);

    /**
     * Generates the graph data!
     *
     * @param graphInfo
     * @param token
     * @return
     * @throws Exception
     */
    private GraphData filter(Graph graphInfo, String token) {
        try {
            final DateTime today = new DateTime();
            final GraphData data = new GraphData();
            // get all records for template.
            if (graphInfo != null) {
                LOG.info("======================================================");
                LOG.info("Graph Name : " + graphInfo.getName());
                // Business access layer.
                final Menu menu = mainAccessBal.getMenu(graphInfo.getMenuId(), token);
                for (Template template : menu.getTemplates()) {
                    if (template.getId().equals(graphInfo.getTemplateId())) {
                        /**
                         * below is where the date logic is generated or x -
                         * axis focus labels. This is where you will start
                         * seeing dragons beware of purple!
                         */
                        // X-AXIS Label Logic
                        boolean xAxisSwapped = false;
                        final List<String> xAxisLabels = new ArrayList<>();
                        final List<String> yAxisLabels = new ArrayList<>();
                        List<DOMAIN> records = getData(graphInfo, menu, template, token);

                        // This is used to bypass the filterdate and filter rule logic
                        boolean bypassDateRule = false;
                        if (GBL_FOCUS_NO_DATE_RULE.equals(graphInfo.getGraphDateType())) {
                            if (!GlobalGraphView.GBL_OFTD.equals(graphInfo.getGraphView())) {
                                bypassDateRule = true;
                            }
                        }

                        if (!bypassDateRule) {
                            endDate = DateUtil.parseJavaScriptDateTime(graphInfo.getGraphDate());
                            startDate = today;
                            //javascript is one day off todo with date type UCT
                            endDate = endDate.plusDays(1);
                        } else {
                            startDate = today;
                            endDate = today;
                        }

                        if (null != graphInfo.getGraphView()) {
                            switch (graphInfo.getGraphView()) {
                                case GBL_MTMTFY: {
                                    xAxisLabels.addAll(DateUtil.geMonthsForYear(endDate.getYear()));
                                    initialiseYearDates();
                                }
                                break;
                                case GBL_MTMFD: {
                                    xAxisLabels.addAll(DateUtil.geMonthsForDate(endDate));
                                    initialiseMonthDates();
                                }
                                break;
                                case GBL_DOWFY: {
                                    xAxisLabels.addAll(DateUtil.getDaysOfWeek(endDate));
                                    initialiseYearDates();
                                }
                                break;
                                case GBL_DOWFM: {
                                    xAxisLabels.addAll(DateUtil.getDaysOfWeek(endDate));
                                    initialiseMonthDates();
                                }
                                break;
                                case GBL_OFTD: {
                                    xAxisLabels.add(today.toLocalDate().toString(FMT_FILTER_DATE));
                                    // Set end and start date to today/current date
                                    endDate = today;
                                    startDate = today;
                                }
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
                                    LocalDate localstratDate = new LocalDate(endDate.getYear() + "-01-01");
                                    startDate = localstratDate.toDateTimeAtStartOfDay();
                                }
                                break;
                                default:
                                    break;
                            }
                        }

                        LOG.info("Type : " + graphInfo.getGraphView().getDisplayName());
                        LOG.info("End Date : " + endDate.toString(FMT_FILTER_DATE));
                        LOG.info("Start Date : " + startDate.toString(FMT_FILTER_DATE));

                        final String endDateStr = new SimpleDateFormat(FMT_FILTER_DATE).format(endDate.toDate());
                        final DateTime endDateRule = DateUtil.parseDate(endDateStr, FMT_FILTER_DATE);
                        final String startDateStr = new SimpleDateFormat(FMT_FILTER_DATE).format(startDate.toDate());
                        final DateTime startDateRule = DateUtil.parseDate(startDateStr, FMT_FILTER_DATE);

                        if (null != graphInfo.getGraphFocus()) {
                            switch (graphInfo.getGraphFocus()) {
                                case GBL_FOCUS_CREATED_BY:
                                    for (User user : userBal.getAll()) {
                                        if (user.isSystemUser()) {
                                            yAxisLabels.add(user.getUserName());
                                        }
                                    }
                                    break;
                                case GBL_FOCUS_FREE_FIELD:
                                    addFreeFieldLabels(yAxisLabels, graphInfo, template);
                                    break;
                                case GBL_FOCUS_IN_AND_OUT:
                                    yAxisLabels.add(IN);
                                    yAxisLabels.add(OUT);
                                    break;
                                case GBL_FOCUS_DEFAULT:
                                    addDefaultValueForModule(yAxisLabels);
                                    break;
                                default:
                                    break;
                            }
                        }

                        if (xAxisSwapped) {
                            yAxisLabels.clear();
                            addDefaultValueForModule(yAxisLabels);
                        }

                        final int[][] yxData = new int[yAxisLabels.size()][xAxisLabels.size()];
                        final String[] yAxis = yAxisLabels.toArray(new String[yAxisLabels.size()]);
                        final String[] xAxis = xAxisLabels.toArray(new String[xAxisLabels.size()]);

                        for (DOMAIN record : records) {
                            // y- axis value should come here
                            String yAxisValue = null;
                            if (null != graphInfo.getGraphFocus()) {
                                switch (graphInfo.getGraphFocus()) {
                                    case GBL_FOCUS_CREATED_BY:
                                        String createdBy = record.getCreatedBy();
                                        yAxisValue = createdBy;
                                        break;
                                    case GBL_FOCUS_FREE_FIELD:
                                        for (FieldValue field : record.getDetails()) {
                                            if (field.getId().equals(graphInfo.getFreefieldId())) {
                                                String value = field.getValue();
                                                if (GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                                                    if (IN.equals(record.getFreeValue())) {
                                                        yAxisValue = value + "-" + IN;
                                                    } else if (OUT.equals(record.getFreeValue())) {
                                                        yAxisValue = value + "-" + OUT;
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
                                        // null check for assignment
                                        // null == in
                                        // not null == out
                                        if (GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                                            yAxisValue = record.getFreeValue();
                                        } else if (!GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                                            if (record.getResourceId() != null
                                                    && !record.getResourceId().trim().isEmpty()) {
                                                yAxisValue = OUT;
                                            } else {
                                                yAxisValue = IN;
                                            }
                                        }
                                        break;
                                    case GBL_FOCUS_DEFAULT:
                                    default:
                                        yAxisValue = getDefaultValueForModule();
                                        break;
                                }
                            }

                            if (yAxisValue == null) {
                                yAxisValue = getDefaultValueForModule();
                            }

                            // Calculate date filter
                            DateTime recodDateTime = today;
                            if (null != graphInfo.getGraphDateType()) {
                                switch (graphInfo.getGraphDateType()) {
                                    case GBL_FOCUS_LAST_MODIFIED:
                                        recodDateTime = DateUtil.parseDate(record.getLastModifiedDate(), FMT_CREATED_DATE);
                                        break;
                                    case GBL_FOCUS_ASSET_IN_OUT_DATE:
                                        recodDateTime = DateUtil.parseDate(record.getFreeDate(), FMT_CREATED_DATE);
                                        break;
                                    case GBL_FOCUS_CREATED_DATE:
                                        recodDateTime = DateUtil.parseDate(record.getCreatedDate(), FMT_CREATED_DATE);
                                        break;
                                    case GBL_FOCUS_FREE_FIELD:
                                        for (FieldValue field : record.getDetails()) {
                                            if (field.getId().equals(graphInfo.getFreeDateFieldId())) {
                                                recodDateTime = DateUtil.parseJavaScriptDateTime(field.getValue());
                                                break;
                                            }
                                        }
                                        break;
                                    default:
                                        recodDateTime = DateUtil.parseDate(record.getCreatedDate(), FMT_CREATED_DATE);
                                        break;
                                }
                            } else {
                                recodDateTime = DateUtil.parseDate(record.getCreatedDate(), FMT_CREATED_DATE);
                            }

                            final String recordDateStr = new SimpleDateFormat(FMT_FILTER_DATE).format(recodDateTime.toDate());
                            final DateTime recordDate = DateUtil.parseDate(recordDateStr, FMT_FILTER_DATE);
                            LOG.info("recordDate : " + recordDate.toString(FMT_FILTER_DATE));

                            // Check if view date and record date is in correct range
                            // AKA date rule implementation.
                            if ( // Before Date comparison (End Date)
                                    (recordDate.isBefore(endDateRule)
                                    || recordDate.isEqual(endDateRule))
                                    && // After Date comparison (Start Date)
                                    (recordDate.isAfter(startDateRule)
                                    || recordDate.isEqual(startDateRule))
                                    // Bypass date
                                    || bypassDateRule) {

                                final LocalDate local = recordDate.toLocalDate();
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
                                            yAxisValue = getDefaultValueForModule();
                                            break;
                                    }
                                }

                                for (int y = 0; y < yAxis.length; y++) {
                                    final String yAxisFocus = yAxis[y];
                                    LOG.info("yAxisFocus : " + yAxisFocus);
                                    LOG.info("yAxisValue : " + yAxisValue);
                                    if (yAxisFocus.equals(yAxisValue)) {
                                        int[] dataSet = yxData[y];
                                        for (int x = 0; x < xAxis.length; x++) {
                                            final String xAxisFocus = xAxis[x];
                                            LOG.info("xAxisFocus : " + xAxisFocus);
                                            LOG.info("xAxisValue : " + xAxisValue);
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
            return data;
        } catch (Exception e) {
            LOG.error("error while get graph data", e);
            throw new BalException(e);
        }
    }

    private void getXAxisLabels(final List<String> xAxis, final Graph graphInfo, final Template temp) throws Exception {
        try {
            if (null != graphInfo.getGraphFocus()) {
                switch (graphInfo.getGraphFocus()) {
                    case GBL_FOCUS_CREATED_BY:
                        userBal.getAll().stream().filter((user) -> (user.isSystemUser())).forEach((user) -> {
                            xAxis.add(user.getUserName());
                        });
                        break;
                    case GBL_FOCUS_IN_AND_OUT:
                        xAxis.add(IN);
                        xAxis.add(OUT);
                        break;
                    case GBL_FOCUS_FREE_FIELD:
                        addFreeFieldLabels(xAxis, graphInfo, temp);
                        break;
                    case GBL_FOCUS_DEFAULT:
                        addDefaultValueForModule(xAxis);
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

    private void addFreeFieldLabels(final List<String> list, final Graph graphInfo, final Template temp) {
        if (graphInfo.getFreefieldId() != null) {
            for (FieldDetail field : temp.getDetails()) {
                if (field.getId().equals(graphInfo.getFreefieldId())) {
                    final String drpValue = field.getName();
                    final int n = drpValue.indexOf(":");
                    final String name = drpValue.substring(0, n - 1);
                    final String array = drpValue.substring(n + 1, drpValue.length());
                    final String jsonStr = "{\"" + name + "\":" + array + "}";
                    final JSONObject json = (JSONObject) JSONValue.parse(jsonStr);
                    final List<String> labels = (List<String>) json.get(name);

                    if (GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                        labels.forEach((value) -> {
                            list.add(value + "-" + IN);
                            list.add(value + "-" + OUT);
                        });
                    } else {
                        labels.forEach((value) -> {
                            list.add(value);
                        });
                    }
                    break;
                }
            }
        }
    }

    private void addDefaultValueForModule(List<String> list) {
        list.add(getDefaultValueForModule());
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
