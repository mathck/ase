package at.tuwien.ase.model;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by mathc_000 on 05-Nov-15.
 */
public class SubtaskUpdate implements Cloneable{

    @Null
    private Integer id;

    @Size(min = 2)
    private String title;

    @Size(min = 2)
    private String description;

    @Null
    private Integer taskId; //reference to task ('consists of')

    @Pattern(regexp = "closed")
    private String status;

    @Null
    private String taskBody;

    @Null
    private Integer dslTemplateId;

    @Min(0)
    private int xp;

    @Null
    private Date creationDate;

    @Null
    private Date updateDate;

    @Null
    private Integer percentageReached;

    @Size(max = 30)
    private LinkedList<TaskElementJsonUpdate> taskElements;

    // Must have no-argument constructor
    public SubtaskUpdate() {
        taskElements = new LinkedList<TaskElementJsonUpdate>();
    }

    public SubtaskUpdate(String title, String description, int taskId, String status, int xp, Date creationDate, Date updateDate) {
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

    public LinkedList<TaskElementJsonUpdate> getTaskElements() {
        return taskElements;
    }

    public void setTaskElements(LinkedList<TaskElementJsonUpdate> taskElements) {
        this.taskElements = taskElements;
    }

    public void addTaskElement(TaskElementJsonUpdate taskElement) {
        this.taskElements.add(taskElement);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Integer getPercentageReached() {
        return percentageReached;
    }

    public void setPercentageReached(Integer percentageReached) {
        this.percentageReached = percentageReached;
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
                ", percentageReached=" + percentageReached +
                ", taskElements=" + taskElements +
                '}';
    }
}
