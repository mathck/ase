package at.tuwien.ase.services;

import at.tuwien.ase.model.LoginUnit;
import at.tuwien.ase.model.Login;

/**
 * Created by DanielHofer on 16.16.11.2015.
 */
public interface LoginService {

    /**
     * login service
     *
     * @param newLogin
     * @return created Login
     */
    public Login login(LoginUnit newLogin);

}
