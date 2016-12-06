/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.domain.core.global;

/**
 *
 * @author adriaan
 */
public enum GlobalMenuType {
    GBL_MT_EMPLOYEE("Employee", GlobalTemplateType.GBL_TT_EMPLOYEE),
    GBL_MT_ASSET("Asset", GlobalTemplateType.GBL_TT_ASSET);

    private final String name;
    private final GlobalTemplateType template;

    GlobalMenuType(String displayName, GlobalTemplateType template) {
        this.name = displayName;
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template.name();
    }

}
