package nl.it.fixx.moknj.repository.wrapper.impl;

import nl.it.fixx.moknj.bal.module.recordaction.Intercept;
import nl.it.fixx.moknj.bal.module.recordaction.delete.Delete;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.repository.AssetRepository;
import nl.it.fixx.moknj.repository.wrapper.BaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetWrapper extends BaseWrapper<Asset, AssetRepository> {

    @Autowired
    public AssetWrapper(AssetRepository repository) {
        super(repository);
    }

    @Override
    @Delete(intercepts = {Intercept.BEFORE})
    public void delete(Asset t) {
        super.delete(t);
    }

}
