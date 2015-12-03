package at.tuwien.ase.dao;

import java.util.Date;

/**
 * Created by DanielHofer on 26.11.2015.
 */
public interface LoginDAO {

    boolean checkCredentials(String email, String password) throws Exception;

    void addUserToken(String email, String token, Date creationDate) throws Exception;

    void deleteUserToken(String email) throws Exception;

    boolean checkLoginValidity(String token) throws Exception;

}
