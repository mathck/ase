package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.task.IssueDAO;
import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.services.IssueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public int updateIssueToTask(int iID)
    {
        logger.debug("update issue with id="+iID+" to task");

        //update issue to task
        taskDAO.updateIssueToTask(iID);

       /* //get project and delete issue
        Project project = projectDAO.findByID(pID);
        project.deleteIssue(iID);

        //get updated task and add it to project
 /      Task t = taskDAO.findByID(iID);

        project.addTask(t);

        //delete old project
        projectDAO.removeProject(pID);

        //insert new project
        projectDAO.insertProject(project);
*/

        return iID;


    }

    public int getNewID() {
        return issueDAO.getNewID();
    }

    public int writeIssue(Issue issue, int pID, String uID)
    {
        int id;

        logger.debug("post new issue");

        id = issueDAO.getNewID();
        issue.setId(id);
        issue.setCreationDate(new Date());
        issue.setUpdateDate(new Date());
        issue.setProjectId(pID);
        issue.setUserId(uID);
/*
        Project project = projectDAO.findByID(pID);
        project.addIssue(issue);
        projectDAO.removeProject(pID);
        projectDAO.insertProject(project);*/

        issueDAO.insertIssue(issue);

        return id;
    }

    public boolean deleteIssue(int pID, int iID)
    {
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

    public LinkedList<Issue> getAllIssuesFromProject(int pID)
    {
        logger.debug("get all issues from project " + pID);
        return issueDAO.loadAllByProject(pID);
    }

}
