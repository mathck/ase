package at.tuwien.ase.services.impl;

import at.tuwien.ase.model.user.User;
import at.tuwien.ase.model.user.RegistrationUnit;
import at.tuwien.ase.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


/**
 * Created by DanielHofer on 16.16.11.2015.
 */

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public User addUser(RegistrationUnit newUser) {
        User user = newUser.createUser();
        user.toFile();
        System.out.println("Created new user " + user.getEmail());
        //TODO Save user to db

        return user;
    }


}
