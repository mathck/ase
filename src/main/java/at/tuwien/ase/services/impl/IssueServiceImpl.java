package at.tuwien.ase.services.impl;

import at.tuwien.ase.controller.TaskController;
import at.tuwien.ase.dao.task.IssueDAO;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.services.IssueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DanielHofer on 16.16.11.2015.
 */

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueDAO issueDAO;

    private static final Logger logger = LogManager.getLogger(IssueServiceImpl.class);

    public Issue getIssue(int issueId) {
        logger.debug("get issue with id="+issueId);

        //find issue by id
        Issue issue = issueDAO.findByIssueId(issueId);

        return issue;
    }

    public Task updateIssue(int issueId) {
        logger.debug("update issue with id="+issueId);

        //TODO update issue to task

        Task dummy = new Task(1, "dummy","dummy");

        return dummy;
    }

    public Issue postIssue(Issue issue) {
        logger.debug("post new issue with id="+issue.getId());

        //db insert and return id
        issue.setId(issueDAO.insertIssue(issue));

        return issue;
    }
}
