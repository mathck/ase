package at.tuwien.ase.services;

import at.tuwien.ase.model.Task;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 16.11.2015.
 */
public interface TaskService {

    int writeTask(int pID, Task task);

    boolean deleteTask(int pID, int tID);

    Task getByID(int tID);
    LinkedList<Task> getAllTasks();
    LinkedList<Task> getAllTasksFromUser(String uID);

    LinkedList<Task> getAllTasksFromProject(int pID);

    int getNewID();

}




