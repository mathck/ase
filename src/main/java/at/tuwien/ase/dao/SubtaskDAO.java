package at.tuwien.ase.dao;

import at.tuwien.ase.model.Subtask;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskElementJson;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel Hofer on 20.11.2015.
 */
public interface SubtaskDAO {
    void insertSubtask(Subtask subtask);
    void insertSubtaskBatch(final List<Subtask> subtaskList);
    void removeSubtaskByID(int tID);

    Subtask findByID(int tID);
    LinkedList<Subtask> loadAll();
    LinkedList<Subtask> loadAllByTask(int tID);
    LinkedList<Subtask> loadAllByProject(int pID);
    LinkedList<Subtask> loadAllByUser(String uID);

    void addTaskItemToSubtask(TaskElementJson taskItem, int sID) throws Exception;
    void addTaskItemToSubtaskBatch(final List<TaskElementJson> taskElementJsonList) throws Exception;
    int updateTaskItemById(TaskElementJson taskItem) throws Exception;

    int updateSubtaskById(int sID, Subtask subtask) throws Exception;

    int getNewID();
    int getNewIDForTaskItem();

}
