package at.tuwien.ase.services.impl;

import at.tuwien.ase.model.Login;
import at.tuwien.ase.model.LoginUnit;
import at.tuwien.ase.services.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by DanielHofer on 16.16.11.2015.
 */

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);

    public Login login(LoginUnit newLogin) {
        Login login  = newLogin.createLogin();
        login.toFile();
        System.out.println("Received new login " + login.getEmail());
        //TODO Authenticate user and send token

        return login;
    }
}
