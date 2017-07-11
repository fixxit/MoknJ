package nl.it.fixx.moknj.bal.module.asset;

import nl.it.fixx.moknj.bal.BAL;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetDeleteLink extends BAL<AssetRepository> {

    private final AssetLinkBal linkBal;

    @Autowired
    public AssetDeleteLink(AssetRepository assetRepo, AssetLinkBal linkBal) {
        super(assetRepo);
        this.linkBal = linkBal;
    }

    public void delete(Asset record, String menuId, String token, boolean cascade) throws Throwable {
        Asset result = repository.findOne(record.getId());
        if (result != null && cascade) {
            // delete links
            linkBal.getAllLinksByRecordId(result.getId(),
                    token).stream().forEach((link) -> {
                        linkBal.delete(link);
                    });
        }
    }

}
