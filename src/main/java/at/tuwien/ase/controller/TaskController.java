package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.dao.task.TaskDAO;

import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.services.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by DanielHofer on 12.11.2015.
 */

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(TaskController.class);


    @RequestMapping(value = "workspace/projects/{projectId}/tasks/{taskId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Task getTask(@PathVariable("projectId") int projectId, @PathVariable("taskId") int taskId) throws Exception {

        return taskService.getTask(taskId);
    }

    @RequestMapping(value = "workspace/projects/{projectId}/tasks", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    Task postTask(@RequestBody Task task, @PathVariable("projectId") int projectId) throws Exception {

        return taskService.postTask(task);
    }

}
