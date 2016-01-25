package at.tuwien.ase.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;

/**
 * A model class describing the project object. It combines task, issue, user
 * as well as the minor classes like sub task and reward.
 *
 * @author Tomislav Nikic
 * @verison 1.0, 13.12.2015
 */
public class Project {

    @NotNull
    @Min(1)
    private int projectID;
    @NotNull
    @Size(min = 1, max = 50)
    private String title;
    @NotNull
    @Size(min = 0, max = 255)
    private String description;
    @NotNull
    private Level level;

    /*
     * Ignoring this attribute due to not being able to use it on the frontend.
     * Used for storing the time in the database with the right format.
     */
    @JsonIgnore private Timestamp creationTimeDB;
    /*
     * Ignoring this attribute due to not being able to use it on the frontend.
     * Used for storing the time in the database with the right format.
     */
    @JsonIgnore private Timestamp updateTimeDB;

    private String creationTime;
    private String updateTime;

    private LinkedList<UserRole> userList;
    private LinkedList<Task> taskList;
    private LinkedList<Issue> issueList;

    public Project() {}

    /**
     * Constructor for creating the project object. After initializing the given parameters
     * it stores the current time and and stores it in the update time since this is the
     * first occurrence of the object.
     *
     * @param projectID The project ID that was assigned during the first write to the database.
     * @param title The main title for the project.
     * @param description A short description of the project.
     */
    public Project(int projectID, String title, String description) {
        this.projectID = projectID;
        this.title = title;
        this.description = description;

        Date time = new Date();
        creationTimeDB = new Timestamp(time.getTime());
        updateTimeDB = creationTimeDB;

        userList = new LinkedList<UserRole>();
        taskList = new LinkedList<Task>();
        issueList = new LinkedList<Issue>();
    }

    /**
     * Adds a user to the list of users (using the class at.tuwien.ase.mode.UserRole).
     *
     * @param userID The user email that is stored in the database.
     * @param role The role of the user, specified in userID, in this project.
     */
    public void addUser(String userID, String role) {
        UserRole newUser = new UserRole(userID, this.projectID, role);
        userList.add(newUser);
    }

    /**
     * Deletes a user from the list of users.
     *
     * @param userID The user email that is stored in the database.
     */
    public void deleteUser(String userID) {
        for (UserRole iteration : userList) {
            if (iteration.getUser() == userID)
                userList.remove(iteration);
        }
    }

    /**
     * Retrieves a specific role of the user specified in the parameter.
     *
     * @param userID The user email that is stored in the database.
     * @return An object of the type UserRole containing this one user and his role in the project.
     */
    public UserRole getUserRole(String userID) {
        for (UserRole iterator : userList)
            if (iterator.getUser() == userID)
                return iterator;
        return null;
    }

    /**
     * Adds a task object to the list of tasks.
     *
     * @param task The task object, that should be added.
     */
    public void addTask(Task task) {
        taskList.add(task);
    }

    /**
     * Deletes a task from the task list.
     *
     * @param taskID The task ID that is given when writing to the database.
     */
    public void deleteTask(int taskID) {
        for (Task iterator : taskList)
            if (iterator.getId() == taskID)
                taskList.remove(iterator);
    }

    /**
     * Adds an issue object to the issue list.
     *
     * @param issue The issue object, that should be added.
     */
    public void addIssue(Issue issue) {
        issueList.add(issue);
    }

    /**
     * Deletes an issue object from the issue list.
     *
     * @param issueID The issue ID that is given when writing to the database.
     */
    public void deleteIssue(int issueID) {
        for (Issue iterator : issueList)
            if (iterator.getId() == issueID)
                issueList.remove(iterator);
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int pID) {
        this.projectID = pID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkedList<UserRole> getAllUser() {
        return userList;
    }

    public void setAllUser(LinkedList<UserRole> userList) {
        this.userList = userList;
    }

    public LinkedList<Task> getAllTasks() {
        return taskList;
    }

    public void setAllTasks(LinkedList<Task> taskList) {
        this.taskList = taskList;
    }

    public LinkedList<Issue> getAllIssues() {
        return issueList;
    }

    public void setAllIssues(LinkedList<Issue> issueList) {
        this.issueList = issueList;
    }

    public Timestamp getCreationTimeDB() {
        return creationTimeDB;
    }

    /**
     * Sets up the creation time used for the database. Doing this, it also saves a readable
     * representation of the date and time as a String in creationTime.
     *
     * @param creationTimeDB The time of creation, produced during the first instantiation.
     */
    public void setCreationTimeDB(Timestamp creationTimeDB) {
        this.creationTimeDB = creationTimeDB;
        creationTime = creationTimeDB.toString();
    }

    public Timestamp getUpdateTimeDB() {
        return updateTimeDB;
    }

    /**
     * Sets up the update time used for the database. Doing this, it also saves a readable
     * representation of the date and time as a String in updateTime.
     *
     * @param updateTimeDB The time when the last changes where made to this object.
     */
    public void setUpdateTimeDB(Timestamp updateTimeDB) {
        this.updateTimeDB = updateTimeDB;
        updateTime = updateTimeDB.toString();
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
