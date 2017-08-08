package nl.it.fixx.moknj.bal.module.validator.field.impl;

import nl.it.fixx.moknj.bal.core.FieldBal;
import nl.it.fixx.moknj.bal.module.validator.field.FieldModuleBase;
import nl.it.fixx.moknj.bal.module.validator.field.Module;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetFieldModuleValidation extends FieldModuleBase<Asset, AssetRepository> {

    @Autowired
    public AssetFieldModuleValidation(AssetRepository repository, FieldBal fieldBal) {
        super(repository, fieldBal, AssetFieldModuleValidation.class);
    }

    @Override
    public Module getModule() {
        return Module.ASSET;
    }

}
