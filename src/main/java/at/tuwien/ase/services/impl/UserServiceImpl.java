package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Subtask;
import at.tuwien.ase.model.UserRole;
import at.tuwien.ase.model.User;
import at.tuwien.ase.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


/**
 * Created by Daniel Hofer on 16.16.11.2015.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private SubtaskDAO subtaskDAO;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserDAO userDAO, ProjectDAO projectDAO, SubtaskDAO subtaskDAO) {
        this.userDAO = userDAO;
        this.projectDAO = projectDAO;
        this.subtaskDAO = subtaskDAO;
    }

    public void writeUser(User user) {
        userDAO.insertUser(user);
    }

    public void deleteUser(String uID) {
        logger.info("Starting process for deleting user <" + uID + ">");
        logger.info("Getting list of projects by user");
        LinkedList<Project> projectList = projectDAO.loadAllByUser(uID);
        for(Project project : projectList) {
            projectDAO.removeUserFromProject(uID, project.getProjectID());
        }
        userDAO.removeUser(uID);
    }

    public User getByID(String uID) throws EmptyResultDataAccessException {
        User user = userDAO.findByID(uID);
        user.setXp(0);
        for(Subtask subtask : subtaskDAO.loadAllByUser(uID)) {
            user.setXp(user.getXp() + subtask.getXp());
        }
        return user;
    }

    public User authUser(String uID) throws EmptyResultDataAccessException {
        return userDAO.authUser(uID);
    }

    public LinkedList<User> getAllUsers() {
        return userDAO.loadAll();
    }

    public LinkedList<UserRole> getAllUsersFromProject(int pID) throws EmptyResultDataAccessException {
        return userDAO.loadAllByProject(pID);
    }

    public void updateUser(String uID, User user) {
        userDAO.updateUser(uID, user);
    }

}
