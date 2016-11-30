/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
