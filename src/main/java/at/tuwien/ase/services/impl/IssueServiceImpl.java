package at.tuwien.ase.services.impl;

import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.dao.IssueDAO;
import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.dao.impl.IssueDAOImpl;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Issue;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.services.IssueService;
import at.tuwien.ase.services.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by Daniel Hofer on 16.11.2015.
 */

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueDAO issueDAO;

    @Autowired
    private TaskDAO taskDAO;

    @Autowired
    private TaskService ts;

    @Autowired
    private javax.validation.Validator validator;


    public IssueServiceImpl() {
    }

    public IssueServiceImpl(IssueDAO issueDAO, TaskDAO taskDAO) {
        this.issueDAO = issueDAO;
        this.taskDAO = taskDAO;
    }

    private static final Logger logger = LogManager.getLogger(IssueServiceImpl.class);

    public Issue getByID(int iID) {
        logger.debug("get issue with id="+iID);
        return issueDAO.findByID(iID);
    }

    public LinkedList<Integer> updateIssueToTask(int iID, int pID, Task task) throws Exception {
        logger.debug("update issue with id="+iID+" to task");

        //remove issue
        issueDAO.removeIssueByID(iID);

        //insert task
        return ts.writeTask(pID, task);

    }

    public JsonStringWrapper writeIssue(Issue issue, int pID, String uID) throws ValidationException{
        int id;

        //Validate Json
        Set<ConstraintViolation<Issue>> constraintViolationsSubtask = validator.validate(issue);
        if (!constraintViolationsSubtask.isEmpty()){
            Iterator<ConstraintViolation<Issue>> flavoursIter = constraintViolationsSubtask.iterator();
            String validationError = new String("");

            while (flavoursIter.hasNext()){
                ConstraintViolation<Issue> violation =  flavoursIter.next();
                validationError += violation.getPropertyPath()+": "+violation.getMessage()+"\n";
            }

            throw new ValidationException(validationError);
        }

        logger.debug("post new issue");

        issue.setCreationDate(new Date());
        issue.setUpdateDate(new Date());
        issue.setProjectId(pID);
        issue.setUserId(uID);

        id = issueDAO.insertIssue(issue);

        return new JsonStringWrapper(id);
    }

    public void deleteIssueByID(int iID)  {
        logger.debug("delete issue with id="+iID);
        issueDAO.removeIssueByID(iID);
    }

    public LinkedList<Issue> getAllIssues() {
        logger.debug("get all issues");
        return issueDAO.loadAll();
    }

    public LinkedList<Issue> getAllIssuesFromUser(String uID) {
        logger.debug("get all issues from user " + uID);
        return issueDAO.loadAllByUser(uID);
    }

    public LinkedList<Issue> getAllIssuesFromProject(int pID) {
        logger.debug("get all issues from project " + pID);
        return issueDAO.loadAllByProject(pID);
    }

    public LinkedList<Issue> getAllIssuesFromProjectAndUser(int pID, String uID) {
        logger.debug("get all issues from project " + pID + " and from user " + uID);
        return issueDAO.loadAllByProjectAndUser(pID, uID);
    }

}
