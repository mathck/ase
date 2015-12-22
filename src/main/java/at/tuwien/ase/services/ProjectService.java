package at.tuwien.ase.services;

import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Project;

import java.util.LinkedList;

/**
 * Interface for project service. With Spring an Autowire is possible.
 *
 * @author Tomislav Nikic
 * @version 1.0, 14.12.2015
 */
public interface ProjectService {

    JsonStringWrapper writeProject(Project project) throws Exception;

    void deleteProject(int pID);

    void updateProject(int pID, Project project);

    Project getByID(int pID, String uID);

    LinkedList<Project> getAllProjects();

    LinkedList<Project> getAllProjectsFromUser(String uID);

    void addUser(int pID, String uID, String role);

    void removeUser(int pID, String uID);

    void updateTime(int projectID);

}
