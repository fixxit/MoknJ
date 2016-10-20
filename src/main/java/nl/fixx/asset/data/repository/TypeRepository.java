/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nl.fixx.asset.data.domain.AssetType;
import nl.fixx.asset.data.repository.custom.TypeRepositoryCustom;

/**
 *
 * @author adriaan
 */
@Repository
public interface TypeRepository extends MongoRepository<AssetType, String>, TypeRepositoryCustom {

}
