package nl.it.fixx.moknj.bal.module;

import java.util.List;
import nl.it.fixx.moknj.domain.core.record.Record;

/**
 * Main Record BAL interface. All common Record REST Controllers logic needs to
 * be consolidated on this layer.
 *
 * @author adriaan
 * @param <DOMAIN>
 */
public interface ModuleBal<DOMAIN extends Record> {

    boolean exists(String id);

    /**
     * Gets a specific record by its UUID.
     *
     * @param id record UUID
     * @return List of Beans
     */
    DOMAIN get(String id);

    /**
     * Gets all the records for entity record.
     *
     * @param templateId
     * @param menuId
     * @param access_token
     * @return List of Beans
     * @throws nl.it.fixx.moknj.exception.BalException
     */
    List<DOMAIN> getAll(String templateId, String menuId, String access_token);

    /**
     * Save the record.
     *
     * @param templateId
     * @param menuId
     * @param record
     * @param access_token
     * @return
     * @throws nl.it.fixx.moknj.exception.BalException
     */
    DOMAIN save(String templateId, String menuId, DOMAIN record, String access_token);

    /**
     * Delete a record.
     *
     * @param record to be deleted
     * @param menuId
     * @param access_token
     * @param cascade delete asset/employee link data
     * @throws nl.it.fixx.moknj.exception.BalException
     */
    void delete(DOMAIN record, String menuId, String access_token, boolean cascade);
}
