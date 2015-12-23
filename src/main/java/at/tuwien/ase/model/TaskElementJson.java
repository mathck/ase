package at.tuwien.ase.model;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 22.12.2015.
 */
public class TaskElementJson {

    private Integer id;
    private Integer itemId;
    private String status;
    private String value;
    private String link;
    private String itemType;
    private Integer subtaskId;

    // Must have no-argument constructor
    public TaskElementJson() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
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

    public Integer getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(Integer subtaskId) {
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
