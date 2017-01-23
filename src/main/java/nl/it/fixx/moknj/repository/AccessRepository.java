/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.repository;

import nl.it.fixx.moknj.domain.core.access.Access;
import nl.it.fixx.moknj.repository.custom.AccessRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author adriaan
 */
@Repository
public interface AccessRepository extends MongoRepository<Access, String>, AccessRepositoryCustom {

}
