package nl.it.fixx.moknj.domain.core.graph;

/**
 *
 * @author adriaan
 */
public class GraphFocus {

    private String enumName;
    private String displayValue;

    public GraphFocus(String enumName, String displayValue) {
        this.enumName = enumName;
        this.displayValue = displayValue;
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

    /**
     * @return the displayValue
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * @param displayValue the displayValue to set
     */
    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }
}
