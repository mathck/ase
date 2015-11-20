package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.task.ProjectDAO;
import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.services.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 16.16.11.2015.
 */

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private ProjectDAO projectDAO;

    private static final Logger logger = LogManager.getLogger(TaskServiceImpl.class);

    public Task getByID(int tID) {
        logger.debug("get task with id=" + tID);
        return taskDAO.findByID(tID);
    }

    public int writeTask(String pID, Task task) {
        logger.debug("post new task with id=" + task.getId());

        Project project = projectDAO.findByID(pID);

        task.setId(taskDAO.getNewID());
        project.addTask(task);

        projectDAO.removeProject(pID);
        projectDAO.insertProject(project);
        return taskDAO.insertTask(task);
    }

    public boolean deleteTask(String pID, int tID) {
        Project project = projectDAO.findByID(pID);
        project.deleteTask(tID);
        projectDAO.removeProject(pID);
        projectDAO.insertProject(project);
        return taskDAO.removeTask(tID);
    }

    public LinkedList<Task> getAllTasks() {
        return taskDAO.loadAll();
    }

    public LinkedList<Task> getAllTasksFromUser(String uID) {
        return taskDAO.loadAllByUser(uID);
    }

    public LinkedList<Task> getAllTasksFromProject(String pID) {
        return taskDAO.loadAllByProject(pID);
    }

    public int getNewID() {
        return taskDAO.getNewID();
    }

}
