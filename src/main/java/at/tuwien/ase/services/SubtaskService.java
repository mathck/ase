package at.tuwien.ase.services;


import at.tuwien.ase.model.Subtask;


import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface SubtaskService {

    int writeSubtask(Subtask subtask);

    void deleteSubtaskByID(int sID);

    Subtask getByID(int sID);
    LinkedList<Subtask> getAllSubtasks();
    LinkedList<Subtask> getAllSubtasksFromTask(int tID);
    LinkedList<Subtask> getAllSubtasksFromUser(String uID);
    LinkedList<Subtask> getAllSubtasksFromProject(int pID);


}




