/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.repository;

import nl.fixx.asset.data.domain.AssetLink;
import nl.fixx.asset.data.repository.custom.AssetLinkRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author adriaan
 */
@Repository
public interface AssetLinkRepository extends MongoRepository<AssetLink, String>, AssetLinkRepositoryCustom {

}