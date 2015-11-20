package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.task.UserDAO;
import at.tuwien.ase.model.user.User;
import at.tuwien.ase.model.user.RegistrationUnit;
import at.tuwien.ase.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


/**
 * Created by DanielHofer on 16.16.11.2015.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public User writeUser(RegistrationUnit ru) {
        User user = ru.createUser();
        user.toFile();
        userDAO.insertUser(user);
        return user;
    }

    public User getByID(String uID) {
        return userDAO.findByID(uID);
    }

    public LinkedList<User> getAllUsers() {
        return userDAO.loadAll();
    }

    public LinkedList<User> getAllUsersFromProject(String pID) {
        return userDAO.loadAllByProject(pID);
    }

    public User updateUser(String uID, String email, String password, String firstName, String lastName, String avatar) {
        User user = userDAO.findByID(uID);
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
        userDAO.removeUser(uID);
        return userDAO.insertUser(user);
    }

}
