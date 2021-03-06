package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;

import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Subtask;
import at.tuwien.ase.model.SubtaskUpdate;
import at.tuwien.ase.services.SubtaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 20.11.2015.
 */

@RestController
@RequestMapping("/api/")
public class SubtaskController {

    @Autowired
    private SubtaskService ts;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(SubtaskController.class);

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/tasks/subtasks/{sID}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasPermission(#sID, 'VIEW_SUBTASK')")
    public Subtask getSubtaskByID(@PathVariable("sID") int sID) throws Exception {
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
    @PreAuthorize("hasPermission(#tID, 'VIEW_TASK')")
    public LinkedList<Subtask> getAllSubtasksFromTask(@PathVariable("tID") int tID) throws Exception {
        return ts.getAllSubtasksFromTask(tID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/subtasks", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasPermission(#pID, 'CHANGE_PROJECT')")
    public LinkedList<Subtask> getAllSubtasksFromProject(@PathVariable("pID") int pID) throws Exception {
        return ts.getAllSubtasksFromProject(pID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/users/{uID}/subtasks", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasPermission(#uID, 'CHANGE_USER')")
    public LinkedList<Subtask> getAllSubtasksFromUser(@PathVariable("uID") String uID) throws Exception {
        return ts.getAllSubtasksFromUser(uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/subtasks/{sID}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasPermission(#sID, 'CHANGE_SUBTASK')")
    public void deleteSubtasksByID(@PathVariable("sID") int sID)  throws Exception {
        ts.deleteSubtaskByID(sID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/tasks/subtasks/{sID}", method = RequestMethod.PATCH)
    @ResponseBody
    @PreAuthorize("hasPermission(#sID, 'CHANGE_SUBTASK')")
    public void updateSubtask(@PathVariable("sID") int sID, @RequestBody SubtaskUpdate subtask) throws Exception {
        ts.updateSubtask(sID, subtask);
    }

}
