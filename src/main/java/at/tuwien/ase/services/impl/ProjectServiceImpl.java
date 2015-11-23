package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.task.IssueDAO;
import at.tuwien.ase.dao.task.ProjectDAO;
import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.model.project.Role;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.model.user.User;
import at.tuwien.ase.services.ProjectService;

import java.util.LinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private IssueDAO issueDAO;
    @Autowired
    private TaskDAO taskDAO;

    private static final Logger logger = LogManager.getLogger(ProjectServiceImpl.class);

    public ProjectServiceImpl() {

    }

    public ProjectServiceImpl(ProjectDAO projectDAO, IssueDAO issueDAO, TaskDAO taskDAO) {
        this.projectDAO = projectDAO;
        this.issueDAO = issueDAO;
        this.taskDAO = taskDAO;
    }

    public Project writeProject(Project project) {
        logger.debug("create project with id " + project.getId());
        return projectDAO.insertProject(project);
    }

    public boolean deleteProject(String pID) {
        Project project = projectDAO.findByID(pID);
        for(Task task : project.getAllTasks())
            taskDAO.removeTask(task.getId());
        for(Issue issue : project.getAllIssues())
            issueDAO.removeIssue(issue.getId());
        return projectDAO.removeProject(pID);
    }

    public Project getByID(String pID) {
        return projectDAO.findByID(pID);
    }

    public LinkedList<Project> getAllProjects() {
        return projectDAO.loadAll();
    }

    public LinkedList<Project> getAllProjectsFromUser(String uID) {
        return projectDAO.loadAllByUser(uID);
    }

    public String addUser(String pID, String uID, Role role) {
        Project project = projectDAO.findByID(pID);
        project.addUser(uID, role);
        return uID;
    }

}
