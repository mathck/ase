package at.tuwien.ase.services;

import at.tuwien.ase.model.project.Project;

import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 17/11/2015.
 */
public interface ProjectService {

    Project createProject(String id, String description);
    boolean saveProject(Project project);
    boolean deleteProject(String id);

    Project getByID(String id);
    LinkedList<Project> getAllProjects();
    LinkedList<Project> getAllProjectsFromUser(String email);

}
