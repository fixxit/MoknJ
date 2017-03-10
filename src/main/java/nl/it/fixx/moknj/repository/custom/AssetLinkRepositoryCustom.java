package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.asset.AssetLink;

/**
 *
 * @author adriaan
 */
public interface AssetLinkRepositoryCustom {

    public List<AssetLink> getAllByResourceId(String id);

    public List<AssetLink> getAllByAssetId(String id);
}
