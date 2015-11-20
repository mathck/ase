package at.tuwien.ase.model.task;

import java.util.Date;

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
    private Integer dslTemplateId;
    private Integer projectId;
    private String userMail;
    private String status;

    // Must have no-argument constructor
    public Task() {

    }

    public Task(String status, String title, String description, String taskType, Date creationDate, Date updateDate, Integer dslTemplateId, Integer projectId, String userMail) {
        this.status = status;
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.dslTemplateId = dslTemplateId;
        this.projectId = projectId;
        this.userMail = userMail;
    }

    public Task(String title, String description) {
        this(null, title, description, null, null, null, null, null, null);
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

    public Integer getDslTemplateId() {
        return dslTemplateId;
    }

    public void setDslTemplateId(Integer dslTemplateId) {
        this.dslTemplateId = dslTemplateId;
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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskType='" + taskType + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", dslTemplateId=" + dslTemplateId +
                ", projectId=" + projectId +
                ", userMail='" + userMail + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}