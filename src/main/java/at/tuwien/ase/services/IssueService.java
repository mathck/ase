package at.tuwien.ase.services;

import at.tuwien.ase.model.Issue;

import java.util.LinkedList;


/**
 * Created by DanielHofer on 16.16.11.2015.
 */
public interface IssueService {

    int writeIssue(Issue issue, int pID, String uID);

    boolean deleteIssue(int pID, int iID);

    Issue getByID(int iID);
    LinkedList<Issue> getAllIssues();
    LinkedList<Issue> getAllIssuesFromUser(String uID);

    LinkedList<Issue> getAllIssuesFromProject(int pID);

    int updateIssueToTask(int iID);

    int getNewID();

}
