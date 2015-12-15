package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.model.*;
import at.tuwien.ase.services.LevelService;
import at.tuwien.ase.services.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.LinkedList;


/**
 * The implemented user service.
 *
 * @author Tomislav Nikic
 * @version 1.0, 14.12.2015
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private SubtaskDAO subtaskDAO;

    @Autowired
    private LevelService levelService;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl() {
    }

    /**
     * Constructor initializing all Autowire annotated attributes.
     *
     * @param userDAO The user dao link.
     * @param projectDAO The project dao link.
     * @param subtaskDAO The sub-task dao link.
     */
    public UserServiceImpl(UserDAO userDAO, ProjectDAO projectDAO, SubtaskDAO subtaskDAO, LevelService levelService) {
        this.userDAO = userDAO;
        this.projectDAO = projectDAO;
        this.subtaskDAO = subtaskDAO;
        this.levelService = levelService;
    }

    /**
     * Insert a user into the database.
     *
     * @param user The user object that is inserted.
     */
    public void writeUser(User user) {
        if(user.getFirstName() != null && user.getFirstName().length() > 2) {
            if(user.getLastName() != null && user.getLastName().length() > 2) {
                if(user.getAvatar() != null && user.getAvatar().length() > 0) {
                    if(user.getPassword() != null) {
                        if(user.getUserID() != null && user.getUserID().length() > 5 && EmailValidator.getInstance().isValid(user.getUserID())) {
                            userDAO.insertUser(user);
                        } else {
                            throw new IllegalArgumentException("User ID is not valid");
                        }
                    } else {
                        throw new IllegalArgumentException("Password is not valid");
                    }
                } else {
                    throw new IllegalArgumentException("Avatar is not valid");
                }
            } else {
                throw new IllegalArgumentException("Last name is not valid");
            }
        } else {
            throw new IllegalArgumentException("First name is not valid");
        }
    }

    /**
     * Deleting the user from the database.
     *
     * @param userID The user email that is stored in the database.
     */
    public void deleteUser(String userID) {
        logger.info("Starting process for deleting user <" + userID + ">");
        logger.info("Getting list of projects by user");
        LinkedList<Project> projectList = projectDAO.loadAllByUser(userID);
        for (Project project : projectList) {
            projectDAO.removeUserFromProject(userID, project.getProjectID());
        }
        userDAO.removeUser(userID);
    }

    /**
     * Load the user specified by his email.
     *
     * @param userID The user email that is stored in the database.
     * @return The user object containing the given email.
     * @throws EmptyResultDataAccessException Only thrown, if the email is not existing.
     */
    public User getByID(String userID) throws EmptyResultDataAccessException {
        User user = userDAO.findByID(userID);
        int xp = 0;
        LinkedList<Subtask> listOfSubtasks = subtaskDAO.loadAllByUser(userID);
        if (listOfSubtasks != null && !listOfSubtasks.isEmpty()) {
            for (Subtask subtask : listOfSubtasks) {
                xp += subtask.getXp();
            }
        }
        user.setLevel(levelService.getLevelByXp("User", xp));
        return user;
    }

    /**
     * Loads a specific user for authentication containing only security attributes.
     *
     * @param userID The user email that is stored in the database.
     * @return The user object containing the encrypted password and salt.
     * @throws EmptyResultDataAccessException Only thrown, if the email is not existing.
     */
    public User authUser(String userID) throws EmptyResultDataAccessException {
        return userDAO.authUser(userID);
    }

    /**
     * Loads all available user.
     *
     * @return A linked list of user objects.
     */
    public LinkedList<User> getAllUsers() {
        return userDAO.loadAll();
    }

    /**
     * Loads all user from a specific project.
     *
     * @param projectID The project ID that is given when written to the database.
     * @return A linked list of user objects.
     * @throws EmptyResultDataAccessException Only thrown, if the project ID is not existing.
     */
    public LinkedList<UserRole> getAllUsersFromProject(int projectID) throws EmptyResultDataAccessException {
        return userDAO.loadAllByProject(projectID);
    }

    /**
     * Updates information of a specific user.
     * TODO Make it possible to change the email address.
     *
     * @param userID The user email that is stored in the database.
     * @param user A user object containing the new information.
     */
    public void updateUser(String userID, User user)
            throws EmptyResultDataAccessException, IllegalArgumentException {
        if(user.getFirstName() != null && user.getLastName().length() > 0) {
            if(user.getLastName() != null && user.getLastName().length() > 0) {
                if(user.getAvatar() != null && user.getAvatar().length() > 0) {
                    if(user.getPassword() != null) {
                        if(user.getUserID() != null && user.getUserID().length() > 5) {
                            userDAO.updateUser(userID, user);
                        } else {
                            throw new IllegalArgumentException("User ID is not valid");
                        }
                    } else {
                        throw new IllegalArgumentException("Password is not valid");
                    }
                } else {
                    throw new IllegalArgumentException("Avatar is not valid");
                }
            } else {
                throw new IllegalArgumentException("Last name is not valid");
            }
        } else {
            throw new IllegalArgumentException("First name is not valid");
        }
        userDAO.updateUser(userID, user);
    }

}
