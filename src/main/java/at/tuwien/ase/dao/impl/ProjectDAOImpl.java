package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.model.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomislav Nikic on 20/11/2015.
 */
@Repository
public class ProjectDAOImpl implements ProjectDAO
{

	private static final Logger logger = LogManager.getLogger(ProjectDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	KeyHolder keyHolder;

	@Autowired
	public void setDataSource(DataSource dataSource)
	{
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.keyHolder = new GeneratedKeyHolder();
	}

	public int getNewProjectID() {

		Integer id = this.jdbcTemplate.queryForObject(
				"SELECT nextval('seq_project_id')",
				Integer.class);

		return id;
	}

	public int getNewRelationID() {

		Integer id = this.jdbcTemplate.queryForObject(
				"SELECT nextval('rel_user_project_id_seq')",
				Integer.class);

		return id;
	}

	public int insertProject(final Project project)
	{
		logger.debug("Inserting project <" + project.getProjectID() + ":" + project.getTitle() + "> into DB");
		int id = this.getNewProjectID();
		final String sqlQuery = "INSERT INTO project (id, description, name, creation_date, update_date) " +
				"VALUES (?,?,?,?,?)";
		this.jdbcTemplate.update(
				sqlQuery,
				id,
				project.getDescription(),
				project.getTitle(),
				project.getCreationTime(),
				project.getUpdateTime()
		);
		return id;
	}

	public void removeProject(int pID)
	{
		logger.debug("Removing project <" + pID + "> from DB");
		String sqlQuery = "DELETE " +
				"FROM project " +
				"WHERE id = ?";
		this.jdbcTemplate.update(
				sqlQuery,
				pID
		);
	}

	public void updateProject(int pID, Project project)
	{
		logger.debug("Updating project <" + pID + "> on DB");
		String sqlQuery = "UPDATE project " +
				"SET " +
				"id = COALESCE (?, id), " +
				"description = COALESCE (?, description), " +
				"name = COALESCE (?, name), " +
				"creation_date = COALESCE (?, creation_date), " +
				"update_date = COALESCE (?, update_date) " +
				"WHERE id = ?";
		this.jdbcTemplate.update(
				sqlQuery,
				project.getProjectID(),
				project.getDescription(),
				project.getTitle(),
				project.getCreationTime(),
				project.getUpdateTime(),
				pID
		);
	}

	public void addUserToProject(String uID, int pID, String role)
	{
		logger.debug("Inserting user <" + uID + "> into project <" + pID + "> and storing on DB");
		String sqlQuery = "INSERT INTO rel_user_project (id, user_mail, project_id, role) " +
				"VALUES (?,?,?,?)";
		this.jdbcTemplate.update(
				sqlQuery,
				this.getNewRelationID(),
				uID,
				pID,
				role.toString()
		);
	}

	public void removeUserFromProject(String uID, int pID)
	{
		logger.debug("Removing user <" + uID + "> from project <" + pID + "> on DB");
		String sqlQuery = "DELETE " +
				"FROM rel_user_project " +
				"WHERE project_id = ? AND user_mail = ?";
		this.jdbcTemplate.update(
				sqlQuery,
				pID,
				uID
		);
	}

	public Project findByID(int pID)
	{
		logger.debug("Searching for projects <" + pID + "> in DB");
		String sqlQuery = "SELECT id, description, name, creation_date, update_date " +
				"FROM project " +
				"WHERE id = ?";
		return this.jdbcTemplate.queryForObject(
				sqlQuery,
				new Integer[]{pID},
				new RowMapper<Project>()
				{
					public Project mapRow(ResultSet resultSet, int i) throws SQLException
					{
						Project project = new Project();
						project.setProjectID(resultSet.getInt("id"));
						project.setDescription(resultSet.getString("description"));
						project.setTitle(resultSet.getString("name"));
						project.setCreationTime(resultSet.getTimestamp("creation_date"));
						project.setUpdateTime(resultSet.getTimestamp("update_date"));
						logger.debug("Found " + project.getProjectID() + ":" + project.getTitle() + "in DB");
						return project;
					}
				}
		);
	}

	public LinkedList<Project> loadAll()
	{
		logger.debug("Loading all projects from DB");
		String sqlQuery = "SELECT id, description, name, creation_date, update_date " +
				"FROM project";
		List<Project> list = this.jdbcTemplate.query(
				sqlQuery,
				new RowMapper<Project>()
				{
					public Project mapRow(ResultSet resultSet, int i) throws SQLException
					{
						Project project = new Project();
						project.setProjectID(resultSet.getInt("id"));
						project.setDescription(resultSet.getString("description"));
						project.setTitle(resultSet.getString("name"));
						project.setCreationTime(resultSet.getTimestamp("creation_date"));
						project.setUpdateTime(resultSet.getTimestamp("update_date"));
						return project;
					}
				}
		);
		return new LinkedList<Project>(list);
	}

	public LinkedList<Project> loadAllByUser(String uID)
	{
		logger.debug("Loading all projects of user <" + uID + "> from DB");
		String sqlQuery = "SELECT project_id, description, name, creation_date, update_date " +
				"FROM project JOIN rel_user_project ON rel_user_project.project_id = project.id " +
				"WHERE rel_user_project.user_mail = ?";
		List<Project> list = this.jdbcTemplate.query(
				sqlQuery,
				new String[]{uID},
				new RowMapper<Project>()
				{
					public Project mapRow(ResultSet resultSet, int i) throws SQLException
					{
						Project project = new Project();
						project.setProjectID(resultSet.getInt("project_id"));
						project.setDescription(resultSet.getString("description"));
						project.setTitle(resultSet.getString("name"));
						project.setCreationTime(resultSet.getTimestamp("creation_date"));
						project.setUpdateTime(resultSet.getTimestamp("update_date"));
						return project;
					}
				}
		);
		return new LinkedList<Project>(list);
	}

}
