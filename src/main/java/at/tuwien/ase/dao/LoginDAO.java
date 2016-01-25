package at.tuwien.ase.dao;

import org.springframework.dao.DataAccessException;

import java.util.Date;

/**
 * Created by DanielHofer on 26.11.2015.
 */
public interface LoginDAO {

    /*boolean checkCredentials(String email, String password) throws Exception;*/

    /**
     *
     * Add generated token to a user in db.
     *
     * @param email            User mail specifies the user.
     * @param token            Generated token.
     * @param creationDate     Token creation date.
     * @throws DataAccessException
     */
    void addUserToken(String email, String token, Date creationDate) throws DataAccessException;

    /**
     *
     * Remove token from db user. This terminates the login.
     *
     * @param email    User mail specifies the user.
     * @throws DataAccessException
     */
    void deleteUserToken(String email) throws DataAccessException;

    /**
     *
     * Check the validity of a login by login token.
     *
     * @param token                 Generated login token.
     * @param tokenValidityInMins   Token validity time specified in milliseconds.
     * @return                      True, if login is valid. False, if login is not valid.
     * @throws DataAccessException
     */
    boolean checkLoginValidity(String token, Integer tokenValidityInMins) throws DataAccessException;

    /**
     *
     * Get the count of unsuccessful login attempts from a user within a certain time.
     *
     * @param email                 User mail specifies the user.
     * @param loginCooldownInMins   Login cooldown in milliseconds.
     * @return                      count of failed logins within login cooldown period.
     * @throws DataAccessException
     */
    int getCountLoginFailsWithinCooldown(String email, Integer loginCooldownInMins) throws DataAccessException;

    /**
     *
     * Reset counter of unsuccessful login attempts from a specific user.
     *
     * @param email  User mail specifies the user.
     * @throws DataAccessException
     */
    void resetCurrentLoginFails(String email) throws DataAccessException;

    /**
     *
     * Increment counter of unsuccessful login attempts from a specific user.
     *
     * @param email  User mail specifies the user.
     * @throws DataAccessException
     */
    void incrementCurrentLoginFails(String email) throws DataAccessException;

    /**
     *
     * Updates {@link Date} of an unsuccessful login attempt from a specific user.
     *
     * @param email  User mail specifies the user.
     * @throws DataAccessException
     */
    void updateLastLoginAttempt(String email) throws DataAccessException;

    String getUserIdByToken(String token) throws Exception;

}
