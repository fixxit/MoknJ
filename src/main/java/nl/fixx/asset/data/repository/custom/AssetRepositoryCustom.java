package nl.fixx.asset.data.repository.custom;

import java.util.List;

import nl.fixx.asset.data.domain.Asset;

public interface AssetRepositoryCustom {

    List<Asset> findAll();
}
