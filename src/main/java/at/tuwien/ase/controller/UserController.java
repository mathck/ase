package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.User;
import at.tuwien.ase.security.PermissionEvaluator;
import at.tuwien.ase.services.LoginService;
import at.tuwien.ase.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.LinkedList;
import java.util.Set;

/**
 * The controller implementation for user management.
 *
 * @author Tomislav Nikic
 * @version 1.0, 14.12.2015
 */

@RestController
public class UserController {

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private PermissionEvaluator permissionEvaluator;

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
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if(constraintViolations.size() == 0) {
            userService.writeUser(user);
        } else {
            throw new ValidationException("Data has failed validation test!");
        }
    }

    /**
     * This is a controller used for getting a user object. To do so, a user ID (email) must be
     * provided.
     *
     * @param userID Consumes a user id (email)
     * @return User                    Returns the user object stored in the database
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@RequestParam("uID") String userID) throws Exception {
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
    public void updateUser(@RequestParam("uID") String userID, @RequestBody User user, @RequestHeader("user-token") String token)
            throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),userID,"CHANGE_USER")) {
            throw new ValidationException("Not allowed");
        }
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if(constraintViolations.size() == 0) {
            userService.updateUser(userID, user);
        } else {
            throw new ValidationException("Data has failed validation test!");
        }
    }

    /**
     * This controller deletes a user from the database
     *
     * @param userID The user ID (email)
     */
    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteUser(@RequestParam("uID") String userID, @RequestHeader("user-token") String token) throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),userID,"CHANGE_USER")) {
            throw new ValidationException("Not allowed");
        }
        userService.deleteUser(userID);
    }

    @RequestMapping(value = "/user/pagination", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<User> relatedUser(@RequestParam("uID") String userId, @RequestHeader("user-token") String token)
            throws Exception {
        if(!permissionEvaluator.hasPermission(loginService.getUserIdByToken(token),userId,"CHANGE_USER")) {
            throw new ValidationException("Not allowed");
        }
        return userService.getRelatedUser(userId);
    }

    @RequestMapping(value = "/user/search", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<User> searchUser (@RequestParam("string") String search) throws Exception {
        return userService.getUserList(search);
    }

}
