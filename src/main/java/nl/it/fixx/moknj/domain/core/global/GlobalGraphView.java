package nl.it.fixx.moknj.domain.core.global;

/**
 * This enum used to identify type of graph view this includes the date usage
 *
 * @author adriaan
 */
public enum GlobalGraphView {
    //MONTHLY VIEWS (MONTHS AS Lables)
    GBL_MTMTFY("Month to month for year (month name as x-axis)"),
    GBL_MTMFD("Month to month for date (month name as x-axis)"),
    // FLAT TIME LINE (Pie chart only) x axis as the data sets
    GBL_SYTEOY("Start of year to end of year (focus value as x-axis)"),
    GBL_SMTEOM("Start of month to end of year/month (focus value as x-axis)"),
    GBL_SYTD("Start of year to end of date (focus value as x-axis)"),
    // ONLY FOR TODAY
    GBL_OFTD("Only for today (focus value as x-axis)"),
    // DAYS OF WEEK (DAY AS Lables)
    GBL_DOWFY("Day of week for year (day name as x-axis)"),
    GBL_DOWFM("Day of week for year/month (day name as x-axis)");
    // ONCE OF


    private GlobalGraphView(String name) {
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
