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
    private UserService us;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    // @author Tomislav Nikic
    @RequestMapping(value = "/user/test", method = RequestMethod.GET)
    @ResponseBody
    public RegistrationUnit test() throws Exception {
        RegistrationUnit testUser = new RegistrationUnit();
        testUser.setEmail("testmail");
        testUser.setFirstName("testname");
        testUser.setLastName("testlastname");
        testUser.setPassword("testPW");
        return testUser;
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public User createUser(@RequestBody RegistrationUnit ru) throws Exception {
        return us.writeUser(ru);
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "/user/{uID}", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable("uID") String uID) throws Exception {
        return us.getByID(uID);
    }

    @RequestMapping(value = "/user/{uID}", method = RequestMethod.PATCH, consumes = "application/json")
    @ResponseBody
    public User updateUser(@PathVariable("uID") String uID, @RequestBody RegistrationUnit ru) throws Exception {
        return us.updateUser(uID, ru.getEmail(),ru.getPassword(),ru.getFirstName(),ru.getLastName(),ru.getAvatar());
    }

}
