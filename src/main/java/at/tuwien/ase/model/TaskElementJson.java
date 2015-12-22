package at.tuwien.ase.model;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 22.12.2015.
 */
public class TaskElementJson {

    private int id;
    private int itemId;
    private String status;
    private String value;
    private String link;
    private String itemType;
    private int subtaskId;

    // Must have no-argument constructor
    public TaskElementJson() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public String toString() {
        return "TaskElementJson{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", status='" + status + '\'' +
                ", value='" + value + '\'' +
                ", link='" + link + '\'' +
                ", itemType='" + itemType + '\'' +
                ", subtaskId=" + subtaskId +
                '}';
    }
}
