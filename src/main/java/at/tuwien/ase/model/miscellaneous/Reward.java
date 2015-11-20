package at.tuwien.ase.model.miscellaneous;

import java.util.Date;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public class Reward {
    private int id;
    private String user_mail;
    private String name;
    private String description;
    private int xpbase;
    private String image_link;
    private Date creation_date;

    // Must have no-argument constructor
    public Reward() {

    }

    public Reward(String user_mail, String name, String description, int xpbase, String image_link, Date creation_date) {
        this.user_mail = user_mail;
        this.name = name;
        this.description = description;
        this.xpbase = xpbase;
        this.image_link = image_link;
        this.creation_date = creation_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
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

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id=" + id +
                ", user_mail='" + user_mail + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", xpbase=" + xpbase +
                ", image_link='" + image_link + '\'' +
                ", creation_date=" + creation_date +
                '}';
    }
}
