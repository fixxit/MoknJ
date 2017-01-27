package nl.it.fixx.moknj.bal;

import java.util.List;

/**
 * Main Record BAL interface. All common Record REST Controllers logic needs to
 * be consolidated on this layer.
 *
 * @author adriaan
 */
public interface RecordBal {

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
     * @throws java.lang.Exception
     */
    public List getAll(String templateId, String menuId, String access_token) throws Exception;

    /**
     * Save the record.
     *
     * @param templateId
     * @param menuId
     * @param record
     * @param access_token
     * @return
     * @throws Exception
     */
    public Object save(String templateId, String menuId, Object record, String access_token) throws Exception;

    /**
     * Delete a record.
     *
     * @param record to be deleted
     * @param access_token
     * @param cascade delete asset/employee link data
     * @throws java.lang.Exception
     */
    public void delete(Object record, String access_token, boolean cascade) throws Exception;
}
