package at.tuwien.ase.services;

import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Project;

import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 17/11/2015.
 */
public interface ProjectService {

    JsonStringWrapper writeProject(Project project);

    void deleteProject(int pID);

    void updateProject(int pID, Project project);

    Project getByID(int pID, String uID);
    LinkedList<Project> getAllProjects();
    LinkedList<Project> getAllProjectsFromUser(String uID);

    void addUser(int pID, String uID, String role);
    void removeUser(int pID, String uID);

}
