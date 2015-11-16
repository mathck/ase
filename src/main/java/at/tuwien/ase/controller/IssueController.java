package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.dao.task.IssueDAO;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.services.IssueService;
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
    private IssueService issueService;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(TaskController.class);


    @RequestMapping(value = "workspace/projects/{projectId}/issues/{issueId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Issue getIssue(@PathVariable("projectId") int projectId, @PathVariable("issueId") int issueId) throws Exception {

        return issueService.getIssue(issueId);

    }

    @RequestMapping(value = "workspace/projects/{projectId}/issues/{issueId}", method = RequestMethod.PATCH)
    public
    @ResponseBody
    Task updateIssue(@PathVariable("projectId") int projectId, @PathVariable("issueId") int issueId) throws Exception {

        return issueService.updateIssue(issueId);

    }

    @RequestMapping(value = "workspace/projects/{projectId}/issues", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    Issue postIssue(@RequestBody Issue issue, @PathVariable("projectId") int projectId) throws Exception {

        return issueService.postIssue(issue);
    }

}
