package at.tuwien.ase.domain.task;

/**
 * Created by mathc_000 on 05-Nov-15.
 */
public abstract class Task {

    private int id;
    private String title;
    private String description;

    public Task() {

    }

    public Task(int id, String title, String description) {
        this.description = description;
        this.title = title;
        this.id = id;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
