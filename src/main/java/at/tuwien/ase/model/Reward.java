package at.tuwien.ase.model;

import java.util.Date;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public class Reward {
    private int id;
    private String userMail;
    private String name;
    private String description;
    private int xpbase;
    private String imageLink;
    private Date creationDate;

    // Must have no-argument constructor
    public Reward() {

    }

    public Reward(String userMail, String name, String description, int xpbase, String imageLink, Date creationDate) {
        this.userMail = userMail;
        this.name = name;
        this.description = description;
        this.xpbase = xpbase;
        this.imageLink = imageLink;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getXpbase() {
        return xpbase;
    }

    public void setXpbase(int xpbase) {
        this.xpbase = xpbase;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id=" + id +
                ", userMail='" + userMail + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", xpbase=" + xpbase +
                ", imageLink='" + imageLink + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
