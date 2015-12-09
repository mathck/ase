package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.dao.IssueDAO;
import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.model.*;
import at.tuwien.ase.services.IssueService;
import at.tuwien.ase.services.ProjectService;

import java.util.LinkedList;

import at.tuwien.ase.services.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private IssueService issueService;
    @Autowired
    private TaskService taskService;

    private static final Logger logger = LogManager.getLogger(ProjectServiceImpl.class);

    public ProjectServiceImpl() {

    }

    public ProjectServiceImpl(ProjectDAO projectDAO, IssueDAO issueDAO, TaskDAO taskDAO, UserDAO userDAO) {
        this.projectDAO = projectDAO;
        this.issueDAO = issueDAO;
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
    }

    public JsonStringWrapper writeProject(Project project) {
        int id = projectDAO.insertProject(project);
        if (project.getAllUser() != null && !project.getAllUser().isEmpty()) {
            for (UserRole user : project.getAllUser()) {
                projectDAO.addUserToProject(user.getUser(), user.getProject(), user.getRole());
            }
        }
        if (project.getAllIssues() != null && !project.getAllIssues().isEmpty()) {
            for (Issue issue : project.getAllIssues()) {
                issueService.writeIssue(issue, project.getProjectID(), issue.getUserId());
            }
        }
        if (project.getAllTasks() != null && !project.getAllTasks().isEmpty()) {
            for (Task task : project.getAllTasks()) {
                taskService.writeTask(project.getProjectID(), task);
            }
        }
        return new JsonStringWrapper(id);
    }

    public void deleteProject(int pID) {
        Project project = projectDAO.findByID(pID);
        for (Task task : project.getAllTasks()) {
            taskDAO.removeTaskByID(task.getId());
        }
        for (Issue issue : project.getAllIssues()) {
            issueDAO.removeIssueByID(issue.getId());
        }
        for (UserRole user : project.getAllUser()) {
            projectDAO.removeUserFromProject(user.getUser(), project.getProjectID());
        }
        projectDAO.removeProject(pID);
    }

    public void updateProject(int pID, Project project) {
        projectDAO.updateProject(pID, project);
    }

    public Project getByID(int pID) throws EmptyResultDataAccessException {
        Project project = projectDAO.findByID(pID);
        project.setAllIssues(issueDAO.loadAllByProject(pID));
        project.setAllTasks(taskDAO.loadAllByProject(pID));
        project.setAllUser(userDAO.loadAllByProject(pID));
        return project;
    }

    public LinkedList<Project> getAllProjects() throws EmptyResultDataAccessException {
        LinkedList<Project> list = projectDAO.loadAll();
        for (Project iteration : list) {
            iteration.setAllUser(userDAO.loadAllByProject(iteration.getProjectID()));
        }
        return list;
    }

    public LinkedList<Project> getAllProjectsFromUser(String uID) throws EmptyResultDataAccessException {
        return projectDAO.loadAllByUser(uID);
    }

    public void addUser(int pID, String uID, String role) {
        LinkedList<UserRole> userList = projectDAO.findByID(pID).getAllUser();
        if(userList != null) {
            for (UserRole userRole : userList) {
                if(userRole.getUser() == uID) {
                    return;
                }
            }
        }
        projectDAO.addUserToProject(uID, pID, role);
    }

    public void removeUser(int pID, String uID) {
        for(Task task : taskDAO.loadAllByProjectAndUser(pID, uID)) {
            taskDAO.removeTaskByID(task.getId());
        }
        for(Issue issue : issueDAO.loadAllByProjectAndUser(pID, uID)) {
            issueDAO.removeIssueByID(issue.getId());
        }
        projectDAO.removeUserFromProject(uID, pID);
    }

}
