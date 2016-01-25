package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.Issue;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.security.PermissionEvaluator;
import at.tuwien.ase.services.IssueService;
import at.tuwien.ase.services.LoginService;
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
    private LoginService loginService;
    @Autowired
    private PermissionEvaluator permissionEvaluator;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(TaskController.class);

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/issues/{iID}", method = RequestMethod.GET)
    @ResponseBody
    public Issue getIssueByID(@PathVariable("iID") int iID, @RequestHeader("user-token") String token) throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),iID,"VIEW_TASK")) {
            throw new ValidationException("Not allowed");
        }
        return is.getByID(iID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/users/issues", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Issue> getIssuesByUser(@RequestParam("uID") String uID, @RequestHeader("user-token") String token)
            throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),uID,"CHANGE_USER")) {
            throw new ValidationException("Not allowed");
        }
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
    public LinkedList<Issue> getIssuesByProjectAndUser(@PathVariable("pID") int pID, @PathVariable("uID") String uID, @RequestHeader("user-token") String token)
            throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token), uID, "CHANGE_USER") ||
                !permissionEvaluator.hasPermission(loginService.getUserIdByToken(token), pID, "VIEW_PROJECT")) {
            throw new ValidationException("Not allowed");
        }
        return is.getAllIssuesFromProjectAndUser(pID, uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/issues", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public JsonStringWrapper createIssue(@RequestBody Issue issue, @PathVariable("pID") int pID, @RequestParam("uID") String uID, @RequestHeader("user-token") String token)
            throws Exception  {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),pID,"VIEW_PROJECT")) {
            throw new ValidationException("Not allowed");
        }
        return is.writeIssue(issue, pID, uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/issues/{iID}", method = RequestMethod.PATCH)
    @ResponseBody
    public LinkedList<Integer> updateIssueToTask(@PathVariable("pID") int pID, @PathVariable("iID") int iID, @RequestBody Task task, @RequestHeader("user-token") String token)
            throws Exception  {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),pID,"CHANGE_PROJECT")) {
            throw new ValidationException("Not allowed");
        }
        return is.updateIssueToTask(iID, pID, task);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/issues", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Issue> getAllIssuesFromProject(@PathVariable("pID") int pID, @RequestHeader("user-token") String token)
            throws Exception  {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),pID,"CHANGE_PROJECT")) {
            throw new ValidationException("Not allowed");
        }
        return is.getAllIssuesFromProject(pID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/issues/{iID}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteIssueByID(@PathVariable("iID") int iID, @RequestHeader("user-token") String token)  throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),iID,"VIEW_TASK")) {
            throw new ValidationException("Not allowed");
        }
        is.deleteIssueByID(iID);
    }

}
