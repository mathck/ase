package at.tuwien.ase.dao;

import at.tuwien.ase.model.Subtask;
import at.tuwien.ase.model.TaskElementJson;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 20.11.2015.
 */
public interface SubtaskDAO {

    void insertSubtask(Subtask subtask);
    void removeSubtaskByID(int tID);

    Subtask findByID(int tID);
    LinkedList<Subtask> loadAll();
    LinkedList<Subtask> loadAllByTask(int tID);
    LinkedList<Subtask> loadAllByProject(int pID);
    LinkedList<Subtask> loadAllByUser(String uID);

    void addTaskItemToSubtask(TaskElementJson taskItem, int sID) throws Exception;
    int updateTaskItemById(TaskElementJson taskItem) throws Exception;

    int updateSubtaskById(int sID, Subtask subtask) throws Exception;

    int getNewID();
    int getNewIDForTaskItem();

}
