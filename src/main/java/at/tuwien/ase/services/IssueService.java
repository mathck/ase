package at.tuwien.ase.services;

import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.Issue;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Task;
import org.springframework.dao.DataAccessException;

import java.util.LinkedList;


/**
 * Created by DanielHofer on 16.11.2015.
 */
public interface IssueService {

    /**
     *
     * Write {@link Issue} to db,
     *
     * @param issue   {@link Issue} object
     * @param pID     Id of {@link at.tuwien.ase.model.Project}
     * @param uID     Id of {@link at.tuwien.ase.model.User}
     * @return        Id of created {@link Issue} wrapped in {@link JsonStringWrapper}
     * @throws ValidationException      if an validation
     *                                  exception occurred
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    JsonStringWrapper writeIssue(Issue issue, int pID, String uID) throws ValidationException, DataAccessException;

    /**
     *
     * Delete {@link Issue} by Id.
     *
     * @param iID  Id of {@link Issue}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void deleteIssueByID(int iID) throws DataAccessException;

    /**
     *
     * Get {@link Issue} by Id.
     *
     * @param iID   Id of {@link Issue}.
     * @return      {@link Issue} object.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    Issue getByID(int iID) throws DataAccessException;

    /**
     *
     * Get all {@link Issue}.
     *
     * @return  {@link LinkedList} of {@link Issue}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> getAllIssues() throws DataAccessException;

    /**
     *
     * Get all {@link Issue} from specific {@link at.tuwien.ase.model.User}.
     *
     * @param uID   Id of {@link at.tuwien.ase.model.User}.
     * @return      {@link LinkedList} of {@link Issue}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> getAllIssuesFromUser(String uID) throws DataAccessException;

    /**
     *
     * Get all {@link Issue} from specific {@link at.tuwien.ase.model.Project}.
     *
     * @param pID   Id of {@link at.tuwien.ase.model.Project}.
     * @return      {@link LinkedList} of {@link Issue}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> getAllIssuesFromProject(int pID) throws DataAccessException;

    /**
     *
     * Get all {@link Issue} from specific {@link at.tuwien.ase.model.Project} and {@link at.tuwien.ase.model.User}.
     *
     * @param pID   Id of {@link at.tuwien.ase.model.Project}.
     * @param uID   Id of {@link at.tuwien.ase.model.User}.
     * @return      {@link LinkedList} of {@link Issue}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> getAllIssuesFromProjectAndUser(int pID, String uID) throws DataAccessException;

    /**
     *
     * Update {@link Issue} to {@link Task}. {@link Issue} will be
     * removed and a new {@link Task} object will be created.
     *
     * @param iID   Id of {@link Issue}.
     * @param pID   Id of {@link at.tuwien.ase.model.Project}.
     * @param task  {@link Task} object.
     * @return      {@link LinkedList} of {@link Task} id's.
     * @throws Exception      if an exception occurred
     */
    LinkedList<Integer> updateIssueToTask(int iID, int pID, Task task) throws Exception;

}
