package at.tuwien.ase.model;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 04.11.2015.
 */
public class Task {

    private Integer id;
    private String title;
    private String description;
    private String taskType;
    private Date creationDate;
    private Date updateDate;
    private Integer projectId;
    private String userMail;
    private String status;
    private User user;

    private LinkedList<Subtask> subtaskList;
    private LinkedList<User> userList;

    // Must have no-argument constructor
    public Task() {
        subtaskList = new LinkedList<Subtask>();
        userList = new LinkedList<User>();
    }

    public Task(String status, String title, String description, String taskType, Date creationDate, Date updateDate, Integer projectId, String userMail) {
        this.status = status;
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.projectId = projectId;
        this.userMail = userMail;
        subtaskList = new LinkedList<Subtask>();
        userList = new LinkedList<User>();
    }

    public Task(String title, String description) {
        this(null, title, description, null, null, null, null, null);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LinkedList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(LinkedList<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public void addSubtask(Subtask subtask){
        this.subtaskList.add(subtask);
    }

    public void addUser(User user){
        this.userList.add(user);
    }

    public LinkedList<User> getUserList() {
        return userList;
    }

    public void setUserList(LinkedList<User> userList) {
        this.userList = userList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskType='" + taskType + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", projectId=" + projectId +
                ", userMail='" + userMail + '\'' +
                ", status='" + status + '\'' +
                ", user=" + user +
                ", subtaskList=" + subtaskList +
                ", userList=" + userList +
                '}';
    }
}