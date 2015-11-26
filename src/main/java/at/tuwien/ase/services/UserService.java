package at.tuwien.ase.services;

import at.tuwien.ase.model.user.User;

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

	LinkedList<User> getAllUsersFromProject(String pID);

	void updateUser(String uID, User user);

}
