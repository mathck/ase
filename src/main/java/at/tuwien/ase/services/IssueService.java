package at.tuwien.ase.services;

import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.Issue;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Task;

import java.util.LinkedList;


/**
 * Created by DanielHofer on 16.11.2015.
 */
public interface IssueService {

    JsonStringWrapper writeIssue(Issue issue, int pID, String uID) throws ValidationException;

    void deleteIssueByID(int iID);

    Issue getByID(int iID);
    LinkedList<Issue> getAllIssues();
    LinkedList<Issue> getAllIssuesFromUser(String uID);

    LinkedList<Issue> getAllIssuesFromProject(int pID);

    LinkedList<Issue> getAllIssuesFromProjectAndUser(int pID, String uID);

    LinkedList<Integer> updateIssueToTask(int iID, int pID, Task task) throws Exception;

}
