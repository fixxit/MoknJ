package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.bal.module.recordaction.Intercept;
import nl.it.fixx.moknj.bal.module.recordaction.delete.Delete;
import org.springframework.stereotype.Repository;

import nl.it.fixx.moknj.domain.modules.asset.Asset;
import nl.it.fixx.moknj.repository.custom.AssetRepositoryCustom;

@Repository
public interface AssetRepository extends RecordRepository<Asset>, AssetRepositoryCustom {

    @Override
    @Delete(intercepts = {Intercept.BEFORE})
    public void delete(Asset t);

}
