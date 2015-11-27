package at.tuwien.ase.dao;

import at.tuwien.ase.model.project.UserRole;
import at.tuwien.ase.model.user.User;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 16.16.11.2015.
 */
public interface UserDAO
{

	void insertUser(User user);

	void removeUser(String uID);

	void updateUser(String uID, User user);

	User findByID(String uID);

	User authUser(String uID);

	LinkedList<User> loadAll();

	LinkedList<UserRole> loadAllByProject(int pID);

}
