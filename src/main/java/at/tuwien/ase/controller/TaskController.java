package at.tuwien.ase.controller;

import at.tuwien.ase.dao.task.TaskDAO;

import at.tuwien.ase.model.task.Task;
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
    private TaskDAO taskDAO;

    private static final Logger logger = LogManager.getLogger(TaskController.class);


    @RequestMapping(value = "workspace/projects/{projectId}/tasks/{taskId}", method = RequestMethod.GET)
    public @ResponseBody Task task(@PathVariable("projectId") int projectId, @PathVariable("taskId") int taskId) {

        logger.debug("get task with id="+taskId);

        //find task by id
        Task task = taskDAO.findByTaskId(taskId);

        return task;
    }

    @RequestMapping(value = "workspace/projects/{projectId}/tasks", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody Task task(@RequestBody Task task, @PathVariable("projectId") int projectId) {

        logger.debug("post new task with id="+task.getId());

        //db insert and return id
        task.setId(taskDAO.insertTask(task));

        return task;
    }

}
