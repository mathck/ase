package at.tuwien.ase.dao;

import at.tuwien.ase.model.*;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel Hofer on 20.11.2015.
 */
public interface SubtaskDAO {

    /**
     *
     * Insert {@link Subtask} object to db.
     *
     * @param subtask  {@link Subtask} object for db insertion.
     * @return         Id of inserted {@link Subtask}
     * @throws DataAccessException
     */
    int insertSubtask(final Subtask subtask) throws DataAccessException;

    /**
     *
     * Insert {@link List} of {@link Subtask} to db in batch mode.
     * Each {@link Subtask} is added for each {@link Task} specified in {@link List}.
     * uuID is added to each entry.
     *
     * @param subtaskList  {@link List} of {@link Subtask} for db insertion.
     * @param taskIds      {@link LinkedList} of {@link Task} Ids.
     * @param uuID         universally unique identifier.
     * @throws DataAccessException
     */
    void insertSubtaskBatch(final List<Subtask> subtaskList, final LinkedList<Integer> taskIds, final String uuID) throws DataAccessException;

    /**
     *
     * Remove {@link Subtask} from db by Id.
     *
     * @param tID  Id of {@link Subtask}
     * @throws DataAccessException
     */
    void removeSubtaskByID(int tID) throws DataAccessException;

    /**
     *
     * Get {@link Subtask} from db by Id.
     *
     * @param tID   Id of {@link Subtask}.
     * @return      Selected {@link Subtask}.
     * @throws DataAccessException
     */
    Subtask findByID(int tID) throws DataAccessException;

    /**
     *
     * Get {@link TaskElementJson} by Id and {@link Subtask} Id.
     *
     * @param tID   Id of {@link TaskElementJson}
     * @param sID   Id of {@link Subtask}
     * @return      {@link TaskElementJson}
     * @throws DataAccessException
     */
    TaskElementJson findTaskItemByID(int tID, int sID) throws DataAccessException;

    /**
     *
     * Get all {@link Subtask} from db
     *
     * @return {@link LinkedList} of {@link Subtask}
     * @throws DataAccessException
     */
    LinkedList<Subtask> loadAll() throws DataAccessException;

    /**
     *
     * Get all {@link Subtask} from db of a specific {@link Task}
     *
     * @param tID   {@link Task}
     * @return      {@link LinkedList} of {@link Subtask}
     * @throws DataAccessException
     */
    LinkedList<Subtask> loadAllByTask(int tID) throws DataAccessException;

    /**
     *
     * Get all {@link Subtask} from db of a specific {@link Project}
     *
     * @param pID   {@link Project}
     * @return      {@link LinkedList} of {@link Subtask}
     * @throws DataAccessException
     */
    LinkedList<Subtask> loadAllByProject(int pID) throws DataAccessException;

    /**
     *
     * Get all {@link Subtask} from db of a specific {@link User}
     *
     * @param uID   {@link User}
     * @return      {@link LinkedList} of {@link Subtask}
     * @throws DataAccessException
     */
    LinkedList<Subtask> loadAllByUser(String uID) throws DataAccessException;

    /**
     *
     * Get a {@link HashMap} where key is id of {@link TaskElementJson}
     * and element is {@link TaskElementJson}.
     * {@link TaskElementJson} are loaded by {@link Subtask} id.
     *
     * @param sID   Id of {@link Subtask}.
     * @return      {@link HashMap} where key is id of {@link TaskElementJson} and element is {@link TaskElementJson}.
     * @throws DataAccessException
     */
    HashMap<Integer, TaskElementJson> loadAllTaskItemsBySubtaskId(Integer sID) throws DataAccessException;

    /**
     *
     * Get a {@link HashMap} where key is id of {@link DslTemplate}
     * and element is {@link LinkedList} of {@link Subtask}.
     * Elements are loaded by universally unique identifier.
     *
     *
     * @param uuID  universally unique identifier.
     * @return      {@link HashMap} where key is id of {@link DslTemplate} and element is {@link LinkedList} of {@link Subtask}
     * @throws DataAccessException
     */
    HashMap<Integer, LinkedList<Subtask>> loadSubtaskIdsByUuID(String uuID) throws DataAccessException;

    /**
     *
     * Add {@link TaskElementJson} to {@link Subtask}.
     *
     * @param taskItem    {@link TaskElementJson} object.
     * @param sID         Id of {@link Subtask}.
     * @throws DataAccessException
     */
    void addTaskItemToSubtask(TaskElementJson taskItem, int sID) throws DataAccessException ;

    /**
     *
     * Add {@link List} of {@link TaskElementJson} to {@link Subtask}.
     * Each {@link TaskElementJson} has an id for {@link Subtask} set.
     *
     * @param taskElementJsonList   {@link List} of {@link TaskElementJson}
     * @throws DataAccessException
     */
    void addTaskItemToSubtaskBatch(final List<TaskElementJson> taskElementJsonList) throws DataAccessException;

    /**
     *
     * Update {@link LinkedList} of {@link TaskElementJsonUpdate} in db.
     *
     * @param taskItemList   {@link LinkedList} of {@link TaskElementJsonUpdate} for db update.
     * @throws DataAccessException
     */
    void updateTaskItemBatch(final LinkedList<TaskElementJsonUpdate> taskItemList) throws DataAccessException;

    /**
     *
     * Update status of {@link Subtask} specified by id.
     *
     * @param sID        Id of {@link Subtask}.
     * @param subtask    {@link Subtask} object
     * @return           Number of affected rows in db
     * @throws DataAccessException
     */
    int updateSubtaskStatusById(int sID, Subtask subtask) throws DataAccessException;

    /**
     *
     * Update {@link Subtask} specified by id.
     *
     * @param sID        Id of {@link Subtask}.
     * @param subtask    {@link Subtask} object
     * @return           Number of affected rows in db
     * @throws DataAccessException
     */
    int updateSubtaskById(int sID, SubtaskUpdate subtask) throws DataAccessException;

}
