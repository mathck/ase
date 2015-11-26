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
    public String login(String email, String password) throws  Exception;

    public boolean checkLogin(String token) ;

}
