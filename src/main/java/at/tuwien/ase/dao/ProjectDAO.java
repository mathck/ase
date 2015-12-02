package at.tuwien.ase.dao;

import at.tuwien.ase.model.Project;

import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 19/11/2015.
 */
public interface ProjectDAO {

	int insertProject(Project project);

	void removeProject(int pID);

	void updateProject(int pID, Project project);

	void addUserToProject(String uID, int pID, String role);

	void removeUserFromProject(String uID, int pID);

	Project findByID(int pID);

	LinkedList<Project> loadAll();
    LinkedList<Project> loadAllByUser(String uID);

}
