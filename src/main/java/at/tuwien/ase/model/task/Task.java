package at.tuwien.ase.model.task;

/**
 * Created by Daniel Hofer on 04.11.2015.
 */
public class Task {

    private int id;
    private String title;
    private String description;

    // Must have no-argument constructor
    public Task() {

    }

    public Task(String title, String description) {
        this.description = description;
        this.title = title;
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
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}