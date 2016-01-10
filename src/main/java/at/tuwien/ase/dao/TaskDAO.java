package at.tuwien.ase.dao;

import at.tuwien.ase.model.Comment;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskState;
import at.tuwien.ase.model.User;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 09.11.2015.
 */
public interface TaskDAO {

    void insertTask(int pID, Task task);
    void removeTaskByID(int tID);

    Task findByID(int tID);
    LinkedList<Task> loadAll();

    LinkedList<Task> loadAllByProject(int pID);
    LinkedList<Task> loadAllByUser(String uID);


    //assign user to task
    void addUserToTask(String uID, int tID);
    //add state to state list
    void addStateToTaskStates(TaskState state, int tID);

    LinkedList<Task> loadAllByProjectAndUser(int pID, String uID);

    int getNewID();
    int getNewIDForRelTaskUser();
    int getNewIDForTaskStates();
    int getNewIDForComments();

    void assignUserToTask(int tID, String uID);
    void removeUserFromTask(int tID, String uID);

    void addCommentToTask(int tID, Comment comment);

    void removeCommentFromTask(int tID, int cID);

}
