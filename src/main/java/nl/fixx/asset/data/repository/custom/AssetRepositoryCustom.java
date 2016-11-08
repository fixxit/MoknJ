package nl.fixx.asset.data.repository.custom;

import java.util.List;
import nl.fixx.asset.data.domain.Asset;

public interface AssetRepositoryCustom {

    public List<Asset> findAll();

    public List<Asset> getAllByTypeId(String id);
    
    public List<Asset> getAllByResourceId(String id);

}
