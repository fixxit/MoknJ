/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.bal.module;

import java.util.List;
import nl.it.fixx.moknj.domain.core.link.Link;
import nl.it.fixx.moknj.exception.BalException;

/**
 *
 * @author SmarBullet
 * @param <DOMAIN>
 */
public interface ModuleLinkBal<DOMAIN extends Link> {

    void delete(DOMAIN record);

    void save(DOMAIN record);

    List<DOMAIN> getAllLinks(String token) throws BalException;

    List<DOMAIN> getAllLinksByRecordId(String recordId, String token) throws BalException;
}
