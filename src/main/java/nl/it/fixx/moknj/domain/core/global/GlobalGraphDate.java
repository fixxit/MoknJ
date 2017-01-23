package nl.it.fixx.moknj.domain.core.global;

import static nl.it.fixx.moknj.domain.core.global.GlobalTemplateType.GBL_TT_ASSET;
import static nl.it.fixx.moknj.domain.core.global.GlobalTemplateType.GBL_TT_EMPLOYEE;

/**
 * Graph date caveat setting
 *
 * @author adriaan
 */
public enum GlobalGraphDate {
    GBL_FOCUS_CREATED_DATE("Created date", GBL_TT_EMPLOYEE, GBL_TT_ASSET),
    GBL_FOCUS_LAST_MODIFIED("Last modified date", GBL_TT_EMPLOYEE, GBL_TT_ASSET),
    GBL_FOCUS_ASSET_IN_OUT_DATE("Asset checked In/Out date", GBL_TT_ASSET),
    GBL_FOCUS_FREE_FIELD("Free field (Template date Field)", GBL_TT_EMPLOYEE, GBL_TT_ASSET),
    GBL_FOCUS_NO_DATE_RULE("No date limitation (Bypass date)", GBL_TT_EMPLOYEE, GBL_TT_ASSET);

    /**
     * display field option name on home setup screen.
     */
    private final String name;
    /**
     * templates aloud to use this focus option in graph setup.
     */
    private final GlobalTemplateType[] templates;

    GlobalGraphDate(String name, GlobalTemplateType... templates) {
        this.name = name;
        this.templates = templates;
    }

    public String displayName() {
        return name;
    }

    public GlobalTemplateType[] getTemplates() {
        return templates;
    }

}
