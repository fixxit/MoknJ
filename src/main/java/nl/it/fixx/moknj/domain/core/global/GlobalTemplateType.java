package nl.it.fixx.moknj.domain.core.global;

/**
 *
 * @author adriaan
 */
public enum GlobalTemplateType {
    GBL_TT_EMPLOYEE("Employee"),
    GBL_TT_ASSET("Asset");

    private final String name;

    GlobalTemplateType(String displayName) {
        this.name = displayName;
    }

    public String getName() {
        return name;
    }

}
