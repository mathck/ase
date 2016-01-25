package at.tuwien.ase.dao;

import at.tuwien.ase.model.Comment;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskState;
import at.tuwien.ase.model.User;
import org.springframework.dao.DataAccessException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Daniel Hofer on 09.11.2015.
 */
public interface TaskDAO {

    /**
     *
     * Insert {@link Task} object to db.
     *
     * @param pID    Id of {@link at.tuwien.ase.model.Project}.
     * @param task   {@link Task} object.
     * @throws DataAccessException
     */
    void insertTask(int pID, Task task) throws DataAccessException;

    /**
     *
     * Insert {@link List} of {@link Task} in batch mode.
     * Each entry is added to {@link at.tuwien.ase.model.Project} specified by Id.
     * uuID is added to each entry.
     *
     * @param pID          Id of {@link at.tuwien.ase.model.Project}.
     * @param taskList     {@link List} of {@link Task} for db batch insert.
     * @param uuID         universally unique identifier.
     * @throws DataAccessException
     */
    void insertTaskBatch(final int pID, final List<Task> taskList, final String uuID) throws DataAccessException;

    /**
     *
     * Remove {@link Task} from db by id.
     *
     * @param tID   Id of {@link Task} to remove from task.
     * @throws DataAccessException
     */
    void removeTaskByID(int tID) throws DataAccessException;

    /**
     *
     * Get {@link Task} from db by Id.
     *
     * @param tID   Id of {@link Task}.
     * @return      {@link Task} object.
     * @throws DataAccessException
     */
    Task findByID(int tID) throws DataAccessException;

    /**
     *
     * Get all {@link Task} from db.
     *
     * @return {@link LinkedList} of {@link Task}.
     * @throws DataAccessException
     */
    LinkedList<Task> loadAll() throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Task} id's by uuID.
     * This method is used for getting all {@link Task} id's after batch insertion.
     *
     * @param uuID    universally unique identifier.
     * @return        {@link LinkedList} of {@link Task} id's.
     * @throws DataAccessException
     */
    LinkedList<Integer> loadTaskIdsByUuID(String uuID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Task} from a specific {@link at.tuwien.ase.model.Project}.
     *
     * @param pID   Id of {@link at.tuwien.ase.model.Project}.
     * @return      {@link LinkedList} of {@link Task}.
     * @throws DataAccessException
     */
    LinkedList<Task> loadAllByProject(int pID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Task} from a specific {@link User}.
     *
     * @param uID   Id of {@link User}.
     * @return      {@link LinkedList} of {@link Task}.
     * @throws DataAccessException
     */
    LinkedList<Task> loadAllByUser(String uID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Comment} from a specific {@link Task}.
     *
     * @param tID     Id of  {@link Task}.
     * @return        {@link LinkedList} of {@link Comment}.
     * @throws DataAccessException
     */
    LinkedList<Comment> loadAllCommentsByTask(int tID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link User} assigned to a specific {@link Task}.
     *
     * @param tID     Id of  {@link Task}.
     * @return        {@link LinkedList} of {@link User}.
     * @throws DataAccessException
     */
    LinkedList<User> loadAllUsersByTask(int tID) throws DataAccessException;

    /**
     *
     * Assign {@link User} to {@link Task}.
     *
     * @param uID  Id of {@link User}.
     * @param tID  Id of {@link Task}.
     * @throws DataAccessException
     */
    void addUserToTask(String uID, int tID) throws DataAccessException;

    /**
     *
     * Add {@link List} of {@link TaskState} to {@link Task} using batch insertion.
     * For each {@link TaskState}: Add {@link TaskState} for each {@link Task}
     * specified in {@link LinkedList} of {@link Task} id's.
     *
     * @param taskStateList   {@link LinkedList} of @link TaskState}.
     * @param taskIds         {@link LinkedList} of {@link Task} id's.
     * @throws DataAccessException
     */
    void addStateToTaskStatesBatch(final List<TaskState> taskStateList, final LinkedList<Integer> taskIds) throws DataAccessException;

    /**
     *
     * Add {@link TaskState} to {@link Task} specified by id.
     *
     * @param state    {@link TaskState} object.
     * @param tID      Id of {@link Task}.
     * @throws DataAccessException
     */
    void addStateToTaskStates(TaskState state, int tID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Task} by a specific {@link User} and
     * by a specific {@link at.tuwien.ase.model.Project}.
     *
     *
     * @param pID    Id of {@link at.tuwien.ase.model.Project}.
     * @param uID    Id of {@link User}.
     * @return       {@link LinkedList} of {@link Task}.
     * @throws DataAccessException
     */
    LinkedList<Task> loadAllByProjectAndUser(int pID, String uID) throws DataAccessException;

    /**
     *
     * Get new Id from db sequence for {@link Comment} insertion.
     *
     * @return Generated Id.
     * @throws DataAccessException
     */
    int getNewIDForComments() throws DataAccessException;

    /**
     *
     * Assign {@link User} to {@link Task}.
     *
     * @param tID  Id of {@link Task}.
     * @param uID  Id of {@link User}
     * @throws DataAccessException
     */
    void assignUserToTask(int tID, String uID) throws DataAccessException;

    /**
     *
     * Assign {@link User} to {@link Task} batch mode.
     *
     * @param userList  {@link List} of {@link User}.
     * @param taskId    Id of {@link Task}.
     * @throws DataAccessException
     */
    void assignUserToTaskBatch(final List<User> userList, final Integer taskId) throws DataAccessException;

    /**
     *
     * Remove {@link User} from {@link Task}.
     *
     * @param tID    Id of {@link Task}.
     * @param uID    Id of {@link User}.
     * @throws DataAccessException
     */
    void removeUserFromTask(int tID, String uID) throws DataAccessException;

    /**
     *
     * Add {@link Comment} to {@link Task}.
     *
     * @param tID         Id of {@link Task}.
     * @param comment     {@link Comment} object.
     * @throws DataAccessException
     */
    void addCommentToTask(int tID, Comment comment) throws DataAccessException;

    /**
     *
     * Remove {@link Comment} from {@link Task}.
     *
     * @param tID         Id of {@link Task}.
     * @param cID         Id of {@link Comment}.
     * @throws DataAccessException
     */
    void removeCommentFromTask(int tID, int cID) throws DataAccessException;

}
