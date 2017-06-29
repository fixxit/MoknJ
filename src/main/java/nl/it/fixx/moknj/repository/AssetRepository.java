package nl.it.fixx.moknj.repository;

import org.springframework.stereotype.Repository;

import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.repository.custom.AssetRepositoryCustom;

@Repository
public interface AssetRepository extends RecordRepository<Asset>, AssetRepositoryCustom {

}
