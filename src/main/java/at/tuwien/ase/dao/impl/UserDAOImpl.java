package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

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

    public void insertUser(User user) {
        this.jdbcTemplate.update(
                "INSERT INTO taskit_user (mail, firstname, lastname, password, avatar_url, salt) VALUES (?,?,?,?,?,?)",
                user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getAvatar(), user.getSalt()
        );
    }

    public void removeUser(String uID) {
        this.jdbcTemplate.update(
                "DELETE FROM taskit_user WHERE mail = ?",
                uID
        );
    }

    public User findByID(String uID) {
        return this.jdbcTemplate.queryForObject(
                "SELECT mail, firstname, lastname, avatar_url FROM taskit_user WHERE mail = ?",
                new String[]{uID},
                new RowMapper<User>() {
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user = new User();
                        user.setEmail(resultSet.getString("mail"));
                        user.setName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        user.setAvatar(resultSet.getString("avatar_url"));
                        user.setProjectList(null);
                        return user;
                    }
                }
        );
    }

    public LinkedList<User> loadAll() {
        List<User> list = this.jdbcTemplate.query(
                "SELECT mail, firstname, lastname, avatar_url FROM taskit_user",
                new RowMapper<User>() {
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user = new User();
                        user.setEmail(resultSet.getString("mail"));
                        user.setName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        user.setAvatar(resultSet.getString("avatar_url"));
                        return user;
                    }
                }
        );
        return new LinkedList<User>(list);
    }

    public LinkedList<User> loadAllByProject(String pID) {
        List<User> list = this.jdbcTemplate.query(
                "SELECT mail, firstname, lastname, avatar_url " +
                        "FROM taskit_user JOIN rel_user_project ON taskit_user.mail = rel_user_project.user_mail " +
                        "WHERE project_id = ?",
                new Object[]{pID},
                new RowMapper<User>() {
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user = new User();
                        user.setEmail(resultSet.getString("mail"));
                        user.setName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        user.setAvatar(resultSet.getString("avatar_url"));
                        return user;
                    }
                }
        );
        return new LinkedList<User>(list);
    }

}
