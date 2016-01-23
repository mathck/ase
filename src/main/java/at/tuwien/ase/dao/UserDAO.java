package at.tuwien.ase.dao;

import at.tuwien.ase.model.UserRole;
import at.tuwien.ase.model.User;

import java.util.LinkedList;

/**
 * Interface for DAO access to User. With Spring an Autowire is possible
 *
 * @author Tomislav Nikic
 * @version 1.0, 13.12.2015
 */
public interface UserDAO {

    void insertUser(User user);

    void removeUser(String uID);

    void updateUser(String uID, User user);

    User findByID(String uID);

    User authUser(String uID);

    LinkedList<User> loadAll();

    LinkedList<UserRole> loadAllByProject(int pID);

    LinkedList<User> getRelatedUser(String userId);

    LinkedList<User> getUserList(String search);

}
