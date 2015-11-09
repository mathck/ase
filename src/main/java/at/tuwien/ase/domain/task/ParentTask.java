package at.tuwien.ase.domain.task;

/**
 * Created by DanielHofer on 04.11.2015.
 */

public class ParentTask extends Task {

    // Must have no-argument constructor
    public ParentTask() {

    }

    public ParentTask(int id, String title, String description) {
        super(id, title, description);
    }

    @Override
    public String toString() {
        return ParentTask.class.getName() + "{" +
                "id=" + this.getId() +
                ", description='" + this.getDescription() + '\'' +
                ", title='" + this.getTitle() + '\'' +
                '}';
    }
}