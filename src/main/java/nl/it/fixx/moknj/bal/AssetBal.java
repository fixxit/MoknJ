/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.bal;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.repository.AssetRepository;
import nl.it.fixx.moknj.repository.MenuRepository;

/**
 * Asset Business Access Layer
 *
 * @author adriaan
 */
public class AssetBal implements RecordBal, BusinessAccessLayer {

    private final AssetRepository assetRep;
    private final MenuRepository menuRep;

    public AssetBal(AssetRepository assetRep, MenuRepository menuRep) {
        this.assetRep = assetRep;
        this.menuRep = menuRep;
    }

    /**
     * Gets a asset by template id and menu id
     *
     * @param templateId
     * @param menuId
     * @return
     */
    @Override
    public List<Asset> getAll(String templateId, String menuId) {

        List<Asset> assets = new ArrayList<>();
        List<Asset> records = assetRep.getAllByTypeId(templateId);
        // Gets custom template settings saved to menu.
        Menu menu = menuRep.findOne(menuId);
        menu.getTemplates().stream().filter((template)
                -> (template.getId().equals(templateId))).forEach((Template template)
                -> {
            records.stream().forEach((Asset record) -> {
                // checks if scope check is required for this asset.
                boolean inScope;
                if (template.isAllowScopeChallenge()) {
                    inScope = record.getMenuScopeIds().contains(menuId);
                } else {
                    inScope = true;
                }
                // if asset is inscope allow adding of asset.
                if (inScope) {
                    // checks if the asset is hidden.
                    if (!record.isHidden()) {
                        assets.add(record);
                    }
                }
            });
        });

        return assets;
    }

}
