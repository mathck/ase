package at.tuwien.ase.dao;

import java.util.Date;

/**
 * Created by DanielHofer on 26.11.2015.
 */
public interface LoginDAO {

    /*boolean checkCredentials(String email, String password) throws Exception;*/

    void addUserToken(String email, String token, Date creationDate) throws Exception;

    void deleteUserToken(String email) throws Exception;

    boolean checkLoginValidity(String token, Integer tokenValidityInMins) throws Exception;

    int getCountLoginFailsWithinCooldown(String email, Integer loginCooldownInMins) throws Exception;

    void resetCurrentLoginFails(String email) throws Exception;

    void incrementCurrentLoginFails(String email) throws Exception;

    void updateLastLoginAttempt(String email) throws Exception;

    String getUserIdByToken(String token) throws Exception;

}
