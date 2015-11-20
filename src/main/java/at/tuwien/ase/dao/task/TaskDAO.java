package at.tuwien.ase.dao.task;

import at.tuwien.ase.model.task.Task;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 09.11.2015.
 */
public interface TaskDAO {

    int insertTask(Task task);
    boolean removeTask(int tID);

    Task findByID(int tID);
    LinkedList<Task> loadAll();
    LinkedList<Task> loadAllByProject(String pID);
    LinkedList<Task> loadAllByUser(String uID);

    int getNewID();

}
