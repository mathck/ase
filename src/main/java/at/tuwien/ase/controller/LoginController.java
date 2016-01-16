//package at.tuwien.ase.controller;
//
//import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
//import at.tuwien.ase.model.LoginToken;
//import at.tuwien.ase.services.LoginService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
///**
// * Created by Andreas on 15.11.2015.
// */
//
//@RestController
//@RequestMapping("/api/")
//public class LoginController {
//
//    @Autowired
//    private LoginService loginService;
//
//    @Autowired
//    private GenericRestExceptionHandler genericRestExceptionHandler;
//
//    // @author Daniel Hofer
//    @RequestMapping(value = "user/login", method = RequestMethod.GET)
//    @ResponseBody
//    public LoginToken login(@RequestParam String email, @RequestParam String password) throws Exception {
//        return loginService.login(email, password);
//    }
//
//    // @author Daniel Hofer
//    @RequestMapping(value = "user/logout", method = RequestMethod.PATCH)
//    @ResponseBody
//    public void logout(@RequestParam String email) throws Exception {
//        loginService.logout(email);
//    }
//
//}
