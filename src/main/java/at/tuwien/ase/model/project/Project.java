package at.tuwien.ase.model.project;

import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.model.user.User;

import javax.sound.sampled.Line;
import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
public class Project {

    // Essential info
    private String id;
    private String title;
    private String description;

    // Lists
    private LinkedList<UserRole> userList;
    private LinkedList<Task> taskList;
    private LinkedList<Issue> issueList;

    //Default Constructor - necessary for REST POST Functionality!
    public Project(){}

    // Creating project and setting up lists
    // @author Tomislav Nikic
    public Project(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;

        userList = new LinkedList<UserRole>();
        taskList = new LinkedList<Task>();
        issueList = new LinkedList<Issue>();


    }

    // Getter and setter for ID
    // @author Tomislav Nikic
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // Getter and setter for Title
    // @author Tomislav Nikic
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and setter for Description
    // @author Tomislav Nikic
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // Add/delete a user to/from the list
    // @author Tomislav Nikic
    public void addUser(String uID, Role role) {
        UserRole newUser = new UserRole(uID, this.id, role);
        userList.add(newUser);
    }
    public void deleteUser(String email) {
        for(UserRole iteration : userList) {
            if(iteration.getUser() == email)
                userList.remove(iteration);
        }
    }

    public void setAllUser(LinkedList<UserRole> userList) {
        this.userList = userList;
    }
    // Reading lists
    // @author Tomislav Nikic
    public LinkedList<UserRole> getAllUser() {
        return userList;
    }
    public UserRole getUserRole(String uID) {
        for(UserRole iterator : userList)
            if(iterator.getUser() == uID)
                return iterator;
        return null;
    }

    // Add/delete a task to/from the list
    // @author Tomislav Nikic
    public void addTask(Task task) {
        taskList.add(task);
    }
    public void deleteTask(int id) {
        for(Task iterator : taskList)
            if(iterator.getId() == id)
                taskList.remove(iterator);
    }

    public void setAllTasks(LinkedList<Task> taskList) {
        this.taskList = taskList;
    }
    // Get list of all tasks
    // @author Tomislav Nikic
    public LinkedList<Task> getAllTasks() {
        return taskList;
    }

    // Add/delete a issue to/from the list
    // @author Tomislav Nikic
    public void addIssue(Issue issue) {
        issueList.add(issue);
    }
    public void deleteIssue(int id) {
        for(Issue iterator : issueList)
            if(iterator.getId() == id)
                issueList.remove(iterator);
    }

    public  void setAllIssues(LinkedList<Issue> issueList) {
        this.issueList = issueList;
    }
    // Get list of all issues
    // @author Tomislav Nikic
    public LinkedList<Issue> getAllIssues() {
        return issueList;
    }

}
