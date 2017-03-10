package nl.it.fixx.moknj.domain.core.global;

import static nl.it.fixx.moknj.domain.core.global.GlobalTemplateType.GBL_TT_ASSET;
import static nl.it.fixx.moknj.domain.core.global.GlobalTemplateType.GBL_TT_EMPLOYEE;

/**
 * This enum is used graph focus options.
 *
 * @author adriaan
 */
public enum GlobalGraphFocus {
    GBL_FOCUS_DEFAULT("Default (no focus)", GBL_TT_EMPLOYEE, GBL_TT_ASSET),
    GBL_FOCUS_CREATED_BY("Created by", GBL_TT_EMPLOYEE, GBL_TT_ASSET),
    GBL_FOCUS_FREE_FIELD("Free Field (Template dropdown)", GBL_TT_EMPLOYEE, GBL_TT_ASSET),
    GBL_FOCUS_IN_AND_OUT("Asset In/Out", GBL_TT_ASSET);

    /**
     * templates aloud to use this focus option in graph setup.
     */
    private final GlobalTemplateType[] templates;
    /**
     * display field option name on home setup screen.
     */
    private final String name;

    GlobalGraphFocus(String name, GlobalTemplateType... templates) {
        this.templates = templates;
        this.name = name;
    }

    public GlobalTemplateType[] getTemplates() {
        return templates;
    }

    public String displayName() {
        return name;
    }

}
