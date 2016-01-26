package at.tuwien.ase.dao;

import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Reward;

import java.util.LinkedList;

/**
 * Interface for DAO access to Project. With Spring an Autowire is possible
 *
 * @author Tomislav Nikic
 * @version 1.0, 13.12.2015
 */
public interface ProjectDAO {

    int insertProject(Project project);

    void insertRewardsToProjectBatch(int pID, LinkedList<Reward> rewardList);

    void removeProject(int pID);

    void updateProject(int pID, Project project);

    void addUserToProject(String uID, int pID, String role);

    void removeUserFromProject(String uID, int pID);

    Project findByID(int pID);

    LinkedList<Project> loadAll();

    LinkedList<Project> loadAllByUser(String uID);

}
