package nl.it.fixx.moknj.bal.module.recordaction.delete;

import nl.it.fixx.moknj.bal.module.asset.AssetLinkBal;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetDeleteAction extends DeleteActionBase<Void, Asset, AssetRepository> {

    private final AssetLinkBal assetLinkBal;

    @Autowired
    public AssetDeleteAction(AssetRepository repository, AssetLinkBal assetLinkBal) {
        super(repository);
        this.assetLinkBal = assetLinkBal;
    }

    @Override
    public boolean valid(Object domain) {
        return (domain instanceof Asset);
    }

    @Override
    public Void before(Asset domain) {
        Asset result = repository.findOne(domain.getId());
        if (result != null && domain.isCascade()) {
            // delete links
            assetLinkBal.getAllLinksByRecordId(result.getId(),
                    domain.getToken()).stream().forEach((link) -> {
                assetLinkBal.delete(link);
            });
        }
        return null;
    }

}
