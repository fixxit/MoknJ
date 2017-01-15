package nl.it.fixx.moknj.domain.core.global;

/**
 * This enum used to identify type of graph
 *
 * @author adriaan
 */
public enum GlobalGraphType {
    GBL_LINE("chart-line", "Line Chart"),
    GBL_BAR("chart-bar", "Bar Chart"),
    GBL_HORIZONTAL_BAR("chart-horizontal-bar", "Horizontal Bar"),
    GBL_POLAR_AREA("chart-polar-area", "Polar Area"),
    GBL_RADAR("chart-radar", "Radar"),
    GBL_PIE("chart-pie", "Pie Chart");

    private final String property;
    private final String name;

    GlobalGraphType(String property, String name) {
        this.property = property;
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

}
