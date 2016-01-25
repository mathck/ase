package at.tuwien.ase.dao;

import at.tuwien.ase.model.DslTemplate;
import org.springframework.dao.DataAccessException;
import java.util.LinkedList;
import at.tuwien.ase.model.User;

/**
 * Created by DanielHofer on 21.12.2015.
 */
public interface DslTemplateDAO {

    /**
     * Inserts a dsl template to db
     *
     * @param template {@link DslTemplate} object for db insertion
     * @throws DataAccessException
     */
    void insertDslTemplate(DslTemplate template) throws DataAccessException;

    /**
     * Removes a dsl template from db.
     * The {@link DslTemplate} must be specified by id.
     *
     * @param tID template id for db removal
     * @throws DataAccessException
     */
    void removeDslTemplateByID(int tID) throws DataAccessException;

    /**
     * Get dsl template from db.
     * The dsl template must be specified by id.
     *
     * @param tID template id for db select
     * @return {@link DslTemplate} that was retrieved from db
     * @throws DataAccessException
     */
    DslTemplate findByID(int tID) throws DataAccessException;

    /**
     *
     * Load all {@link DslTemplate} from db
     *
     * @return {@link LinkedList} of {@link DslTemplate}.
     * @throws DataAccessException
     */
    LinkedList<DslTemplate> loadAll() throws DataAccessException;

    /**
     * Load all {@link DslTemplate} from db from a specific {@link User}
     *
     * @param uID user id for db selection
     * @return {@link LinkedList} of {@link DslTemplate}.
     * @throws DataAccessException
     */
    LinkedList<DslTemplate> loadAllByUser(String uID) throws DataAccessException;

    /**
     *
     * Alter {@link DslTemplate} by template Id
     *
     * @param template updated {@link DslTemplate}
     * @param tID      id of {@link DslTemplate}
     * @throws DataAccessException
     */
    void alterDslTemplateByID(DslTemplate template, int tID) throws DataAccessException;

    /**
     *
     * Get new Id from db sequence for {@link DslTemplate} insertion
     *
     * @return Generated id
     * @throws DataAccessException
     */
    int getNewID() throws DataAccessException;

}