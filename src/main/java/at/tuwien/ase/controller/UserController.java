package at.tuwien.ase.controller;

import at.tuwien.ase.model.user.RegistrationUnit;
import at.tuwien.ase.model.user.User;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Tomislav on 10/11/2015.
 */

@RestController
public class UserController {

    @RequestMapping(value = "/user/test", method = RequestMethod.GET)
    public @ResponseBody RegistrationUnit test() {
        RegistrationUnit testUser = new RegistrationUnit();
        testUser.setEmail("test");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setPassword("test");
        return testUser;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody User user(@RequestBody RegistrationUnit newUser) {
        User user = newUser.createUser();
        user.toFile();
        System.out.println("Created new user " + user.getEmail());
        //TODO Save user to db
        return user;
    }

    //TODO RestController for login and authentication

}
