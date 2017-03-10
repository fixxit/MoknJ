package nl.it.fixx.moknj.domain.core.global;

/**
 * This enum should be used to determine the date rule, as what time line is
 * used for the graph. This should be implemented in the future. The
 * GlobalGraphView should only have iteration as daily, monthly, yearly or none.
 *
 * @author adriaan
 */
public enum GlobalGraphViewRule {
    GBL_YEAR("Year"),
    GBL_MONTH("Month"),
    GBL_DATE("Date"),
    GBL_SYTEY("Start of year to end of year"),
    GBL_SMTEOM("Start of month to end of year/month"),
    GBL_SYTD("Start of year to end of date");

    private GlobalGraphViewRule(String name) {
        this.displayName = name;
    }

    final private String displayName;

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }
}
