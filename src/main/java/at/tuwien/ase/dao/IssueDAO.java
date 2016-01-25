package at.tuwien.ase.dao;

import at.tuwien.ase.model.DslTemplate;
import at.tuwien.ase.model.Issue;
import org.springframework.dao.DataAccessException;

import java.util.LinkedList;


/**
 * Created by Daniel Hofer on 14.11.2015.
 */
public interface IssueDAO {

    /**
     *
     *  Insert {@link Issue} object to db.
     *
     * @param issue  {@link Issue} for db insertion.
     * @return       Generated id of inserted {@link Issue}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    int insertIssue(final Issue issue) throws DataAccessException ;

    /**
     *
     * Remove {@link Issue} from db by Id.
     *
     * @param iID  Id of {@link Issue} to remove from db.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void removeIssueByID(int iID) throws DataAccessException ;

    /**
     *
     * Get {@link Issue} from db by Id.
     *
     * @param iID  {@link Issue} id to select from db.
     * @return     {@link Issue} object selected from db.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    Issue findByID(int iID) throws DataAccessException ;

    /**
     *
     * Get all {@link Issue} from db.
     *
     * @return {@link LinkedList} of {@link Issue}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> loadAll() throws DataAccessException ;

    /**
     *
     * Get all {@link Issue} from a specific {@link at.tuwien.ase.model.Project}.
     * {@link at.tuwien.ase.model.Project} must be specified by id.
     *
     * @param pID  {@link at.tuwien.ase.model.Project} id.
     * @return     {@link LinkedList} of {@link Issue}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> loadAllByProject(int pID) throws DataAccessException ;

    /**
     *
     * Get all {@link Issue} from a specific {@link at.tuwien.ase.model.User}.
     * {@link at.tuwien.ase.model.User} must be specified by id.
     *
     * @param uID  {@link at.tuwien.ase.model.User} id.
     * @return     {@link LinkedList} of {@link Issue}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> loadAllByUser(String uID) throws DataAccessException ;

    /**
     *
     * Get all {@link Issue} from a specific {@link at.tuwien.ase.model.User} and {@link at.tuwien.ase.model.Project}.
     * {@link at.tuwien.ase.model.User} and {@link at.tuwien.ase.model.Project} must be specified by id.
     *
     * @param pID  {@link at.tuwien.ase.model.Project} id.
     * @param uID  {@link at.tuwien.ase.model.User} id.
     * @return     {@link LinkedList} of {@link Issue}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Issue> loadAllByProjectAndUser(int pID, String uID) throws DataAccessException;

}
