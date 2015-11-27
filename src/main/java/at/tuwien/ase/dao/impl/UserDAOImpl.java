package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.model.project.Role;
import at.tuwien.ase.model.project.UserRole;
import at.tuwien.ase.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class UserDAOImpl implements UserDAO
{
	private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	KeyHolder keyHolder;

	@Autowired
	public void setDataSource(DataSource dataSource)
	{
		logger.debug("Creating JDBC template");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.keyHolder = new GeneratedKeyHolder();
	}

	public void insertUser(User user)
	{
		logger.debug("Inserting user <" + user.getUserID() + "> into DB");
		String sqlQuery = "INSERT INTO taskit_user (mail, firstname, lastname, password, avatar_url, salt) " +
				"VALUES (?,?,?,?,?,?)";
		this.jdbcTemplate.update(
				sqlQuery,
				user.getUserID(),
				user.getFirstName(),
				user.getLastName(),
				user.getPassword(),
				user.getAvatar(),
				user.getSalt()
		);
	}

	public void removeUser(String uID)
	{
		logger.debug("Removing user <" + uID + "> from DB");
		String sqlQuery = "DELETE " +
				"FROM taskit_user " +
				"WHERE mail = ?";
		this.jdbcTemplate.update(
				sqlQuery,
				uID
		);
	}

	public void updateUser(String uID, User user)
	{
		logger.debug("Updating user <" + uID + "> on DB");
		String sqlQuery = "UPDATE taskit_user " +
				"SET " +
				"firstname = COALESCE (?, firstname), " +
				"lastname = COALESCE (?, lastname), " +
				"mail = COALESCE (?, mail), " +
				"password = COALESCE (?, password), " +
				"avatar_url = COALESCE (?, avatar_url) " +
				"WHERE mail = ?";
		this.jdbcTemplate.update(
				sqlQuery,
				user.getFirstName(),
				user.getLastName(),
				user.getUserID(),
				user.getPassword(),
				user.getAvatar(),
				uID
		);
	}

	public User findByID(String uID)
	{
		logger.debug("Searching for user <" + uID + "> in DB");
		String sqlQuery = "SELECT mail, firstname, lastname, avatar_url " +
				"FROM taskit_user " +
				"WHERE mail = ?";
		return this.jdbcTemplate.queryForObject(
				sqlQuery,
				new String[]{uID},
				new RowMapper<User>()
				{
					public User mapRow(ResultSet resultSet, int i) throws SQLException
					{
						User user = new User();
						user.setUserID(resultSet.getString("mail"));
						user.setFirstName(resultSet.getString("firstname"));
						user.setLastName(resultSet.getString("lastname"));
						user.setAvatar(resultSet.getString("avatar_url"));
						logger.debug("Found " + user.getFirstName() + " " + user.getLastName() + "in DB");
						return user;
					}
				}
		);
	}

	public User authUser(String uID)
	{
		logger.debug("Searching for user <" + uID + "> in DB to authenticate");
		String sqlQuery = "SELECT mail, password, salt " +
				"FROM taskit_user " +
				"WHERE mail = ?";
		return this.jdbcTemplate.queryForObject(
				sqlQuery,
				new String[]{uID},
				new RowMapper<User>()
				{
					public User mapRow(ResultSet resultSet, int i) throws SQLException
					{
						User user = new User();
						user.setUserID(resultSet.getString("mail"));
						user.setPassword(resultSet.getBytes("password"));
						user.setSalt(resultSet.getBytes("salt"));
						logger.debug("Found <" + user.getUserID() + "> in DB");
						return user;
					}
				}
		);
	}

	public LinkedList<User> loadAll()
	{
		logger.debug("Loading all users from DB");
		String sqlQuery = "SELECT mail, firstname, lastname, avatar_url " +
				"FROM taskit_user";
		List<User> list = this.jdbcTemplate.query(
				sqlQuery,
				new RowMapper<User>()
				{
					public User mapRow(ResultSet resultSet, int i) throws SQLException
					{
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

	public LinkedList<UserRole> loadAllByProject(int pID)
	{
		logger.debug("Loading all users of project <" + pID + "> from DB");
		String sqlQuery = "SELECT user_mail, project_id, role " +
				"FROM rel_user_project " +
				"WHERE project_id = ?";
		List<UserRole> list = this.jdbcTemplate.query(
				sqlQuery,
				new Object[]{pID},
				new RowMapper<UserRole>()
				{
					public UserRole mapRow(ResultSet resultSet, int i) throws SQLException
					{
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
