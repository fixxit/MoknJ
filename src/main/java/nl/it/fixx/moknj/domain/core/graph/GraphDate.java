package nl.it.fixx.moknj.domain.core.graph;

/**
 *
 * @author adriaan
 */
public class GraphDate {

    private String displayName;
    private String enumName;

    public GraphDate(String displayName, String enumName) {
        this.displayName = displayName;
        this.enumName = enumName;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the enumName
     */
    public String getEnumName() {
        return enumName;
    }

    /**
     * @param enumName the enumName to set
     */
    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

}
