package nl.it.fixx.moknj.response;

import java.util.List;
import nl.it.fixx.moknj.domain.modules.asset.Asset;

/**
 *
 * @author adriaan
 */
public class AssetResponse extends Response {

    private Asset asset;
    private List<Asset> assets;

    /**
     * @return the asset
     */
    public Asset getAsset() {
        return asset;
    }

    /**
     * @param asset the asset to set
     */
    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    /**
     * @return the assets
     */
    public List<Asset> getAssets() {
        return assets;
    }

    /**
     * @param assets the assets to set
     */
    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    @Override
    public String toString() {
        return "AssetResponse{" + "success=" + success + ", action='" + action + '\'' + ", method='" + method + '\'' + ", message='" + message + '\'' + ", asset=" + asset + '\'' + ", assets=" + assets + '}';
    }
}
