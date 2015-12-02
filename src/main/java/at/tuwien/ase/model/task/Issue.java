package at.tuwien.ase.model.task;

import at.tuwien.ase.model.user.User;

import java.util.Date;

/**
 * Created by DanielHofer on 14.11.2015.
 */
public class Issue {

    private Integer id;
    private String title;
    private String description;
    private String taskType;
    private Date creationDate;
    private Date updateDate;
    private Integer projectId;
    private String userId;
    private User user; //creator of issue

    // Must have no-argument constructor
    public Issue() {

    }

    public Issue(String title, String description, String taskType, Date creationDate, Date updateDate, Integer projectId, String userId) {
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.projectId = projectId;
        this.userId = userId;
    }

    public Issue(String title, String description) {
        this(title, description, null, null, null, null, null);
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskType='" + taskType + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", projectId=" + projectId +
                ", userId='" + userId + '\'' +
                ", user=" + user +
                '}';
    }
}