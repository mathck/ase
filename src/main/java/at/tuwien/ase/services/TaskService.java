package at.tuwien.ase.services;

import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Task;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 16.11.2015.
 */
public interface TaskService {

    void writeTask(int pID, Task task) throws Exception;

    void deleteTaskByID(int tID);

    Task getByID(int tID);
    LinkedList<Task> getAllTasks();
    LinkedList<Task> getAllTasksFromUser(String uID);

    LinkedList<Task> getAllTasksFromProject(int pID);

    LinkedList<Task> getAllTasksFromProjectAndUser(int pID, String uID);

    void assignUserToTask(int tID, String uID);
    void removeUserFromTask(int tID, String uID);


}




