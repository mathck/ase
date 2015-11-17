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

    @Override
    public User addUser(User newUser) {
        System.out.println("Created new user " + newUser.getEmail());
        //TODO Save user to db
        return newUser;
    }

    @Override
    public User addUser(String email, String password) {
        User newUser = new User(email, password);
        //TODO Save user to db
        return newUser;
    }

    @Override
    public User updateUser(String email, String password, String firstName, String lastName, String avatar) {
        User user = this.getUserByMail(email);
        if(email != null)
            user.setEmail(email);
        if(password != null)
            user.setPassword(password);
        if(firstName != null)
            user.setName(firstName);
        if(lastName != null)
            user.setLastName(lastName);
        if(avatar != null)
            user.setAvatar(avatar);
        return user;
    }

    public User getUserByMail(String email) {
        //TODO Get user by email
        return null;
    }


}
