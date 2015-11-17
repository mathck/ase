package at.tuwien.ase.services;

import at.tuwien.ase.model.user.RegistrationUnit;
import at.tuwien.ase.model.user.User;

/**
 * Created by Daniel Hofer on 16.11.2015.
 */
public interface UserService {

    /**
     * used to user
     *
     * @param newUser
     * @return created user
     */
    public User addUser(RegistrationUnit newUser);

    public User addUser(User newUser);
    public User addUser(String email, String password);

    User updateUser(String email, String password, String firstName, String lastName, String avatar);
    User getUserByMail(String email);

}
