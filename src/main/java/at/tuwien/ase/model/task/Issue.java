package at.tuwien.ase.model.task;

/**
 * Created by DanielHofer on 14.11.2015.
 */
public class Issue {

    private int id;
    private String title;
    private String description;

    // Must have no-argument constructor
    public Issue() {

    }

    public Issue(int id, String title, String description) {
        this.description = description;
        this.title = title;
        this.id = id;
    }

    public void setDescription(String fname) {
        this.description = fname;
    }

    public String getDescription() {
        return this.description;
    }

    public void setTitle(String lname) {
        this.title = lname;
    }

    public String getTitle() {
        return this.title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}