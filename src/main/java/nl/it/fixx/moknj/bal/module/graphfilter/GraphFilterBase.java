package nl.it.fixx.moknj.bal.module.graphfilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.bal.core.access.MainAccessCoreBal;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.domain.core.field.FieldDetail;
import nl.it.fixx.moknj.domain.core.field.FieldValue;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphDate;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphView;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphData;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.util.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nl.it.fixx.moknj.bal.module.ModuleBal;
import nl.it.fixx.moknj.bal.module.link.impl.AssetLinkBal;

/**
 * This class is used to group the graph data for display in the UI
 *
 * @author adriaan
 * @param <MODULE>
 */
public abstract class GraphFilterBase<MODULE extends ModuleBal> implements GraphFilter {

    private static final Logger LOG = LoggerFactory.getLogger(GraphFilterBase.class);

    private DateTime endDate;
    private DateTime startDate;
    private GraphFilter nextIn;

    private static final String IN = "In";
    private static final String OUT = "Out";

    protected final MainAccessCoreBal mainAccessBal;
    protected final MODULE module;
    protected final AssetLinkBal linkBal;
    protected final UserCoreBal userBal;

    public GraphFilterBase(MainAccessCoreBal mainAccessBal, MODULE module,
            AssetLinkBal linkBal, UserCoreBal userBal) {
        this.mainAccessBal = mainAccessBal;
        this.module = module;
        this.linkBal = linkBal;
        this.userBal = userBal;
    }

    private final String FMT_CREATED_DATE = "yyyy-MM-dd HH:mm";
    private final String FMT_FILTER_DATE = "yyyy-MM-dd";
    private final String FMT_MONTH_NAME = "MMM";
    private final String FMT_DAY_NAME = "EEE";

    @Override
    public void setNextIn(GraphFilter graphSearch) {
        this.nextIn = graphSearch;
    }

    @Override
    public boolean hasNext() {
        return nextIn != null;
    }

    @Override
    public GraphData execute(Graph graphInfo, String token) throws Exception {
        if (valid(graphInfo, token)) {
            return filter(graphInfo, token);
        } else {
            if (hasNext()) {
                return nextIn.execute(graphInfo, token);
            }
        }
        return new GraphData();
    }

    /**
     * Generates the graph data!
     *
     * @param graphInfo
     * @param token
     * @return
     * @throws Exception
     */
    private GraphData filter(Graph graphInfo, String token) throws Exception {
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
                        List<String> xAxisLabels = new ArrayList<>();
                        List<String> yAxisLabels = new ArrayList<>();
                        List records = module.getAll(template.getId(), menu.getId(), token);
                        // duplicate records for all entries which have the same id.
                        // Basically a left join...
                        if (GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                            records = getAssetCheckOutAndInRecords(records, menu, template, token);
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
                                case GBL_OFTD: {
                                    xAxisLabels.add(today.toLocalDate().toString(FMT_FILTER_DATE));
                                    // Set end and start date to today/current date
                                    endDate = today;
                                    startDate = today;
                                }
                                break;
                                case GBL_SMTEOM: {
                                    getXAxisLabels(xAxisLabels, graphInfo, template, token);
                                    xAxisSwapped = true;
                                    initialiseMonthDates();
                                }
                                break;
                                case GBL_SYTEOY: {
                                    getXAxisLabels(xAxisLabels, graphInfo, template, token);
                                    xAxisSwapped = true;
                                    initialiseYearDates();
                                }
                                break;
                                case GBL_SYTD: {
                                    // SWOPS THE data set for x axis labels
                                    getXAxisLabels(xAxisLabels, graphInfo, template, token);
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
                            yAxisLabels = new ArrayList<>();
                            addDefaultValueForModule(yAxisLabels);
                        }

                        final int[][] yxData = new int[yAxisLabels.size()][xAxisLabels.size()];
                        final String[] yAxis = yAxisLabels.toArray(new String[yAxisLabels.size()]);
                        final String[] xAxis = xAxisLabels.toArray(new String[xAxisLabels.size()]);

                        for (Object record : records) {
                            final Record recodValue = (Record) record;
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
                                                    if (IN.equals(recodValue.getFreeValue())) {
                                                        yAxisValue = value + "-" + IN;
                                                    } else if (OUT.equals(recodValue.getFreeValue())) {
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
                                        if (record instanceof Asset) {
                                            final Asset asset = (Asset) record;
                                            // null check for assignment
                                            // null == in
                                            // not null == out
                                            if (GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                                                yAxisValue = asset.getFreeValue();
                                            } else if (!GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
                                                if (asset.getResourceId() != null
                                                        && !asset.getResourceId().trim().isEmpty()) {
                                                    yAxisValue = OUT;
                                                } else {
                                                    yAxisValue = IN;
                                                }
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
                                    || bypassDateRulle) {

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
            throw e;
        }
    }

    private void getXAxisLabels(List<String> xAxis, Graph graphInfo, Template temp, String token) throws Exception {
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

    private void addFreeFieldLabels(List<String> list, Graph graphInfo, Template temp) {
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

                    if (GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
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

    private List<Asset> getAssetCheckOutAndInRecords(List records, Menu menu, Template template, String token) throws Exception {
        final List<Asset> results = new ArrayList<>();
        for (final Object record : records) {
            if (record instanceof Asset) {
                final Asset asset = (Asset) record;
                // gets all the checked in/out records for asset.
                final List<AssetLink> links = linkBal.getAllAssetLinksByAssetId(
                        asset.getId(), menu.getId(), template.getId(), token);

                links.forEach((link) -> {
                    // create new instance of asset... prototype pattern would be nice here...
                    final Asset newAsset = new Asset();
                    if (link.getAssetId().equals(asset.getId())) {
                        DateTime date = DateUtil.parseJavaScriptDateTime(link.getDate());
                        newAsset.setFreeDate(new SimpleDateFormat(FMT_CREATED_DATE).format(date.plusDays(1).toDate()));
                        newAsset.setFreeValue(link.isChecked() ? OUT : IN);
                        newAsset.setCreatedBy(asset.getCreatedBy());
                        newAsset.setCreatedDate(asset.getCreatedDate());
                        newAsset.setDetails(asset.getDetails());
                        newAsset.setLastModifiedBy(asset.getLastModifiedBy());
                        newAsset.setLastModifiedDate(asset.getLastModifiedDate());
                        newAsset.setMenuScopeIds(asset.getMenuScopeIds());
                        newAsset.setResourceId(asset.getResourceId());
                        newAsset.setTypeId(asset.getTypeId());
                        results.add(newAsset);
                    }
                });
            }
        }
        return results;
    }

}