package at.tuwien.ase.model;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by mathc_000 on 05-Nov-15.
 */
public class Subtask {

    private Integer id;
    private String title;
    private String description;
    private int taskId; //reference to task ('consists of')
    private String status;
    private String taskBody;
    private Integer dslTemplateId;
    private int xp;
    private Date creationDate;
    private Date updateDate;

    private LinkedList<TaskElementJson> taskElements;

    // Must have no-argument constructor
    public Subtask() {
        taskElements = new LinkedList<TaskElementJson>();
    }

    public Subtask(String title, String description, int taskId, String status, int xp, Date creationDate, Date updateDate) {
        this.title = title;
        this.description = description;
        this.taskId = taskId;
        this.status = status;
        this.xp = xp;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
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

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
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

    public String getTaskBody() {
        return taskBody;
    }

    public void setTaskBody(String taskBody) {
        this.taskBody = taskBody;
    }

    public LinkedList<TaskElementJson> getTaskElements() {
        return taskElements;
    }

    public void setTaskElements(LinkedList<TaskElementJson> taskElements) {
        this.taskElements = taskElements;
    }

    public void addTaskElement(TaskElementJson taskElement) {
        this.taskElements.add(taskElement);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status='" + status + '\'' +
                ", taskBody='" + taskBody + '\'' +
                ", dslTemplateId=" + dslTemplateId +
                ", xp=" + xp +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", taskElements=" + taskElements +
                '}';
    }
}
