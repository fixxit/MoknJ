/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.bal;

import java.util.List;

/**
 * Main BAL interface. All common REST Controllers logic needs to be
 * consolidated on this layer.
 *
 * @author adriaan
 */
public interface EntityBal {

    /**
     * Gets all the records for entity record.
     *
     * @param templateId
     * @param menuId
     * @return List of Beans
     */
    public List getAll(String templateId, String menuId);
}
