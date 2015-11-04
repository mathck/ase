package at.tuwien.ase.domain;

/**
 * Task POJO
 *
 * @author Daniel Hofer
 */

public class Task {

    private int id;
    private String title;
    private String description;

    // Must have no-argument constructor
    public Task() {

    }

    public Task(int id, String title, String description) {
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
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}