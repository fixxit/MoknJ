package nl.it.fixx.moknj.bal.module.recordaction.save.impl;

import nl.it.fixx.moknj.bal.module.recordaction.save.SaveActionBase;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;
import nl.it.fixx.moknj.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Just prove of concept class to prove that main chain is instantiated...
 *
 * @author SmarBullet
 */
@Service
public class AssetSaveAction extends SaveActionBase<AssetLink, Asset, AssetRepository> {

    @Autowired
    public AssetSaveAction(AssetRepository repository) {
        super(repository);
    }

    @Override
    public boolean valid(Object domain) {
        return (domain instanceof Asset);
    }

}
