package at.tuwien.ase.services;


import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Subtask;
import at.tuwien.ase.model.SubtaskUpdate;
import org.springframework.dao.DataAccessException;


import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface SubtaskService {

    /**
     *
     * Write {@link Subtask} to db.
     *
     * @param subtask   {@link Subtask} object.
     * @return          Id of created {@link Subtask} wrapped in {@link JsonStringWrapper}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    JsonStringWrapper writeSubtask(Subtask subtask) throws DataAccessException;

    /**
     *
     * Delete {@link Subtask} by id.
     *
     * @param sID   Id of {@link Subtask}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void deleteSubtaskByID(int sID)  throws DataAccessException;

    /**
     *
     * Update {@link Subtask} by id. The following values can be
     * updated: title, description, xp, status
     *
     * @param sID       Id of {@link Subtask}.
     * @param subtask   {@link Subtask} object.
     * @throws ValidationException      if an validation
     *                                  exception occurred
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void updateSubtask(int sID, SubtaskUpdate subtask) throws ValidationException, DataAccessException;

    /**
     *
     * Set status of {@link Subtask} to closed.
     *
     * @param sID  Id of {@link Subtask}.
     * @throws ValidationException      if an validation
     *                                  exception occurred
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void closeSubtask(int sID)  throws ValidationException, DataAccessException;

    /**
     *
     * Get {@link Subtask} by id.
     *
     * @param sID  Id of {@link Subtask}.
     * @return     {@link Subtask} object.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    Subtask getByID(int sID) throws DataAccessException;

    /**
     *
     * Get all {@link Subtask} from db.
     *
     * @return       {@link LinkedList} of {@link Subtask}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Subtask> getAllSubtasks() throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Subtask} by specific {@link at.tuwien.ase.model.Task}.
     *
     * @param tID    Id of {@link at.tuwien.ase.model.Task}.
     * @return       {@link LinkedList} of {@link Subtask}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Subtask> getAllSubtasksFromTask(int tID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Subtask} by specific {@link at.tuwien.ase.model.User}.
     *
     * @param uID    Id of {@link at.tuwien.ase.model.User}.
     * @return       {@link LinkedList} of {@link Subtask}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Subtask> getAllSubtasksFromUser(String uID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Subtask} by specific {@link at.tuwien.ase.model.Project}.
     *
     * @param pID    Id of {@link at.tuwien.ase.model.Project}.
     * @return       {@link LinkedList} of {@link Subtask}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Subtask> getAllSubtasksFromProject(int pID) throws DataAccessException;


}




