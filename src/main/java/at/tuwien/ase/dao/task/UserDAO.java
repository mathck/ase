package at.tuwien.ase.dao.task;

import at.tuwien.ase.model.user.User;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 16.16.11.2015.
 */
public interface UserDAO {

    User insertUser(User user);
    boolean removeUser(String uID);

    User findByID(String uID);
    LinkedList<User> loadAll();
    LinkedList<User> loadAllByProject(String pID);

}
