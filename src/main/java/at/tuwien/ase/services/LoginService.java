package at.tuwien.ase.services;

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
    String login(String email, String password) throws  Exception;

    void logout(String email) throws  Exception;

    boolean checkLogin(String token) throws Exception;

}
