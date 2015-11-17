package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.user.RegistrationUnit;
import at.tuwien.ase.model.user.User;
import at.tuwien.ase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Tomislav Nikic on 10/11/2015.
 */

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    @RequestMapping(value = "/user/test", method = RequestMethod.GET)
    public
    @ResponseBody
    RegistrationUnit test() throws Exception {
        RegistrationUnit testUser = new RegistrationUnit();
        testUser.setEmail("testmail");
        testUser.setFirstName("testname");
        testUser.setLastName("testlastname");
        testUser.setPassword("testPW");
        return testUser;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public
    @ResponseBody
    RegistrationUnit testList() throws Exception {
        //TODO return list of users (if necessary?)
        return null;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    User user(@RequestBody RegistrationUnit newUser) throws Exception {

        return userService.addUser(newUser);

    }

    //TODO RestController for login and authentication

}
