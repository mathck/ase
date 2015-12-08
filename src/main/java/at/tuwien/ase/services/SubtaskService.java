package at.tuwien.ase.services;


import at.tuwien.ase.model.Subtask;


import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface SubtaskService {

    int writeSubtask(Subtask subtask);

    void deleteSubtaskByID(String pID, int sID);

    Subtask getByID(int sID);
    LinkedList<Subtask> getAllSubtasks();
    LinkedList<Subtask> getAllSubtasksFromTask(String tID);
    LinkedList<Subtask> getAllSubtasksFromUser(String uID);
    LinkedList<Subtask> getAllSubtasksFromProject(String pID);


}




