package at.tuwien.ase.services;

import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 16.16.11.2015.
 */
public interface TaskService {

    int writeTask(String pID, Task task);

    boolean deleteTask(String pID, int tID);

    Task getByID(int tID);
    LinkedList<Task> getAllTasks();
    LinkedList<Task> getAllTasksFromUser(String uID);
    LinkedList<Task> getAllTasksFromProject(String pID);

    int getNewID();

}




