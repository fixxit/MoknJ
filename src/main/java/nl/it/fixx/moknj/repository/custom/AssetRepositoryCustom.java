package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.asset.Asset;

public interface AssetRepositoryCustom {

    List<Asset> findAll();

    List<Asset> getAllByTypeId(String id);

    List<Asset> getAllByResourceId(String id);

}
