package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;

import at.tuwien.ase.model.Comment;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.User;
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
    public Task getTaskByID(@PathVariable("tID") int tID) throws Exception {
        return ts.getByID(tID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/users/{uID}/tasks", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Task> getTasksByProjectAndUser(@PathVariable("pID") int pID, @PathVariable("uID") String uID) throws Exception {
        return ts.getAllTasksFromProjectAndUser(pID, uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/tasks", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public LinkedList<Integer> createTask(@RequestBody Task task, @PathVariable("pID") int pID) throws Exception {
       return ts.writeTask(pID, task);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/tasks", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Task> getAllTasks()  throws Exception {
        return ts.getAllTasks();
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/tasks", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Task> getAllTasksFromProject(@PathVariable("pID") int pID)  throws Exception {
        return ts.getAllTasksFromProject(pID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/users/{uID}/tasks", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Task> getAllTasksAssignedToUser(@PathVariable("uID") String uID)  throws Exception {
        return ts.getAllTasksFromUser(uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/users/{uID}/tasks/{tID}", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void assignUserToTask(@PathVariable("tID") int tID, @PathVariable("uID") String uID) throws Exception {
        ts.assignUserToTask(tID, uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/users/{uID}/tasks/{tID}", method = RequestMethod.DELETE)
    @ResponseBody
    public void reomveUserFromTask(@PathVariable("tID") int tID, @PathVariable("uID") String uID) throws Exception {
        ts.removeUserFromTask(tID, uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/tasks/{tID}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteTasksByID(@PathVariable("tID") int tID)  throws Exception {
        ts.deleteTaskByID(tID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/tasks/{tID}/comments", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public JsonStringWrapper addCommentToTask(@PathVariable("tID") int tID, @RequestBody Comment comment) throws Exception {
        return ts.addCommentToTask(tID, comment);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/tasks/{tID}/comments/{cID}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteCommentFromTask(@PathVariable("tID") int tID, @PathVariable("cID") int cID)  throws Exception {
        ts.deleteCommentFromTask(tID, cID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/tasks/{tID}/comments", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Comment> getAllCommentsByTask(@PathVariable("tID") int tID)  throws Exception {
        return ts.getAllCommentsByTask(tID);
    }


    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/tasks/{tID}/users", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<User> getUsersFromTask(@PathVariable("tID") int tID)  throws Exception {
        return ts.getAllUserFromTask(tID);
    }

}
