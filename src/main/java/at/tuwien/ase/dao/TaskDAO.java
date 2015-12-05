package at.tuwien.ase.dao;

import at.tuwien.ase.model.Task;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 09.11.2015.
 */
public interface TaskDAO {

    void insertTask(Task task);
    boolean removeTask(int tID);

    Task findByID(int tID);
    LinkedList<Task> loadAll();

    LinkedList<Task> loadAllByProject(int pID);
    LinkedList<Task> loadAllByUser(String uID);

    void updateIssueToTask(int iID);

    int getNewID();

}