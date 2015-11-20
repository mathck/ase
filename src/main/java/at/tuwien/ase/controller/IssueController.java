package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.services.IssueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 14.11.2015.
 */

@RestController
public class IssueController {

    @Autowired
    private IssueService is;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(TaskController.class);

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/issues/{iID}", method = RequestMethod.GET)
    @ResponseBody
    public Issue getIssue(@PathVariable("iID") int iID) throws Exception {
        return is.getByID(iID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/issues", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public int createIssue(@RequestBody Issue issue, @PathVariable("pID") String pID) throws Exception {
        return is.writeIssue(pID, issue);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/issues/{iID}", method = RequestMethod.PATCH)
    @ResponseBody
    public int updateIssueToTask(@PathVariable("pID") String pID, @PathVariable("iID") int iID) throws Exception {
        return is.updateIssueToTask(pID, iID);
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "workspace/projects/{pID}/issues", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Issue> getAllIssuesFromProject(@PathVariable("pID") String pID) {
        return is.getAllIssuesFromProject(pID);
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "workspace/users/{uID}/issues", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Issue> getAllIssuesFromUser(@PathVariable("uID") String uID) {
        return is.getAllIssuesFromUser(uID);
    }

}
