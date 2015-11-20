package at.tuwien.ase.dao.task;

import at.tuwien.ase.model.task.Subtask;
import at.tuwien.ase.model.task.Task;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 20.11.2015.
 */
public interface SubtaskDAO {

    void insertSubtask(Subtask subtask);
    boolean removeSubtask(int tID);

    Subtask findByID(int tID);
    LinkedList<Subtask> loadAll();
    LinkedList<Subtask> loadAllByTask(String tID);
    LinkedList<Subtask> loadAllByProject(String pID);
    LinkedList<Subtask> loadAllByUser(String uID);

    int getNewID();

}
