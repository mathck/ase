package at.tuwien.ase.services;

import at.tuwien.ase.controller.TaskController;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;

import java.util.LinkedList;


/**
 * Created by DanielHofer on 16.16.11.2015.
 */
public interface IssueService {

    int writeIssue(String pID, Issue issue);

    boolean deleteIssue(String pID, int iID);

    Issue getByID(int iID);
    LinkedList<Issue> getAllIssues();
    LinkedList<Issue> getAllIssuesFromUser(String uID);
    LinkedList<Issue> getAllIssuesFromProject(String pID);

    int updateIssueToTask(String pID, int iID, String uID);

    int getNewID();

}
