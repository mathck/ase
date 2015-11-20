package at.tuwien.ase.services;

import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.model.project.Role;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.model.user.User;

import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 17/11/2015.
 */
public interface ProjectService {

    Project writeProject(Project project);

    boolean deleteProject(String pID);

    Project getByID(String pID);
    LinkedList<Project> getAllProjects();
    LinkedList<Project> getAllProjectsFromUser(String uID);

    User addUser(String pID, User user, Role role);

}
