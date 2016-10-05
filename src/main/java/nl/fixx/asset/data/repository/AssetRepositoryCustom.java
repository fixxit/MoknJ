package nl.fixx.asset.data.repository;

import java.math.BigDecimal;
import java.util.List;

import nl.fixx.asset.data.domain.Asset;

public interface AssetRepositoryCustom {
    ////example to to implemented
    List<Asset> findAllExpensiveAssets(BigDecimal price, Asset asset);
}
