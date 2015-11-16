package at.tuwien.ase.services;

import at.tuwien.ase.controller.TaskController;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;


/**
 * Created by DanielHofer on 16.16.11.2015.
 */
public interface IssueService {


    /**
     * get Issue by id
     *
     * @param issueId
     * @return created Issue
     */
    public Issue getIssue(int issueId);

    /**
     * update issue to task
     *
     * @param issueId
     * @return Task object
     */
    public Task updateIssue(int issueId);

    /**
     * create new issue
     *
     * @param issue
     * @return created Issue
     */
    public Issue postIssue(Issue issue);
}
