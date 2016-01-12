package at.tuwien.ase.dao;

import at.tuwien.ase.model.Issue;

import java.util.LinkedList;


/**
 * Created by Daniel Hofer on 14.11.2015.
 */
public interface IssueDAO {

    int insertIssue(final Issue issue);
    void removeIssueByID(int iID);

    Issue findByID(int iID);
    LinkedList<Issue> loadAll();

    LinkedList<Issue> loadAllByProject(int pID);
    LinkedList<Issue> loadAllByUser(String uID);
    LinkedList<Issue> loadAllByProjectAndUser(int pID, String uID);

}
