package nl.it.fixx.moknj.domain.core.global;

/**
 * This is global access rights type enum for all rights.
 *
 * @author adriaan
 */
public enum GlobalAccessRights {
    VIEW("View"),
    NEW("New"),
    EDIT("Edit"),
    DELETE("Delete");

    GlobalAccessRights(String displayValue) {
        this.displayValue = displayValue;
    }
    private final String displayValue;

    public String getDisplayValue() {
        return displayValue;
    }

}
