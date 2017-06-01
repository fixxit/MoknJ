package nl.it.fixx.moknj.bal;

import java.util.List;
import nl.it.fixx.moknj.exception.BalException;

/**
 * Main Record BAL interface. All common Record REST Controllers logic needs to
 * be consolidated on this layer.
 *
 * @author adriaan
 * @param <T>
 */
public interface RecordBal<T> {

    /**
     * Gets a specific record by its UUID.
     *
     * @param id record UUID
     * @return List of Beans
     * @throws java.lang.Exception
     */
    public Object get(String id) throws Exception;

    /**
     * Gets all the records for entity record.
     *
     * @param templateId
     * @param menuId
     * @param access_token
     * @return List of Beans
     * @throws nl.it.fixx.moknj.exception.BalException
     */
    public List<T> getAll(String templateId, String menuId, String access_token) throws BalException;

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
    public T save(String templateId, String menuId, T record, String access_token) throws BalException;

    /**
     * Delete a record.
     *
     * @param record to be deleted
     * @param menuId
     * @param access_token
     * @param cascade delete asset/employee link data
     * @throws nl.it.fixx.moknj.exception.BalException
     */
    public void delete(T record, String menuId, String access_token, boolean cascade) throws BalException;
}
