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
 * @param <I>
 */
public interface ModuleLinkBal<I extends Link> {

    public void delete(I link);

    public void save(I link);

    public List<I> getAllLinks(String token) throws BalException;

    public List<I> getAllLinksByRecordId(String recordId, String token) throws BalException;
}
