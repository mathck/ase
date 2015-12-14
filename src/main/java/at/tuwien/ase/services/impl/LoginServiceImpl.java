package at.tuwien.ase.services.impl;
import at.tuwien.ase.dao.LoginDAO;
import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.model.LoginToken;
import at.tuwien.ase.model.User;
import at.tuwien.ase.security.TokenGenerator;
import at.tuwien.ase.services.LoginService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by DanielHofer on 16.11.2015.
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDAO loginDAO;

    @Autowired
    private UserDAO userDAO;

    @Value("${authentication.login.maxLoginAttempts}")
    private Integer maxLoginAttempts;

    @Value("${authentication.login.loginCooldownInMins}")
    private Integer loginCooldownInMins;

    @Value("${authentication.token.tokenValidityInMins}")
    private Integer tokenValidityInMins;

    private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);

    public LoginToken login(String email, String password) throws  Exception{

        User user;
        LoginToken token;
        int countLoginFails;

        //get user from db
        try{
            user = userDAO.authUser(email);
        }catch (EmptyResultDataAccessException exception){
            logger.info("User could not be found");
            throw new Exception ("Authentication failed! User mail or password incorrect!");
        }

        //get number of failed logins within cooldown period
        countLoginFails = 0;
        try {
            countLoginFails = loginDAO.getCountLoginFailsWithinCooldown(email, loginCooldownInMins);
        }catch (EmptyResultDataAccessException exception){
            logger.info("No failed login within cooldown period");
        }

        //cooldown period expired --> reset num login attempts
        if (countLoginFails == 0){
            loginDAO.resetCurrentLoginFails(email);
        }

        //threshold for may login attempts already exceeded?
        if (countLoginFails >= maxLoginAttempts) {
            throw new Exception("Authentication failed! User is locked for " + this.loginCooldownInMins + " minutes!");
        }

        //update date
        loginDAO.updateLastLoginAttempt(email);

        //authenticate user
        if (PasswordEncryption.authenticate(password, user.getPassword(), user.getSalt())) {
            //authentication successful

            //generate new token
            token = TokenGenerator.createNewToken();

            //add token to user
            loginDAO.addUserToken(email, token.getToken(), new Date());

            //reset num login attempts
            loginDAO.resetCurrentLoginFails(email);

        }else{
            //authentication NOT successful

            //increment counter for failed logins
            loginDAO.incrementCurrentLoginFails(email);

            //check login threshold exceeded
            if (countLoginFails + 1 >= maxLoginAttempts) {
                throw new Exception("Authentication failed! User is locked for " + this.loginCooldownInMins + " minutes!");
            }else{
                throw new Exception ("Authentication failed! User mail or password incorrect!");
            }

        }

        return token;

    }

    public void logout(String email) throws Exception {
        loginDAO.deleteUserToken(email);
    }

    public boolean checkLogin(String token) throws Exception {

        return loginDAO.checkLoginValidity(token, tokenValidityInMins);

    }


}
