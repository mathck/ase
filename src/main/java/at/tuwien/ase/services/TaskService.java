package at.tuwien.ase.services;

import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.Comment;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.User;
import org.springframework.dao.DataAccessException;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 16.11.2015.
 */
public interface TaskService {

    /**
     *
     * Write {@link Task} object to db.
     *
     * @param pID    Id of {@link at.tuwien.ase.model.Project}.
     * @param task   {@link Task} object.
     * @return       {@link LinkedList} of {@link Task} id's.
     * @throws Exception      if an exception occurred
     */
    LinkedList<Integer> writeTask(int pID, Task task) throws Exception;

    /**
     *
     * Delete {@link Task} by id.
     *
     * @param tID    Id of {@link Task}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void deleteTaskByID(int tID) throws DataAccessException;

    /**
     *
     * Get {@link Task} by id.
     *
     * @param tID   Id of {@link Task}.
     * @return      {@link Task} object.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    Task getByID(int tID) throws DataAccessException;

    /**
     *
     * Get all {@link Task} from db.
     *
     * @return     {@link LinkedList} of {@link Task}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Task> getAllTasks() throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Task} by specific {@link User}.
     *
     * @param uID    Id of {@link User}.
     * @return       {@link LinkedList} of {@link Task}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Task> getAllTasksFromUser(String uID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Task} by specific {@link at.tuwien.ase.model.Project}.
     *
     * @param pID    Id of {@link at.tuwien.ase.model.Project}.
     * @return       {@link LinkedList} of {@link Task}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Task> getAllTasksFromProject(int pID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Comment} by specific {@link Task}.
     *
     * @param tID    Id of {@link Task}.
     * @return       {@link LinkedList} of {@link Comment}
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Comment> getAllCommentsByTask(int tID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link User} by specific {@link Task}.
     *
     * @param tID    Id of {@link Task}.
     * @return       {@link LinkedList} of {@link Comment}
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<User> getAllUserFromTask(int tID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Task} by specific {@link at.tuwien.ase.model.Project}
     * and {@link User}.
     *
     * @param pID    Id of {@link at.tuwien.ase.model.Project}
     * @param uID    Id of {@link User}
     * @return       {@link LinkedList} of {@link Task}
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Task> getAllTasksFromProjectAndUser(int pID, String uID) throws DataAccessException;

    /**
     *
     * Assign {@link User} to {@link Task}.
     *
     * @param tID  Id of {@link Task}.
     * @param uID  Id of {@link User}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void assignUserToTask(int tID, String uID) throws DataAccessException;

    /**
     *
     * Remove {@link User} from {@link Task}.
     *
     * @param tID    Id of {@link Task}.
     * @param uID    Id of {@link User}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void removeUserFromTask(int tID, String uID) throws DataAccessException;

    /**
     *
     * Add {@link Comment} to {@link Task}.
     *
     * @param tID      Id of {@link Task}.
     * @param comment  {@link Comment} object
     * @return         Id of {@link Comment} wrapped in {@link JsonStringWrapper}
     * @throws ValidationException      if an validation
     *                                  exception occurred
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    JsonStringWrapper addCommentToTask(int tID, Comment comment) throws ValidationException, DataAccessException;

    /**
     *
     * Delete {@link Comment} from {@link Task}.
     *
     * @param tID    Id of {@link Task}.
     * @param cID    Id of {@link Comment}
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void deleteCommentFromTask(int tID, int cID) throws DataAccessException;


}




