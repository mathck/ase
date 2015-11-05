package at.tuwien.ase.domain.task;

/**
 * Created by mathc_000 on 05-Nov-15.
 */
public class Subtask extends Task {

    // todo add task parameters

    // Must have no-argument constructor
    public Subtask() {

    }

    public Subtask(int id, String title, String description) {
        super(id, title, description);
    }

    @Override
    public String toString() {
        return Subtask.class.getName() + "{" +
                "id=" + this.getId() +
                ", description='" + this.getDescription() + '\'' +
                ", title='" + this.getTitle() + '\'' +
                '}';
    }
}
