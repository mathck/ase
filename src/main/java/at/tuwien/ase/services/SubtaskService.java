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
     * @param subtask
     * @return
     * @throws DataAccessException
     */
    JsonStringWrapper writeSubtask(Subtask subtask) throws DataAccessException;

    /**
     *
     * @param sID
     * @throws DataAccessException
     */
    void deleteSubtaskByID(int sID)  throws DataAccessException;

    /**
     *
     * @param sID
     * @param subtask
     * @throws ValidationException
     * @throws DataAccessException
     */
    void updateSubtask(int sID, SubtaskUpdate subtask) throws ValidationException, DataAccessException;

    /**
     *
     * @param sID
     * @throws ValidationException
     * @throws DataAccessException
     */
    void closeSubtask(int sID)  throws ValidationException, DataAccessException;

    /**
     *
     * @param sID
     * @return
     * @throws DataAccessException
     */
    Subtask getByID(int sID) throws DataAccessException;

    /**
     *
     * @return
     * @throws DataAccessException
     */
    LinkedList<Subtask> getAllSubtasks() throws DataAccessException;

    /**
     *
     * @param tID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Subtask> getAllSubtasksFromTask(int tID) throws DataAccessException;

    /**
     *
     * @param uID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Subtask> getAllSubtasksFromUser(String uID) throws DataAccessException;

    /**
     *
     * @param pID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Subtask> getAllSubtasksFromProject(int pID) throws DataAccessException;


}




