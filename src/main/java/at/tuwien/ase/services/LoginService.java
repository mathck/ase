package at.tuwien.ase.services;

import at.tuwien.ase.model.LoginToken;

/**
 * Created by DanielHofer on 16.16.11.2015.
 */
public interface LoginService {

    /**
     * login service
     *
     * @param email, password
     * @return created Login
     */
    LoginToken login(String email, String password) throws  Exception;

    void logout(String email) throws  Exception;

    boolean checkLogin(String token) throws Exception;

}
