package at.tuwien.ase.model.project;

import at.tuwien.ase.exceptions.UserRoleNotFoundException;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.model.user.User;

import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
public class Project {

    // Essential info
    private String id;
    private String description;

    // Lists
    private LinkedList<UserRole> userList;
    private LinkedList<Task> taskList;
    private LinkedList<Issue> issueList;

    // Creating project and setting up lists
    // @author Tomislav Nikic
    public Project(String id, String description) {
        this.description = description;
        this.id = id;

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
    public void addUser(User user, Role role) {
        UserRole newUser = new UserRole(user, this.id, role);
        userList.add(newUser);
    }
    public void deleteUser(String email) {
        for(UserRole iteration : userList) {
            if(iteration.getUser().getEmail() == email)
                userList.remove(iteration);
        }
    }

    // Reading lists
    // @author Tomislav Nikic
    public LinkedList<UserRole> getAllUser() {
        return userList;
    }
    public UserRole getUserRole(User user) throws UserRoleNotFoundException {
        for(UserRole iterator : userList)
            if(iterator.getUser() == user)
                return iterator;
        throw new UserRoleNotFoundException();
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

    // Get list of all issues
    // @author Tomislav Nikic
    public LinkedList<Issue> getAllIssues() {
        return issueList;
    }

}
