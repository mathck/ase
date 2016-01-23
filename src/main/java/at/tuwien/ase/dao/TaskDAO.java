package at.tuwien.ase.dao;

import at.tuwien.ase.model.Comment;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskState;
import at.tuwien.ase.model.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Daniel Hofer on 09.11.2015.
 */
public interface TaskDAO {

    void insertTask(int pID, Task task);
    void insertTaskBatch(final int pID, final List<Task> taskList, final String uuID);
    void removeTaskByID(int tID);

    Task findByID(int tID);
    LinkedList<Task> loadAll();
    LinkedList<Integer> loadTaskIdsByUuID(String uuID);

    LinkedList<Task> loadAllByProject(int pID);
    LinkedList<Task> loadAllByUser(String uID);
    LinkedList<Comment> loadAllCommentsByTask(int tID);
    LinkedList<User> loadAllUsersByTask(int tID);

    //assign user to task
    void addUserToTask(String uID, int tID);
    void addStateToTaskStatesBatch(final List<TaskState> taskStateList, final LinkedList<Integer> taskIds);

    //add state to state list
    void addStateToTaskStates(TaskState state, int tID);

    LinkedList<Task> loadAllByProjectAndUser(int pID, String uID);

    int getNewIDForComments();

    void assignUserToTask(int tID, String uID);
    void assignUserToTaskBatch(final List<User> userList, final Integer taskId);

    void removeUserFromTask(int tID, String uID);

    void addCommentToTask(int tID, Comment comment);

    void removeCommentFromTask(int tID, int cID);

}
