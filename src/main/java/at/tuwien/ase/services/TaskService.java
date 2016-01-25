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
     * @param pID
     * @param task
     * @return
     * @throws Exception
     */
    LinkedList<Integer> writeTask(int pID, Task task) throws Exception;

    /**
     *
     * @param tID
     * @throws DataAccessException
     */
    void deleteTaskByID(int tID) throws DataAccessException;

    /**
     *
     * @param tID
     * @return
     * @throws DataAccessException
     */
    Task getByID(int tID) throws DataAccessException;

    /**
     *
     * @return
     * @throws DataAccessException
     */
    LinkedList<Task> getAllTasks() throws DataAccessException;

    /**
     *
     * @param uID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Task> getAllTasksFromUser(String uID) throws DataAccessException;

    /**
     *
     * @param pID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Task> getAllTasksFromProject(int pID) throws DataAccessException;

    /**
     *
     * @param tID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Comment> getAllCommentsByTask(int tID) throws DataAccessException;

    /**
     *
     * @param tID
     * @return
     * @throws DataAccessException
     */
    LinkedList<User> getAllUserFromTask(int tID) throws DataAccessException;

    /**
     *
     * @param pID
     * @param uID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Task> getAllTasksFromProjectAndUser(int pID, String uID) throws DataAccessException;

    /**
     *
     * @param tID
     * @param uID
     * @throws DataAccessException
     */
    void assignUserToTask(int tID, String uID) throws DataAccessException;

    /**
     *
     * @param tID
     * @param uID
     * @throws DataAccessException
     */
    void removeUserFromTask(int tID, String uID) throws DataAccessException;

    /**
     *
     * @param tID
     * @param comment
     * @return
     * @throws ValidationException
     * @throws DataAccessException
     */
    JsonStringWrapper addCommentToTask(int tID, Comment comment) throws ValidationException, DataAccessException;

    /**
     *
     * @param tID
     * @param cID
     * @throws DataAccessException
     */
    void deleteCommentFromTask(int tID, int cID) throws DataAccessException;


}




