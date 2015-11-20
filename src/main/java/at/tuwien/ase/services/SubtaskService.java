package at.tuwien.ase.services;


import at.tuwien.ase.model.task.Subtask;
import at.tuwien.ase.model.task.Task;


import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface SubtaskService {

    int writeSubtask(String pID, Subtask subtask);

    boolean deleteSubtask(String pID, int sID);

    Subtask getByID(int sID);
    LinkedList<Subtask> getAllSubtasks();
    LinkedList<Subtask> getAllSubtasksFromTask(String tID);
    LinkedList<Subtask> getAllSubtasksFromUser(String uID);
    LinkedList<Subtask> getAllSubtasksFromProject(String pID);

    int getNewID();

}




