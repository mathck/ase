package at.tuwien.ase.services;

import at.tuwien.ase.model.project.Project;

import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 17/11/2015.
 */
public interface ProjectService {

    int writeProject(Project project);

    void deleteProject(int pID);

    void updateProject(int pID, Project project);

    Project getByID(int pID);
    LinkedList<Project> getAllProjects();
    LinkedList<Project> getAllProjectsFromUser(String uID);

    void addUser(int pID, String uID, String role);

}
