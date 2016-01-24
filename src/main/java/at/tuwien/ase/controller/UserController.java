package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.User;
import at.tuwien.ase.services.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * The controller implementation for user management.
 *
 * @author Tomislav Nikic
 * @version 1.0, 14.12.2015
 */

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    /**
     * This is a controller used for creating/posting a new user <p> The new user must not be in the
     * database and will be writen to it. Since the users email is the ID, nothing will be returned
     *
     * @param user Consumes a user object containing firstname, lastname, password, avatar, and mail
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void createUser(@RequestBody User user) throws Exception {
        userService.writeUser(user);
    }

    /**
     * This is a controller used for getting a user object. To do so, a user ID (email) must be
     * provided.
     *
     * @param userID Consumes a user id (email)
     * @return User Returns the user object stored in the database
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@RequestParam("uID") String userID) throws Exception {
        logger.debug("get user with id " + userID);
        return userService.getByID(userID);
    }

    /**
     * This is a controller that collects all the users available on the database and returns them
     * as a linked list
     *
     * @return LinkedList&lt;User&gt;	Returns a linked list of users
     */
    @RequestMapping(value = "/user/all", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<User> getAllUser() {
        return userService.getAllUsers();
    }

    /**
     * This controller updates the referenced user and replaces old values with new ones if added
     *
     * @param userID  The user ID (email)
     * @param user A user object containing firstname, lastname, avatar, password and user ID
     */
    @RequestMapping(value = "/user", method = RequestMethod.PATCH, consumes = "application/json")
    @ResponseBody
    @PreAuthorize("hasPermission(#userID, 'CHANGE_USER')")
    public void updateUser(@RequestParam("uID") String userID, @RequestBody User user) throws Exception {
        userService.updateUser(userID, user);
    }

    /**
     * This controller deletes a user from the database
     *
     * @param userID The user ID (email)
     */
    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasPermission(#userID, 'CHANGE_USER')")
    public void deleteUser(@RequestParam("uID") String userID) {
        userService.deleteUser(userID);
    }

    @RequestMapping(value = "/user/pagination", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public LinkedList<User> relatedUser(@RequestParam("uID") String userId) throws Exception {
        return userService.getRelatedUser(userId);
    }

    @RequestMapping(value = "/user/search", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public LinkedList<User> searchUser (@RequestParam("string") String search) throws Exception {
        return userService.getUserList(search);
    }

}
