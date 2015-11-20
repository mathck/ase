package at.tuwien.ase.dao.task;

import at.tuwien.ase.model.project.Project;

import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 19/11/2015.
 */
public interface ProjectDAO {

    Project insertProject(Project project);
    boolean removeProject(String pID);

    Project findByID(String pID);
    LinkedList<Project> loadAll();
    LinkedList<Project> loadAllByUser(String uID);

}
