package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.User;
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
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService us;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    /**
     * This is a controller used for creating/posting a new user
     * <p>
     *     The new user must not be in the database and will be writen to it.
     *     Since the users email is the ID, nothing will be returned
     * </p>
     *
     * @author		Tomislav Nikic
     * @since		2015-12-01
     * @param 		user					Consumes a user object containing firstname,
     *                                 		lastname, password, avatar, and mail
     * @throws 		Exception
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void createUser(@RequestBody User user) throws Exception {
        us.writeUser(user);
    }

    /**
     * This is a controller used for getting a user object.
     * To do so, a user ID (email) must be provided.
     *
     * @author		Tomislav Nikic
     * @since		2015-12-01
     * @param 		uID						Consumes a user id (email)
     * @return		User					Returns the user object stored in the database
     * @throws 		Exception
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@RequestParam("uID") String uID) throws Exception {
        logger.debug("get user with id " + uID);
        return us.getByID(uID);
    }

    /**
     * This is a controller that collects all the users available on the database
     * and returns them as a linked list
     *
     * @author		Tomislav Nikic
     * @since		2015-12-01
     * @return		LinkedList&lt;User&gt;	Returns a linked list of users
     */
    @RequestMapping(value = "/user/all", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<User> getAllUser() {
        return us.getAllUsers();
    }

    /**
     * This controller updates the referenced user
     * and replaces old values with new ones if added
     *
     * @author		Tomislav Nikic
     * @since		2015-12-01
     * @param 		uID						The user ID (email)
     * @param 		user					A user object containing firstname, lastname,
     *                                      avatar, password and user ID
     * @throws 		Exception
     */
    @RequestMapping(value = "/user", method = RequestMethod.PATCH, consumes = "application/json")
    @ResponseBody
    public void updateUser(@RequestParam("uID") String uID, @RequestBody User user) throws Exception {
        us.updateUser(uID, user);
    }

    /**
     * This controller deletes a user from the database
     *
     * @author		Tomislav Nikic
     * @since		2015-12-01
     * @param 		uID						The user ID (email)
     */
    @RequestMapping(value = "user", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteUser(@RequestParam("uID") String uID) {
        us.deleteUser(uID);
    }

}
