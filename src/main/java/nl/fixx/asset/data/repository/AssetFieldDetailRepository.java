/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.repository;

import nl.fixx.asset.data.domain.AssetFieldDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetFieldDetailRepository extends MongoRepository<AssetFieldDetail, String> {

}