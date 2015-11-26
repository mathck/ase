package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.user.User;
import at.tuwien.ase.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 10/11/2015.
 */

@RestController
public class UserController
{

	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService us;

	@Autowired
	private GenericRestExceptionHandler genericRestExceptionHandler;

	// @author Tomislav Nikic
	@RequestMapping(value = "/user/register", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void createUser(@RequestBody User user) throws Exception
	{
		us.writeUser(user);
	}

	// @author Tomislav Nikic
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	public User getUser(@RequestParam("uID") String uID) throws Exception
	{
		logger.debug("get user with id " + uID);
		return us.getByID(uID);
	}

	@RequestMapping(value = "/user/all", method = RequestMethod.GET)
	@ResponseBody
	public LinkedList<User> getAllUser()
	{
		return us.getAllUsers();
	}

	//@RequestMapping(value = "/user", method = RequestMethod.PATCH, consumes = "application/json")
	//@ResponseBody
	//public void updateUser(@RequestParam("uID") String uID, @RequestBody User user) throws Exception
	//{
	//	us.updateUser(uID, ru.getEmail(), ru.getPassword(), ru.getFirstName(), ru.getLastName(), ru.getAvatar());
	//}

	@RequestMapping(value = "user", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteUser(@RequestParam("uID") String uID)
	{
		us.deleteUser(uID);
	}

}
