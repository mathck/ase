package at.tuwien.ase.services;

import at.tuwien.ase.model.UserRole;
import at.tuwien.ase.model.User;

import java.util.LinkedList;

/**
 * Interface for user service. With Spring an Autowire is possible.
 *
 * @author Tomislav Nikic
 * @version 1.0, 14.12.2015
 */
public interface UserService {

    void writeUser(User user);

    void deleteUser(String uID);

    User getByID(String uID);

    User authUser(String uID);

    LinkedList<User> getAllUsers();

    LinkedList<UserRole> getAllUsersFromProject(int pID);

    void updateUser(String uID, User user);
}