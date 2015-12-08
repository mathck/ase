package at.tuwien.ase.dao;

import at.tuwien.ase.model.Subtask;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 20.11.2015.
 */
public interface SubtaskDAO {

    void insertSubtask(Subtask subtask);
    void removeSubtaskByID(int tID);

    Subtask findByID(int tID);
    LinkedList<Subtask> loadAll();
    LinkedList<Subtask> loadAllByTask(String tID);
    LinkedList<Subtask> loadAllByProject(String pID);
    LinkedList<Subtask> loadAllByUser(String uID);

    int getNewID();

}
