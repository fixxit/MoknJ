/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.repository.custom;

import java.util.List;
import nl.it.fixx.moknj.domain.core.access.Access;

/**
 *
 * @author adriaan
 */
public interface AccessRepositoryCustom {

    public boolean hasAccess(String userId, String menuId, String templateId) throws Exception;

    public List<Access> getAccessList(String userId) throws Exception;

}
