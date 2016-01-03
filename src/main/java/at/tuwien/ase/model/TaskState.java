package at.tuwien.ase.model;

import java.util.LinkedList;
import java.util.zip.Inflater;

/**
 * Created by DanielHofer on 22.12.2015.
 */
public class TaskState {

    private Integer id;
    private String stateName;
    private Integer taskId;

    // Must have no-argument constructor
    public TaskState() {

    }

    public TaskState(Integer id, String stateName, Integer taskId) {
        this.id = id;
        this.stateName = stateName;
        this.taskId = taskId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "TaskState{" +
                "id=" + id +
                ", stateName='" + stateName + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
