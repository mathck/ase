package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.user.Login;
import at.tuwien.ase.model.user.LoginUnit;
import at.tuwien.ase.model.user.RegistrationUnit;
import at.tuwien.ase.model.user.User;
import at.tuwien.ase.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Andreas on 15.11.2015.
 */

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    @RequestMapping(value = "/user/login", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    Login login(@RequestBody LoginUnit newLogin) throws Exception {

        return loginService.login(newLogin);
    }

}
