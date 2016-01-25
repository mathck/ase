package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.LoginDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;

/**
 * Created by DanielHofer on 26.11.2015.
 */


@Repository
public class LoginDAOImpl implements LoginDAO{

    private static final Logger logger = LogManager.getLogger(LoginDAOImpl.class);

    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;

    @Autowired
    public void setDataSource(DataSource dataSource)  throws Exception {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }


/*    public boolean checkCredentials(String email, String password)  throws Exception {

        logger.debug("select from db: check login credentials for user mail="+email);

        int count = this.jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TASKIT_USER WHERE MAIL = ? AND PASSWORD = ?", Integer.class, email, password.getBytes());

        if (count > 0){
            return true;
        }else{
            return false;
        }

    }*/

    public void addUserToken(String email, String token, Date creationDate) throws DataAccessException {

        logger.debug("update db: user token for user mail="+email);

        String sqlQuery  = "UPDATE TASKIT_USER SET AUTH_TOKEN = ?, AUTH_TOKEN_CREATION_DATE = ? WHERE MAIL = ?";

        this.jdbcTemplate.update(
                sqlQuery,
                token,
                creationDate,
                email);
    }

    public void deleteUserToken(String email) throws DataAccessException {

        logger.debug("update db: remove user token for user mail="+email);

        String sqlQuery  = "UPDATE TASKIT_USER SET AUTH_TOKEN = ?, AUTH_TOKEN_CREATION_DATE = ? WHERE MAIL = ?";

        this.jdbcTemplate.update(
                sqlQuery,
                null, null, email);

    }

    public boolean checkLoginValidity(String token, Integer tokenValidityInMins)  throws DataAccessException {

        logger.debug("select from db: check login validity");

        String sqlQuery  = "SELECT COUNT(*) FROM TASKIT_USER WHERE AUTH_TOKEN = ? AND AUTH_TOKEN_CREATION_DATE > (NOW() - INTERVAL '"+tokenValidityInMins+" mins')";

        int count = this.jdbcTemplate.queryForObject(
                sqlQuery,
                Integer.class,
                token);

        if (count > 0){
            return true;
        }else{
            return false;
        }

    }

    public int getCountLoginFailsWithinCooldown(String email, Integer loginCooldownInMins) throws DataAccessException {

        logger.debug("select from db: get count login fails within cooldown");

        String sqlQuery  = "SELECT LOGIN_CURRENT_FAILS FROM TASKIT_USER WHERE LOGIN_LAST_LOGIN_ATTEMPT > (NOW() - INTERVAL '"+loginCooldownInMins+" mins') AND MAIL = ? ";

        int countLoginFails = this.jdbcTemplate.queryForObject(
                sqlQuery,
                Integer.class,
                email);

        return countLoginFails;
    }

    public void resetCurrentLoginFails(String email) throws DataAccessException {

        logger.debug("update db: reset login counter");

        String sqlQuery  = "UPDATE TASKIT_USER SET LOGIN_CURRENT_FAILS = ? WHERE MAIL = ?";

        this.jdbcTemplate.update(
                sqlQuery,
                new Integer(0),
                email);
    }

    public void incrementCurrentLoginFails(String email) throws DataAccessException {

        logger.debug("update db: increment login counter");

        String sqlQuery  = "UPDATE TASKIT_USER SET LOGIN_CURRENT_FAILS = LOGIN_CURRENT_FAILS + 1 WHERE MAIL = ?";

        this.jdbcTemplate.update(
                sqlQuery,
                email);
    }

    public void updateLastLoginAttempt(String email) throws DataAccessException {

        logger.debug("update db: update lastLoginAttempt");

        String sqlQuery  = "UPDATE TASKIT_USER SET LOGIN_LAST_LOGIN_ATTEMPT = ? WHERE MAIL = ?";

        this.jdbcTemplate.update(
                sqlQuery,
                new Date(),
                email
        );
    }

    public String getUserIdByToken(String token) throws Exception {
        String sqlQuery = "SELECT mail " +
                "FROM taskit_user " +
                "WHERE auth_token = ?";
        return this.jdbcTemplate.queryForObject(
                sqlQuery,
                String.class,
                token
        );
    }

}


