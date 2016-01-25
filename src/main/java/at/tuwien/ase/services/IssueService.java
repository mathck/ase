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
     *
     *
     * @param issue
     * @param pID
     * @param uID
     * @return
     * @throws ValidationException      if an validation
     *                                  exception occurred
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    JsonStringWrapper writeIssue(Issue issue, int pID, String uID) throws ValidationException, DataAccessException;

    /**
     *
     *
     *
     * @param iID
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void deleteIssueByID(int iID) throws DataAccessException;

    /**
     *
     *
     *
     * @param iID
     * @return
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    Issue getByID(int iID) throws DataAccessException;

    /**
     *
     *
     *
     * @return
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> getAllIssues() throws DataAccessException;

    /**
     *
     *
     *
     * @param uID
     * @return
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> getAllIssuesFromUser(String uID) throws DataAccessException;

    /**
     *
     *
     *
     * @param pID
     * @return
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> getAllIssuesFromProject(int pID) throws DataAccessException;

    /**
     *
     *
     *
     * @param pID
     * @param uID
     * @return
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> getAllIssuesFromProjectAndUser(int pID, String uID) throws DataAccessException;

    /**
     *
     *
     *
     * @param iID
     * @param pID
     * @param task
     * @return
     * @throws Exception      if an exception occurred
     */
    LinkedList<Integer> updateIssueToTask(int iID, int pID, Task task) throws Exception;

}
