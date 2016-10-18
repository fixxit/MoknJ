/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.info;

import java.util.ArrayList;
import nl.fixx.asset.data.domain.Asset;

/**
 *
 * @author adriaan
 */
public class AssetResponse extends Response {

    private Asset asset;
    private ArrayList<Asset> assets;

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
    public ArrayList<Asset> getAssets() {
        return assets;
    }

    /**
     * @param assets the assets to set
     */
    public void setAssets(ArrayList<Asset> assets) {
        this.assets = assets;
    }

    @Override
    public String toString() {
        return "AssetResponse{" + "success=" + success
                + ", action='" + action + '\''
                + ", method='" + method + '\''
                + ", message='" + message + '\''
                + ", asset=" + asset + '\''
                + ", assets=" + assets + '}';
    }
}
