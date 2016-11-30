package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.asset.Asset;

public interface AssetRepositoryCustom {

    public List<Asset> findAll();

    public List<Asset> getAllByTypeId(String id);
    
    public List<Asset> getAllByResourceId(String id);

}
