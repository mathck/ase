package at.tuwien.ase.services;

import at.tuwien.ase.model.UserRole;
import at.tuwien.ase.model.User;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 16.11.2015.
 */
public interface UserService
{

	void writeUser(User user);

	void deleteUser(String uID);

	User getByID(String uID);

	LinkedList<User> getAllUsers();

	LinkedList<UserRole> getAllUsersFromProject(int pID);

	void updateUser(String uID, User user);

}
