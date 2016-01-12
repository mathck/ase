package at.tuwien.ase.model;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 04.11.2015.
 */
public class Task implements Cloneable{

    private Integer id;
    private String title;
    private String description;
    private String taskType;
    private String executionType; //single_task, collaborative_task
    private Date creationDate;
    private Date updateDate;
    private Integer projectId;
    private String userMail;
    private String status; //current status
    private User user;
    private boolean commentsAllowed;

    private LinkedList<Subtask> subtaskList;
    private LinkedList<User> userList;
    private LinkedList<TaskState> taskStates;
    private LinkedList<Comment> commentList;

    // Must have no-argument constructor
    public Task() {
        subtaskList = new LinkedList<Subtask>();
        userList = new LinkedList<User>();
        taskStates = new LinkedList<TaskState>();
        commentList = new LinkedList<Comment>();
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
        taskStates = new LinkedList<TaskState>();
        commentList = new LinkedList<Comment>();
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

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public LinkedList<TaskState> getTaskStates() {
        return taskStates;
    }

    public void setTaskStates(LinkedList<TaskState> taskStates) {
        this.taskStates = taskStates;
    }

    public void addTaskState(TaskState taskState){
        this.taskStates.add(taskState);
    }

    public void addComment(Comment comment){
        this.commentList.add(comment);
    }

    public LinkedList<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(LinkedList<Comment> commentList) {
        this.commentList = commentList;
    }

    public boolean isCommentsAllowed() {
        return commentsAllowed;
    }

    public void setCommentsAllowed(boolean commentsAllowed) {
        this.commentsAllowed = commentsAllowed;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskType='" + taskType + '\'' +
                ", executionType='" + executionType + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", projectId=" + projectId +
                ", userMail='" + userMail + '\'' +
                ", status='" + status + '\'' +
                ", user=" + user +
                ", commentsAllowed=" + commentsAllowed +
                ", subtaskList=" + subtaskList +
                ", userList=" + userList +
                ", taskStates=" + taskStates +
                ", commentList=" + commentList +
                '}';
    }
}