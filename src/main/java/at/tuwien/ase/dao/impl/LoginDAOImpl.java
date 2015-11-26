package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.LoginDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    final int tokenValidityInMins = 10;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }


    public boolean checkCredentials(String email, String password) {

        logger.debug("select from db: check login credentials for user mail="+email);

        int count = this.jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TASKIT_USER WHERE MAIL = ? AND PASSWORD = ?", Integer.class, email, password.getBytes());

        if (count > 0){
            return true;
        }else{
            return false;
        }

    }

    public void addUserToken(String email, String token, Date creationDate) {

        logger.debug("insert into db: user token for user mail="+email);

        this.jdbcTemplate.update(
                "UPDATE TASKIT_USER SET AUTH_TOKEN = ?, AUTH_TOKEN_CREATION_DATE = ? WHERE MAIL = ?",
                token, creationDate, email);


    }

    public boolean checkLoginValidity(String token) {

        logger.debug("select from db: check login validity");

        int count = this.jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TASKIT_USER WHERE AUTH_TOKEN = ? AND AUTH_TOKEN_CREATION_DATE > (NOW() - INTERVAL '"+this.tokenValidityInMins+" mins')", Integer.class, token);

        if (count > 0){
            return true;
        }else{
            return false;
        }

    }



}


