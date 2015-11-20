package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.dao.task.TaskDAO;

import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.services.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 12.11.2015.
 */

@RestController
public class TaskController {

    @Autowired
    private TaskService ts;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(TaskController.class);

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/tasks/{tID}", method = RequestMethod.GET)
    @ResponseBody
    public Task getTask(@PathVariable("tID") int tID) throws Exception {
        return ts.getByID(tID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/tasks", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public int createTask(@RequestBody Task task, @PathVariable("pID") String pID) throws Exception {
        return ts.writeTask(pID, task);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/tasks", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Task> getAllTasksFromProject(@PathVariable("pID") String pID) {
        return ts.getAllTasksFromProject(pID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/users/{uID}/tasks", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Task> getAllTasksFromUser(@PathVariable("uID") String uID) {
        return ts.getAllTasksFromUser(uID);
    }

}
