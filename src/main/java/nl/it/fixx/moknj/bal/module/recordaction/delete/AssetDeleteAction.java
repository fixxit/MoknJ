package nl.it.fixx.moknj.bal.module.recordaction.delete;

import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetDeleteAction extends DeleteActionBase<AssetLink, Asset, AssetRepository> {

    @Autowired
    public AssetDeleteAction(AssetRepository repository) {
        super(repository);
    }

    @Override
    public boolean valid(Object domain) {
        return (domain instanceof Asset);
    }

}
