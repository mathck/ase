package at.tuwien.ase.dao;

import at.tuwien.ase.model.Issue;

import java.util.LinkedList;


/**
 * Created by Daniel Hofer on 14.14.11.2015.
 */
public interface IssueDAO {

    void insertIssue(Issue issue);
    boolean removeIssue(int iID);

    Issue findByID(int iID);
    LinkedList<Issue> loadAll();

    LinkedList<Issue> loadAllByProject(int pID);
    LinkedList<Issue> loadAllByUser(String uID);

    int getNewID();

}