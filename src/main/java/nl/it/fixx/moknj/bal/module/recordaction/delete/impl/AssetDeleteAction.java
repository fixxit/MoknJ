package nl.it.fixx.moknj.bal.module.recordaction.delete.impl;

import nl.it.fixx.moknj.bal.module.ModuleLinkBal;
import nl.it.fixx.moknj.bal.module.recordaction.delete.DeleteActionBase;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AssetDeleteAction extends DeleteActionBase<Void, Record, RecordRepository<Asset>> {

    private final ModuleLinkBal<AssetLink> assetLinkBal;

    @Autowired
    public AssetDeleteAction(
            @Qualifier("assetRepository") RecordRepository<Asset> repository,
            @Qualifier("assetLinkBal") ModuleLinkBal<AssetLink> assetLinkBal) {
        super(repository);
        this.assetLinkBal = assetLinkBal;
    }

    @Override
    public boolean valid(Object domain) {
        return (domain instanceof Asset);
    }

    @Override
    public Void before(Record domain) {
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
