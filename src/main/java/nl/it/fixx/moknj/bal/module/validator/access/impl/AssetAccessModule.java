package nl.it.fixx.moknj.bal.module.validator.access.impl;

import nl.it.fixx.moknj.bal.core.access.AccessCoreBal;
import nl.it.fixx.moknj.bal.core.user.UserCoreBal;
import nl.it.fixx.moknj.bal.module.validator.access.AccessModuleBase;
import nl.it.fixx.moknj.domain.modules.asset.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetAccessModule extends AccessModuleBase<Asset> {

    @Autowired
    public AssetAccessModule(UserCoreBal userBal, AccessCoreBal accessBal) {
        super(userBal, accessBal);
    }

    @Override
    public boolean canValidate(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Asset) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getModule() {
        return "asset";
    }

}
