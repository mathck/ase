package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;

import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Subtask;
import at.tuwien.ase.model.SubtaskUpdate;
import at.tuwien.ase.security.PermissionEvaluator;
import at.tuwien.ase.services.LoginService;
import at.tuwien.ase.services.SubtaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 20.11.2015.
 */

@RestController
public class SubtaskController {

    @Autowired
    private SubtaskService ts;
    @Autowired
    private LoginService loginService;
    @Autowired
    private PermissionEvaluator permissionEvaluator;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(SubtaskController.class);

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/tasks/subtasks/{sID}", method = RequestMethod.GET)
    @ResponseBody
    public Subtask getSubtaskByID(@PathVariable("sID") int sID, @RequestHeader("user-token") String token) throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),sID,"VIEW_SUBTASK")) {
            throw new ValidationException("Not allowed");
        }
        return ts.getByID(sID);
    }
/*
    // @author Daniel Hofer
    @RequestMapping(value = "workspace/tasks/subtasks", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public JsonStringWrapper createSubtask(@RequestBody Subtask subtask) throws Exception {
        return ts.writeSubtask(subtask);
    }*/

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/tasks/subtasks", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Subtask> getAllSubtasks() throws Exception {
        return ts.getAllSubtasks();
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/tasks/{tID}/subtasks", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Subtask> getAllSubtasksFromTask(@PathVariable("tID") int tID, @RequestHeader("user-token") String token) throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),tID,"VIEW_TASK")) {
            throw new ValidationException("Not allowed");
        }
        return ts.getAllSubtasksFromTask(tID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/subtasks", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Subtask> getAllSubtasksFromProject(@PathVariable("pID") int pID, @RequestHeader("user-token") String token) throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),pID,"CHANGE_PROJECT")) {
            throw new ValidationException("Not allowed");
        }
        return ts.getAllSubtasksFromProject(pID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/users/{uID}/subtasks", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Subtask> getAllSubtasksFromUser(@PathVariable("uID") String uID, @RequestHeader("user-token") String token) throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),uID,"CHANGE_USER")) {
            throw new ValidationException("Not allowed");
        }
        return ts.getAllSubtasksFromUser(uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/subtasks/{sID}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteSubtasksByID(@PathVariable("sID") int sID, @RequestHeader("user-token") String token)  throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),sID,"CHANGE_SUBTASK")) {
            throw new ValidationException("Not allowed");
        }
        ts.deleteSubtaskByID(sID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/tasks/subtasks/{sID}", method = RequestMethod.PATCH)
    @ResponseBody
    public void updateSubtask(@PathVariable("sID") int sID, @RequestBody SubtaskUpdate subtask, @RequestHeader("user-token") String token) throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),sID,"CHANGE_SUBTASK")) {
            throw new ValidationException("Not allowed");
        }
        ts.updateSubtask(sID, subtask);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/subtasks/{sID}", method = RequestMethod.PATCH)
    @ResponseBody
    public void closeSubtask(@PathVariable("sID") int sID, @RequestHeader("user-token") String token) throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),sID,"VIEW_SUBTASK")) {
            throw new ValidationException("Not allowed");
        }
        ts.closeSubtask(sID);
    }

}
