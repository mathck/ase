package at.tuwien.ase.services;

import at.tuwien.ase.model.user.RegistrationUnit;
import at.tuwien.ase.model.user.User;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 16.11.2015.
 */
public interface UserService {

    void writeUser(RegistrationUnit newUser);

    void deleteUser(String uID);

    User getByID(String uID);
    LinkedList<User> getAllUsers();
    LinkedList<User> getAllUsersFromProject(String pID);

    void updateUser(String uID, String email, String password, String firstName, String lastName, String avatar);

}
