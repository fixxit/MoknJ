package nl.it.fixx.moknj.domain.core.global;

/**
 * Graph date caveat setting
 *
 * @author adriaan
 */
public enum GlobalGraphDate {
    GBL_FOCUS_LAST_MODIFIED("Last Modified Date"),
    GBL_FOCUS_CREATED_DATE("Created Date"),
    GBL_FOCUS_FREE_FIELD("Free Field (Template Date Field)");

    /**
     * display field option name on home setup screen.
     */
    private final String name;

    GlobalGraphDate(String name) {
        this.name = name;
    }

    public String displayName() {
        return name;
    }
}
