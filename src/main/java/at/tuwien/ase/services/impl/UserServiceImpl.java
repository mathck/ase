package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.model.UserRole;
import at.tuwien.ase.model.User;
import at.tuwien.ase.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


/**
 * Created by Daniel Hofer on 16.16.11.2015.
 */

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private UserDAO userDAO;

	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

	public UserServiceImpl()
	{
	}

	public UserServiceImpl(UserDAO userDAO)
	{
		this.userDAO = userDAO;
	}

	public void writeUser(User user)
	{
		userDAO.insertUser(user);
	}

	public void deleteUser(String uID)
	{
		userDAO.removeUser(uID);
	}

	public User getByID(String uID)
	{
		return userDAO.findByID(uID);
	}

	public User authUser(String uID) {
		return userDAO.authUser(uID);
	}

	public LinkedList<User> getAllUsers()
	{
		return userDAO.loadAll();
	}

	public LinkedList<UserRole> getAllUsersFromProject(int pID)
	{
		return userDAO.loadAllByProject(pID);
	}

	public void updateUser(String uID, User user)
	{
		userDAO.updateUser(uID, user);
	}

}
