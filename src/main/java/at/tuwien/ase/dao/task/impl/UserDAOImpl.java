package at.tuwien.ase.dao.task.impl;

import at.tuwien.ase.dao.task.UserDAO;
import at.tuwien.ase.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 20/11/2015.
 */
@Repository
public class UserDAOImpl implements UserDAO {

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }

    // @author Tomislav Nikic
    public User insertUser(User user) {
        this.jdbcTemplate.update(
                "INSERT INTO TASKIT_USER (ID, FIRSTNAME, LASTNAME, PASSWORD, MAIL) VALUES (?,?,?,?,?)",
                user.getEmail(), user.getFirstName(), user.getLastName(),user.getPassword(), user.getEmail()
        );
        return user;
    }

    public boolean removeUser(String uID) {
        //TODO
        return false;
    }

    public User findByID(String uID) {
        //this.jdbcTemplate.queryForObject(
        //        "SELECT MAIL, FIRSTNAME, LASTNAME, PASSWORD"
        //);
        return null;
    }

    public LinkedList<User> loadAll() {
        return null;
    }

    public LinkedList<User> loadAllByProject(String pID) {
        return null;
    }

}
