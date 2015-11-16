package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.services.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DanielHofer on 16.16.11.2015.
 */

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDAO taskDAO;

    private static final Logger logger = LogManager.getLogger(TaskServiceImpl.class);

    public Task getTask(int taskId) {
        logger.debug("get task with id=" + taskId);

        //find task by id
        Task task = taskDAO.findByTaskId(taskId);

        return task;
    }

    public Task postTask(Task task) {
        logger.debug("post new task with id=" + task.getId());

        //db insert and return id
        task.setId(taskDAO.insertTask(task));

        return task;
    }
}
