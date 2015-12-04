package at.tuwien.ase.services.impl;
import at.tuwien.ase.controller.PasswordEncryption;
import at.tuwien.ase.dao.LoginDAO;
import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.model.user.User;
import at.tuwien.ase.security.TokenGenerator;
import at.tuwien.ase.services.LoginService;
import at.tuwien.ase.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by DanielHofer on 16.16.11.2015.
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDAO loginDAO;

    @Autowired
    private UserDAO userDAO;

    private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);

    public String login(String email, String password) throws  Exception{

        User user;
        String token = "";

        user = userDAO.authUser(email);

        if (PasswordEncryption.authenticate(password, user.getPassword(), user.getSalt())){

            token = TokenGenerator.createNewToken();

            loginDAO.addUserToken(email, token, new Date());


        }else{
            throw new Exception ("Authentication failed! User mail or password incorrect!");
        }

        return token;

    }

    public void logout(String email) throws Exception {
        loginDAO.deleteUserToken(email);
    }

    public boolean checkLogin(String token) throws Exception {

        return loginDAO.checkLoginValidity(token);

    }


}
