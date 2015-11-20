package at.tuwien.ase.dao.task;

import at.tuwien.ase.model.task.Issue;

import java.util.LinkedList;


/**
 * Created by Daniel Hofer on 14.14.11.2015.
 */
public interface IssueDAO {

    int insertIssue(Issue issue);
    boolean removeIssue(int iID);

    Issue findByID(int iID);
    LinkedList<Issue> loadAll();
    LinkedList<Issue> loadAllByProject(String pID);
    LinkedList<Issue> loadAllByUser(String uID);

    int getNewID();

}
