package at.tuwien.ase.controller;

import at.tuwien.ase.dao.task.IssueDAO;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by DanielHofer on 14.11.2015.
 */

@RestController
public class IssueController {

    @Autowired
    private IssueDAO issueDAO;

    private static final Logger logger = LogManager.getLogger(TaskController.class);


    @RequestMapping(value = "workspace/projects/{projectId}/issues/{issuesId}", method = RequestMethod.GET)
    public @ResponseBody
    Issue getIssue(@PathVariable("projectId") int projectId, @PathVariable("issuesId") int issuesId) {

        logger.debug("get issue with id="+issuesId);

        //find issue by id
        Issue issue = issueDAO.findByIssueId(issuesId);

        return issue;
    }

    @RequestMapping(value = "workspace/projects/{projectId}/issues/{issuesId}", method = RequestMethod.PATCH)
    public @ResponseBody Task updateIssue(@PathVariable("projectId") int projectId, @PathVariable("issuesId") int issuesId) {

        logger.debug("update issue with id="+issuesId);

        //TODO update issue to task

        Task dummy = new Task(1, "dummy","dummy");

        return dummy;
    }

    @RequestMapping(value = "workspace/projects/{projectId}/issues", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody Issue postIssue(@RequestBody Issue issue, @PathVariable("projectId") int projectId) {

        logger.debug("post new issue with id="+issue.getId());

        //db insert and return id
        issue.setId(issueDAO.insertIssue(issue));


        return issue;
    }

}
