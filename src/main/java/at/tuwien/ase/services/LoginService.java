package at.tuwien.ase.services;

import at.tuwien.ase.model.Issue;
import at.tuwien.ase.model.LoginToken;
import org.springframework.dao.DataAccessException;

/**
 * Created by DanielHofer on 16.11.2015.
 */
public interface LoginService {

    /**
     *
     * login service for {@link at.tuwien.ase.model.User} specified by email.
     * Credentials of {@link at.tuwien.ase.model.User} are validated.
     *
     * @param email       id of {@link at.tuwien.ase.model.User}.
     * @param password    {@link at.tuwien.ase.model.User} password.
     * @return            {@link LoginToken} object.
     * @throws Exception      if an exception occurred
     */
    LoginToken login(String email, String password) throws  Exception;

    /**
     *
     * Logout service for {@link at.tuwien.ase.model.User} specified by email.
     * Login token of {@link at.tuwien.ase.model.User} will be removed.
     *
     * @param email       id of {@link at.tuwien.ase.model.User}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void logout(String email) throws  DataAccessException;

    /**
     *
     * Check login validity by {@link LoginToken}.
     *
     * @param token  {@link LoginToken} object.
     * @return       True, if login is valid. False, if login is not valid.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    boolean checkLogin(String token) throws DataAccessException;

    /**
     *
     * Get id of {@link at.tuwien.ase.model.User} by {@link LoginToken}.
     *
     * @param token   {@link LoginToken} object.
     * @return        Id of {@link at.tuwien.ase.model.User}.
     * @throws Exception      if an exception occurred
     */
    String getUserIdByToken(String token) throws Exception;

}
