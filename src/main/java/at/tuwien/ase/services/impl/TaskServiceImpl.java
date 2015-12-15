package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.User;
import at.tuwien.ase.services.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 16.11.2015.
 */

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDAO taskDAO;

    private static final Logger logger = LogManager.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl() {
    }

    public TaskServiceImpl(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public Task getByID(int tID) {
        logger.debug("get task with id=" + tID);
        return taskDAO.findByID(tID);
    }

    public JsonStringWrapper writeTask(int pID, Task task) {
        int id;

        logger.debug("post new task to project with id="+pID);

        id = taskDAO.getNewID();
        task.setId(id);
        task.setCreationDate(new Date());
        task.setUpdateDate(new Date());

        //insert task to db
        taskDAO.insertTask(task);

        if (task.getUserList() != null && !task.getUserList().isEmpty())
        {
            for (User u : task.getUserList())
            {
                //assign user to task
                taskDAO.addUserToTask(u.getUserID(), task.getId());
            }
        }


        return new JsonStringWrapper(id);
    }

    public void deleteTaskByID(int tID) {
        logger.debug("delete task with id="+tID);
        taskDAO.removeTaskByID(tID);
    }

    public LinkedList<Task> getAllTasks() {
        logger.debug("get all tasks");
        return taskDAO.loadAll();
    }

    public LinkedList<Task> getAllTasksFromUser(String uID) {
        logger.debug("get all tasks from user"+uID);
        return taskDAO.loadAllByUser(uID);
    }

    public LinkedList<Task> getAllTasksFromProject(int pID) {
        logger.debug("get all tasks from project");
        return taskDAO.loadAllByProject(pID);
    }

    public LinkedList<Task> getAllTasksFromProjectAndUser(int pID, String uID) {
        logger.debug("get all tasks from project " + pID + " and from user " + uID);
        return taskDAO.loadAllByProjectAndUser(pID, uID);
    }

    public void assignUserToTask(int tID, String uID) {
        logger.debug("assign user with id="+uID+" to task with id="+tID);
        taskDAO.assignUserToTask(tID, uID);
    }

    public void removeUserFromTask(int tID, String uID) {
        logger.debug("remove user with id="+uID+" from task with id="+tID);
        taskDAO.removeUserFromTask(tID, uID);
    }
}