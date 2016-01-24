package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.Issue;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.services.IssueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 14.11.2015.
 */

@RestController
@RequestMapping("/api/")
public class IssueController {

    @Autowired
    private IssueService is;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(TaskController.class);

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/issues/{iID}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasPermission(#iID, 'VIEW_TASK')")
    public Issue getIssueByID(@PathVariable("iID") int iID) throws Exception {
        return is.getByID(iID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/users/issues", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasPermission(#uID, 'CHANGE_USER')")
    public LinkedList<Issue> getIssuesByUser(@RequestParam("uID") String uID) throws Exception {
        return is.getAllIssuesFromUser(uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/issues", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Issue> getAllIssues() throws Exception {
        return is.getAllIssues();
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/users/{uID}/issues", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasPermission(#uID, 'CHANGE_USER') AND hasPermission(#pID, 'VIEW_PROJECT')")
    public LinkedList<Issue> getIssuesByProjectAndUser(@PathVariable("pID") int pID, @PathVariable("uID") String uID) throws Exception {
        return is.getAllIssuesFromProjectAndUser(pID, uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/issues", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    @PreAuthorize("hasPermission(#pID, 'VIEW_PROJECT')")
    public JsonStringWrapper createIssue(@RequestBody Issue issue, @PathVariable("pID") int pID, @RequestParam("uID") String uID) throws Exception  {
        return is.writeIssue(issue, pID, uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/issues/{iID}", method = RequestMethod.PATCH)
    @ResponseBody
    @PreAuthorize("hasPermission(#pID, 'CHANGE_PROJECT')")
    public LinkedList<Integer> updateIssueToTask(@PathVariable("pID") int pID, @PathVariable("iID") int iID, @RequestBody Task task) throws Exception  {
        return is.updateIssueToTask(iID, pID, task);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/issues", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasPermission(#pID, 'CHANGE_PROJECT')")
    public LinkedList<Issue> getAllIssuesFromProject(@PathVariable("pID") int pID) throws Exception  {
        return is.getAllIssuesFromProject(pID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/issues/{iID}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasPermission(#iID, 'VIEW_TASK')")
    public void deleteIssueByID(@PathVariable("iID") int iID)  throws Exception {
        is.deleteIssueByID(iID);
    }

}
