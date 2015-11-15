package at.tuwien.ase.controller;

import at.tuwien.ase.model.user.Login;
import at.tuwien.ase.model.user.LoginUnit;
import at.tuwien.ase.model.user.RegistrationUnit;
import at.tuwien.ase.model.user.User;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Andreas on 15.11.2015.
 */

@RestController
public class LoginController {

    @RequestMapping(value = "/user/login", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody Login login(@RequestBody LoginUnit newLogin) {
        Login login  = newLogin.createLogin();
        login.toFile();
        System.out.println("Received new login " + login.getEmail());
        //TODO Authenticate user and send token
        return login;
    }

}
