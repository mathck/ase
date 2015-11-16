package at.tuwien.ase.services;

import at.tuwien.ase.model.user.RegistrationUnit;
import at.tuwien.ase.model.user.User;

/**
 * Created by DanielHofer on 16.16.11.2015.
 */
public interface UserService {

    /**
     * used to user
     *
     * @param newUser
     * @return created user
     */
    public User addUser(RegistrationUnit newUser);

}
