/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 *
 * @author adriaan
 */
public class AssetTypeRepositoryCustomImpl implements AssetTypeRepositoryCustom {

    @Autowired
    private MongoOperations operations;



}
