package at.tuwien.ase.services;

import at.tuwien.ase.model.Issue;

import java.util.LinkedList;


/**
 * Created by DanielHofer on 16.11.2015.
 */
public interface IssueService {

    int writeIssue(Issue issue, int pID, String uID);

    void deleteIssueByID(int iID);

    Issue getByID(int iID);
    LinkedList<Issue> getAllIssues();
    LinkedList<Issue> getAllIssuesFromUser(String uID);

    LinkedList<Issue> getAllIssuesFromProject(int pID);

    LinkedList<Issue> getAllIssuesFromProjectAndUser(int pID, String uID);

    int updateIssueToTask(int iID);

}
