package nl.it.fixx.moknj.bal.module.graphfilter.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.bal.core.access.MainAccessCoreBal;
import nl.it.fixx.moknj.bal.module.graphfilter.GraphFilterBase;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.bal.module.ModuleBal;
import nl.it.fixx.moknj.bal.module.link.impl.AssetLinkBal;
import nl.it.fixx.moknj.domain.core.global.GlobalGraphDate;
import nl.it.fixx.moknj.domain.core.global.GlobalMenuType;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.menu.Menu;
import nl.it.fixx.moknj.domain.core.template.Template;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.util.DateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AssetGraphFilter extends GraphFilterBase<Asset> {

    private final AssetLinkBal linkBal;
    private final ModuleBal<Asset> module;

    @Autowired
    public AssetGraphFilter(MainAccessCoreBal mainAccessBal,
            @Qualifier("assetBal") ModuleBal<Asset> assetBal,
            AssetLinkBal linkBal, UserCoreBal userBal) {
        super(mainAccessBal, userBal);
        this.linkBal = linkBal;
        this.module = assetBal;
    }

    @Override
    public String getDefaultValueForModule() {
        return "Asset";
    }

    @Override
    public boolean valid(Graph graphInfo, String token) {
        final Menu menu = mainAccessBal.getMenu(graphInfo.getMenuId(), token);
        return GlobalMenuType.GBL_MT_ASSET.equals(menu.getMenuType());
    }

    @Override
    public List<Asset> getData(Graph graphInfo, Menu menu, Template template, String token) {
        List<Asset> records = module.getAll(template.getId(), menu.getId(), token);
        // duplicate records for all entries which have the same id.
        // Basically a left join...
        if (GlobalGraphDate.GBL_FOCUS_ASSET_IN_OUT_DATE.equals(graphInfo.getGraphDateType())) {
            records = getAssetCheckOutAndInRecords(records, menu, template, token);
        }
        return records;
    }

    private List<Asset> getAssetCheckOutAndInRecords(List<Asset> records, Menu menu, Template template, String token) {
        final List<Asset> results = new ArrayList<>();
        records.stream().filter((record) -> (record instanceof Asset)).map((record)
                -> (Asset) record).forEachOrdered((asset) -> {
            // gets all the checked in/out records for asset.
            final List<AssetLink> links = linkBal.getAllAssetLinksByAssetId(
                    asset.getId(), menu.getId(), template.getId(), token);

            links.forEach((link) -> {
                // create new instance of asset... prototype pattern would be nice here...
                final Asset newAsset = new Asset();
                if (link.getAssetId().equals(asset.getId())) {
                    DateTime date = DateUtil.parseJavaScriptDateTime(link.getDate());
                    newAsset.setFreeDate(new SimpleDateFormat(FMT_CREATED_DATE).format(date.plusDays(1).toDate()));
                    newAsset.setFreeValue(link.isChecked() ? OUT : IN);
                    newAsset.setCreatedBy(asset.getCreatedBy());
                    newAsset.setCreatedDate(asset.getCreatedDate());
                    newAsset.setDetails(asset.getDetails());
                    newAsset.setLastModifiedBy(asset.getLastModifiedBy());
                    newAsset.setLastModifiedDate(asset.getLastModifiedDate());
                    newAsset.setMenuScopeIds(asset.getMenuScopeIds());
                    newAsset.setResourceId(asset.getResourceId());
                    newAsset.setTypeId(asset.getTypeId());
                    results.add(newAsset);
                }
            });
        });
        return results;
    }

}
