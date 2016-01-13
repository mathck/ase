package at.tuwien.ase.services;


import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Subtask;
import at.tuwien.ase.model.SubtaskUpdate;


import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface SubtaskService {

    JsonStringWrapper writeSubtask(Subtask subtask);

    void deleteSubtaskByID(int sID);

    void updateSubtask(int sID, SubtaskUpdate subtask) throws Exception;

    Subtask getByID(int sID);
    LinkedList<Subtask> getAllSubtasks();
    LinkedList<Subtask> getAllSubtasksFromTask(int tID);
    LinkedList<Subtask> getAllSubtasksFromUser(String uID);
    LinkedList<Subtask> getAllSubtasksFromProject(int pID);


}




