package at.tuwien.ase.services;

import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.Comment;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Task;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 16.11.2015.
 */
public interface TaskService {

    LinkedList<Integer> writeTask(int pID, Task task) throws Exception;

    void deleteTaskByID(int tID) throws Exception;

    Task getByID(int tID) throws Exception;
    LinkedList<Task> getAllTasks() throws Exception;
    LinkedList<Task> getAllTasksFromUser(String uID) throws Exception;

    LinkedList<Task> getAllTasksFromProject(int pID) throws Exception;

    LinkedList<Task> getAllTasksFromProjectAndUser(int pID, String uID) throws Exception;

    void assignUserToTask(int tID, String uID) throws Exception;
    void removeUserFromTask(int tID, String uID) throws Exception;

    JsonStringWrapper addCommentToTask(int tID, Comment comment) throws ValidationException;

    void deleteCommentFromTask(int tID, int cID) throws Exception;


}




