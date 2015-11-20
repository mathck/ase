package at.tuwien.ase.services.impl;

import at.tuwien.ase.controller.TaskController;
import at.tuwien.ase.dao.task.IssueDAO;
import at.tuwien.ase.dao.task.ProjectDAO;
import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.services.IssueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 16.16.11.2015.
 */

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueDAO issueDAO;
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private ProjectDAO projectDAO;

    private static final Logger logger = LogManager.getLogger(IssueServiceImpl.class);

    public Issue getByID(int iID) {
        logger.debug("get issue with id="+iID);
        return issueDAO.findByID(iID);
    }

    public int updateIssue(String pID, int iID) {
        logger.debug("update issue with id="+iID);
        Project project = projectDAO.findByID(pID);
        Issue issue = issueDAO.findByID(iID);
        Task task = new Task(issue.getTitle(),issue.getDescription());
        task.setId(taskDAO.getNewID());

        project.deleteIssue(issue.getId());
        issueDAO.removeIssue(issue.getId());
        projectDAO.removeProject(pID);

        taskDAO.insertTask(task);
        project.addTask(task);
        projectDAO.insertProject(project);

        return task.getId();
    }

    public int getNewID() {
        return issueDAO.getNewID();
    }

    public int writeIssue(String pID, Issue issue) {
        logger.debug("post new issue with id="+issue.getId());
        Project project = projectDAO.findByID(pID);

        issue.setId(issueDAO.getNewID());
        project.addIssue(issue);

        projectDAO.removeProject(pID);
        projectDAO.insertProject(project);

        return issueDAO.insertIssue(issue);
    }

    public boolean deleteIssue(String pID, int iID) {
        logger.debug("delete issue with id="+iID);
        Project project = projectDAO.findByID(pID);
        project.deleteIssue(iID);
        projectDAO.removeProject(pID);
        projectDAO.insertProject(project);

        return issueDAO.removeIssue(iID);
    }

    public LinkedList<Issue> getAllIssues() {
        logger.debug("get all issues");
        return issueDAO.loadAll();
    }

    public LinkedList<Issue> getAllIssuesFromUser(String uID) {
        logger.debug("get all issues from user " + uID);
        return issueDAO.loadAllByUser(uID);
    }

    public LinkedList<Issue> getAllIssuesFromProject(String pID) {
        logger.debug("get all issues from project " + pID);
        return issueDAO.loadAllByProject(pID);
    }

}
