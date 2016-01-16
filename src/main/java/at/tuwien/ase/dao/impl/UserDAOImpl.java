package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.model.UserRole;
import at.tuwien.ase.model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
 * The PostgreSQL implementation of the User DAO interface. Using annotations it is possible to
 * Autowire to this class.
 *
 * @author Tomislav Nikic
 * @version 1.0, 13.12.2015
 */
@Repository
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    /**
     * Creating the JDBC template and key holder.
     *
     * @param dataSource Is provided through @Autowired. Needed to connect through SQL.
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }

    /**
     * Inserts a user into the database and stores them through a INSERT SQL query.
     *
     * @param user The user object that should be inserted.
     */
    public void insertUser(User user) {
        logger.info("Inserting user <" + user.getLastName() + " " + user.getFirstName() + "> into DB");
        String sqlQuery = "INSERT INTO taskit_user (mail, firstname, lastname, password_encr, avatar_url, salt, login_current_fails) " +
                "VALUES (?,?,?,?,?,?,?)";
        this.jdbcTemplate.update(
                sqlQuery,
                user.getUserID(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getAvatar(),
                user.getSalt(),
                new Integer(0)
        );
    }

    /**
     * Removes a user from the database querying first for the user by his email address.
     *
     * @param userID The user email that is stored in the database.
     */
    public void removeUser(String userID) {
        logger.info("Removing user <" + userID + "> from DB");
        String sqlQuery = "DELETE " +
                "FROM taskit_user " +
                "WHERE mail = ?";
        this.jdbcTemplate.update(
                sqlQuery,
                userID
        );
    }

    /**
     * Updates an existing user in the database with new values that are provided through
     * the parameter.
     *
     * @param userID The user email that is stored in the database.
     * @param user The user object with the updated entries.
     */
    public void updateUser(String userID, User user) {
        logger.info("Updating user <" + userID + "> on DB");
        String sqlQuery = "UPDATE taskit_user " +
                "SET " +
                "firstname = COALESCE (?, firstname), " +
                "lastname = COALESCE (?, lastname), " +
                "password = COALESCE (?, password_encr), " +
                "avatar_url = COALESCE (?, avatar_url) " +
                "WHERE mail = ?";
        this.jdbcTemplate.update(
                sqlQuery,
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getAvatar(),
                userID
        );
    }

    /**
     * Read from the database by using the email provided by the user.
     *
     * @param userID The user email that is stored in the database.
     * @return A user object having the unique email provided by userID.
     * @throws EmptyResultDataAccessException Only thrown, if the email is not existing.
     */
    public User findByID(String userID) throws EmptyResultDataAccessException {
        logger.info("Searching for user <" + userID + "> in DB");
        String sqlQuery = "SELECT mail, firstname, lastname, avatar_url " +
                "FROM taskit_user " +
                "WHERE mail = ?";
        return this.jdbcTemplate.queryForObject(
                sqlQuery,
                new String[]{userID},
                new RowMapper<User>() {
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user = new User();
                        user.setUserID(resultSet.getString("mail"));
                        user.setFirstName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        user.setAvatar(resultSet.getString("avatar_url"));
                        logger.info("Found " + user.getFirstName() + " " + user.getLastName() + "in DB");
                        return user;
                    }
                }
        );
    }

    /**
     * Reads a user from the database like findByID, but pulls the password and salt,
     * for authentication.
     *
     * @param userID The user email that is stored in the database.
     * @return A user object containing the email, encrypted password and salt.
     * @throws EmptyResultDataAccessException Only thrown, if the email is not existing.
     */
    public User authUser(String userID) throws EmptyResultDataAccessException {
        logger.info("Searching for user <" + userID + "> in DB to authenticate");
        String sqlQuery = "SELECT mail, password_encr, salt " +
                "FROM taskit_user " +
                "WHERE mail = ?";
        return this.jdbcTemplate.queryForObject(
                sqlQuery,
                new String[]{userID},
                new RowMapper<User>() {
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user = new User();
                        user.setUserID(resultSet.getString("mail"));
                        user.setPasswordEnc(resultSet.getString("password_encr"));
                        user.setSalt(resultSet.getBytes("salt"));
                        logger.info("Found <" + user.getUserID() + "> in DB");
                        return user;
                    }
                }
        );
    }

    /**
     * Loads all users from the database and returns them as a LinkedList of user objects.
     *
     * @return A linked list of users.
     */
    public LinkedList<User> loadAll() {
        logger.info("Loading all users from DB");
        String sqlQuery = "SELECT mail, firstname, lastname, avatar_url " +
                "FROM taskit_user";
        List<User> list = this.jdbcTemplate.query(
                sqlQuery,
                new RowMapper<User>() {
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user = new User();
                        user.setUserID(resultSet.getString("mail"));
                        user.setFirstName(resultSet.getString("firstname"));
                        user.setLastName(resultSet.getString("lastname"));
                        user.setAvatar(resultSet.getString("avatar_url"));
                        return user;
                    }
                }
        );
        return new LinkedList<User>(list);
    }

    /**
     * Reads all users from the projects referenced by the project ID provided.
     *
     * @param projectID The project ID that is given when written to the database.
     * @return A linked list of UserRole objects.
     * @throws EmptyResultDataAccessException Only thrown, if the email is not existing.
     */
    public LinkedList<UserRole> loadAllByProject(int projectID) throws EmptyResultDataAccessException {
        logger.info("Loading all users of project <" + projectID + "> from DB");
        String sqlQuery = "SELECT user_mail, project_id, role " +
                "FROM rel_user_project " +
                "WHERE project_id = ?";
        List<UserRole> list = this.jdbcTemplate.query(
                sqlQuery,
                new Object[]{projectID},
                new RowMapper<UserRole>() {
                    public UserRole mapRow(ResultSet resultSet, int i) throws SQLException {
                        UserRole user = new UserRole();
                        user.setUser(resultSet.getString("user_mail"));
                        user.setProject(resultSet.getInt("project_id"));
                        user.setRole(resultSet.getString("role"));
                        return user;
                    }
                }
        );
        return new LinkedList<UserRole>(list);
    }

}
