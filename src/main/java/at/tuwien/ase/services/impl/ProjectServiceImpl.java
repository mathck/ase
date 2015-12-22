package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.*;
import at.tuwien.ase.model.*;
import at.tuwien.ase.services.IssueService;
import at.tuwien.ase.services.LevelService;
import at.tuwien.ase.services.ProjectService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;

import at.tuwien.ase.services.TaskService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * The implemented project service
 *
 * @author Tomislav Nikic
 * @version 1.0, 14.12.2015
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
    private SubtaskDAO subtaskDAO;

    @Autowired
    private IssueService issueService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private LevelService levelService;

    private static final Logger logger = LogManager.getLogger(ProjectServiceImpl.class);

    public ProjectServiceImpl() {

    }

    /**
     *
     *
     * @param projectDAO
     * @param issueDAO
     * @param taskDAO
     * @param userDAO
     * @param subtaskDAO
     */
    public ProjectServiceImpl(ProjectDAO projectDAO, IssueDAO issueDAO, TaskDAO taskDAO,
                              UserDAO userDAO, SubtaskDAO subtaskDAO) {
        this.projectDAO = projectDAO;
        this.issueDAO = issueDAO;
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
        this.subtaskDAO = subtaskDAO;
    }

    /**
     *
     * @param project
     * @return
     */
    public JsonStringWrapper writeProject(Project project) throws Exception {
        if(project.getTitle() != null && project.getTitle().length() > 3) {
            if(project.getDescription() != null && project.getDescription().length() > 3) {
                project.setCreationTimeDB(new Timestamp(new Date().getTime()));
                project.setUpdateTimeDB(project.getCreationTimeDB());

                int projectID = projectDAO.insertProject(project);

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

                return new JsonStringWrapper(projectID);
            } else {
                throw new IllegalArgumentException("No or too short description for project");
            }
        } else {
            throw new IllegalArgumentException("No or too short title for project");
        }
    }

    /**
     *
     * @param projectID
     */
    public void deleteProject(int projectID) {
        Project project = projectDAO.findByID(projectID);
        project.setAllIssues(issueDAO.loadAllByProject(projectID));
        project.setAllTasks(taskDAO.loadAllByProject(projectID));
        project.setAllUser(userDAO.loadAllByProject(projectID));

        for (Task task : project.getAllTasks()) {
            taskDAO.removeTaskByID(task.getId());
        }
        for (Issue issue : project.getAllIssues()) {
            issueDAO.removeIssueByID(issue.getId());
        }
        for (UserRole user : project.getAllUser()) {
            projectDAO.removeUserFromProject(user.getUser(), project.getProjectID());
        }

        projectDAO.removeProject(projectID);
    }

    /**
     *
     * @param projectID
     * @param project
     */
    public void updateProject(int projectID, Project project) {
        if (project.getTitle() != null && project.getTitle().length() > 3) {
            if (project.getDescription() != null && project.getDescription().length() > 3) {
                project.setUpdateTimeDB(new Timestamp(new Date().getTime()));
                projectDAO.updateProject(projectID, project);
            } else {
                throw new IllegalArgumentException("No or too short description for project");
            }
        } else {
            throw new IllegalArgumentException("No or too short title for project");
        }
    }

    /**
     *
     * @param projectID
     * @param userID
     * @return
     * @throws EmptyResultDataAccessException
     */
    public Project getByID(int projectID, String userID) throws EmptyResultDataAccessException {
        Project project = projectDAO.findByID(projectID);
        project.setAllIssues(issueDAO.loadAllByProject(projectID));
        project.setAllTasks(taskDAO.loadAllByProject(projectID));
        project.setAllUser(userDAO.loadAllByProject(projectID));

        LinkedList<Subtask> listOfSubtasks = subtaskDAO.loadAllByUser(userID);
        int xp = 0;
        if (listOfSubtasks != null && !listOfSubtasks.isEmpty()) {
            for (Subtask subtask : listOfSubtasks) {
                if (subtask.getId() == projectID) {
                    xp += subtask.getXp();
                }
            }
        }
        project.setLevel(levelService.getLevelByXp("Project", xp));

        return project;
    }

    /**
     *
     * @return
     * @throws EmptyResultDataAccessException
     */
    public LinkedList<Project> getAllProjects() throws EmptyResultDataAccessException {
        LinkedList<Project> list = projectDAO.loadAll();
        for (Project iteration : list) {
            iteration.setAllUser(userDAO.loadAllByProject(iteration.getProjectID()));
        }
        return list;
    }

    /**
     *
     * @param userID
     * @return
     * @throws EmptyResultDataAccessException
     */
    public LinkedList<Project> getAllProjectsFromUser(String userID) throws EmptyResultDataAccessException {
        LinkedList<Project> list = projectDAO.loadAllByUser(userID);
        if (list != null && !list.isEmpty()) {
            for (Project project : list) {
                project.setAllUser(userDAO.loadAllByProject(project.getProjectID()));
            }
        }
        return list;
    }

    /**
     *
     * @param projectID
     * @param userID
     * @param role
     */
    public void addUser(int projectID, String userID, String role)
            throws EmptyResultDataAccessException, IllegalArgumentException {
        if(userID != null && userID.length() > 5) {
            if (role != null && role.length() > 3) {
                LinkedList<UserRole> userList = projectDAO.findByID(projectID).getAllUser();
                if (userList != null) {
                    for (UserRole userRole : userList) {
                        if (userRole.getUser() == userID) {
                            return;
                        }
                    }
                }
                projectDAO.addUserToProject(userID, projectID, role);
                this.updateTime(projectID);
            } else {
                throw new IllegalArgumentException("Role parameter is not applicable");
            }
        } else {
            throw new IllegalArgumentException("User ID parameter is not applicable");
        }
    }

    /**
     *
     * @param projectID
     * @param userID
     */
    public void removeUser(int projectID, String userID) {
        for (Task task : taskDAO.loadAllByProjectAndUser(projectID, userID)) {
            taskDAO.removeTaskByID(task.getId());
        }
        for (Issue issue : issueDAO.loadAllByProjectAndUser(projectID, userID)) {
            issueDAO.removeIssueByID(issue.getId());
        }
        projectDAO.removeUserFromProject(userID, projectID);
        this.updateTime(projectID);
    }

    public void updateTime(int projectID) {
        Project project = new Project();
        project.setUpdateTimeDB(new Timestamp(new Date().getTime()));
        this.updateProject(projectID, project);
    }

}
