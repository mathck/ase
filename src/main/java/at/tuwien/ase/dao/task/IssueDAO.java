package at.tuwien.ase.dao.task;

import at.tuwien.ase.model.task.Issue;


/**
 * Created by DanielHofer on 14.14.11.2015.
 */

public interface IssueDAO {
    /**
     * used to insert new issue objects to db
     *
     * @param issue
     * @return issue id from db
     */
    public int insertIssue(Issue issue);

    /**
     * used to find issue by id
     *
     * @param issueId
     * @return issue object
     */
    public Issue findByIssueId(int issueId);

}
