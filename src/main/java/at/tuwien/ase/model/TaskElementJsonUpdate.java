package at.tuwien.ase.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.LinkedList;

/**
 * Created by DanielHofer on 22.12.2015.
 */
public class TaskElementJsonUpdate implements Cloneable{

    @NotNull
    private Integer id;

    @Null
    private Integer itemId;

    @Size(min = 1)
    private String status;

    @Size(min = 1)
    private String value;

    @Size(min = 1)
    private String link;

    @Null
    private String itemType;

    @Null
    private Integer subtaskId;

    @Null
    private Integer dslTemplateId;

    @Null
    private String solution;

    // Must have no-argument constructor
    public TaskElementJsonUpdate() {

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
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Integer getDslTemplateId() {
        return dslTemplateId;
    }

    public void setDslTemplateId(Integer dslTemplateId) {
        this.dslTemplateId = dslTemplateId;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
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
                ", dslTemplateId=" + dslTemplateId +
                ", solution='" + solution + '\'' +
                '}';
    }
}
