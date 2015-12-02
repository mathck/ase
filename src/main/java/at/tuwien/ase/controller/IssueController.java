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
    @RequestMapping(value = "workspace/projects/issues", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Issue> getIssue(@RequestParam("uID") String uID) throws Exception {
        return is.getAllIssuesFromUser(uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/issues", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public int createIssue(@RequestBody Issue issue, @PathVariable("pID") int pID, @RequestParam("uID") String uID) throws Exception  {
        return is.writeIssue(issue, pID, uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/issues/{iID}", method = RequestMethod.PATCH)
    @ResponseBody
    public int updateIssueToTask(@PathVariable("iID") int iID) throws Exception  {
        return is.updateIssueToTask(iID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/issues", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Issue> getAllIssuesFromProject(@PathVariable("pID") int pID) throws Exception  {
        return is.getAllIssuesFromProject(pID);
    }


}
