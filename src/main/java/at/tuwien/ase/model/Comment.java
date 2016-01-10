package at.tuwien.ase.model;

import java.util.Date;

/**
 * Created by DanielHofer on 10.01.2016.
 */
public class Comment {

    private Integer id;
    private Integer task_id;
    private String text;
    private String user_mail;
    private Date creationDate;

    // Must have no-argument constructor
    public Comment() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", task_id=" + task_id +
                ", text='" + text + '\'' +
                ", user_mail='" + user_mail + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
