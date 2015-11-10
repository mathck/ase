package at.tuwien.ase.service;

import at.tuwien.ase.domain.user.User;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Tomislav on 10/11/2015.
 */

@RestController
public class UserController {

    @RequestMapping(value = "/user/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "Test successful";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public User user(@RequestBody User user) {
        System.out.println("Created new user " + user.getEmail());
        //TODO Save user to db
        return user;
    }

    //TODO RestController for login and authentication

}
