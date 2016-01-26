package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.model.Project;

import at.tuwien.ase.model.Reward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * The PostgreSQL implementation of the Project DAO interface. Using annotations it is possible to
 * Autowire to this class.
 *
 * @author Tomislav Nikic
 * @version 1.0, 14.12.2015
 */
@Repository
public class ProjectDAOImpl implements ProjectDAO {

    private static final Logger logger = LogManager.getLogger(ProjectDAOImpl.class);

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
     * Gets a new unique ID for project.
     *
     * @return An integer project ID.
     */
    public int getNewProjectID() {
        Integer projectID = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_project_id')",
                Integer.class);
        return projectID;
    }

    /**
     * Gets a new unique ID for the user-project-role relation.
     *
     * @return An integer user-project-role ID.
     */
    public int getNewRelationID() {
        Integer relationID = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_user_project_id')",
                Integer.class);
        return relationID;
    }

    /**
     * Gets a new unique ID for the project-reward relation.
     *
     * @return An integer project-reward ID.
     */
    public int getNewRewardRelationID() {
        Integer relationID = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_project_reward_id')",
                Integer.class);
        return relationID;
    }

    /**
     * Writes a project to the database.
     *
     * @param project Project object that is to be written to the database.
     * @return The project ID generated during the writing process.
     */
    public int insertProject(final Project project) {
        int projectID = this.getNewProjectID();
        logger.info("Inserting project <" + projectID + ":" + project.getTitle() + "> into DB");
        final String sqlQuery = "INSERT INTO project (id, description, name, creation_date, update_date) " +
                "VALUES (?,?,?,?,?)";
        this.jdbcTemplate.update(
                sqlQuery,
                projectID,
                project.getDescription(),
                project.getTitle(),
                project.getCreationTimeDB(),
                project.getUpdateTimeDB()
        );
        return projectID;
    }

    /**
     * Removes a project from the database.
     *
     * @param projectID The project ID that is given when written to the database.
     */
    public void removeProject(int projectID) {
        logger.info("Removing project <" + projectID + "> from DB");
        String sqlQuery = "DELETE " +
                "FROM project " +
                "WHERE id = ?";
        this.jdbcTemplate.update(
                sqlQuery,
                projectID
        );
    }

    /**
     * Updates an existing project in the database with new values that are provided through
     * the parameter.
     *
     * @param projectID The project ID that is given when written to the database.
     * @param project The project object with the updated entries.
     */
    public void updateProject(int projectID, Project project) {
        logger.info("Updating project <" + projectID + "> on DB");
        String sqlQuery = "UPDATE project " +
                "SET " +
                "description = COALESCE (?, description), " +
                "name = COALESCE (?, name), " +
                "update_date = COALESCE (?, update_date) " +
                "WHERE id = ?";
        this.jdbcTemplate.update(
                sqlQuery,
                project.getDescription(),
                project.getTitle(),
                project.getUpdateTimeDB(),
                projectID
        );
    }

    /**
     * Adds a user (using UserRole) to the project and storing it in the relation table.
     *
     * @param userID The user email that is stored in the database.
     * @param projectID The project ID that is given when written to the database.
     * @param role The role of the user in this project.
     */
    public void addUserToProject(String userID, int projectID, String role) {
        logger.info("Inserting user <" + userID + "> into project <" + projectID + "> and storing on DB");
        String sqlQuery = "INSERT INTO rel_user_project (id, user_mail, project_id, role) " +
                "VALUES (?,?,?,?)";
        this.jdbcTemplate.update(
                sqlQuery,
                this.getNewRelationID(),
                userID,
                projectID,
                role.toString()
        );
    }

    /**
     * Removes user from a specific project.
     *
     * @param userID The user email that is stored in the database.
     * @param projectID The project ID that is given when written to the database.
     */
    public void removeUserFromProject(String userID, int projectID) {
        logger.info("Removing user <" + userID + "> from project <" + projectID + "> on DB");
        String sqlQuery = "DELETE " +
                "FROM rel_user_project " +
                "WHERE project_id = ? AND user_mail = ?";
        this.jdbcTemplate.update(
                sqlQuery,
                projectID,
                userID
        );
    }

    /**
     * Pull the project object from the database.
     *
     * @param projectID The project ID that is given when written to the database.
     * @return The requested project object.
     * @throws EmptyResultDataAccessException Only thrown, if the list is empty.
     */
    public Project findByID(int projectID) throws EmptyResultDataAccessException {
        logger.info("Searching for projects <" + projectID + "> in DB");
        String sqlQuery = "SELECT id, description, name, creation_date, update_date " +
                "FROM project " +
                "WHERE id = ?";
        return this.jdbcTemplate.queryForObject(
                sqlQuery,
                new Integer[]{projectID},
                new RowMapper<Project>() {
                    public Project mapRow(ResultSet resultSet, int i) throws SQLException {
                        Project project = new Project();
                        project.setProjectID(resultSet.getInt("id"));
                        project.setDescription(resultSet.getString("description"));
                        project.setTitle(resultSet.getString("name"));
                        project.setCreationTimeDB(resultSet.getTimestamp("creation_date"));
                        project.setUpdateTimeDB(resultSet.getTimestamp("update_date"));
                        logger.debug("Found " + project.getProjectID() + ":" + project.getTitle() + "in DB");
                        return project;
                    }
                }
        );
    }

    /**
     * Loads all projects from the database and return them as a linked list.
     *
     * @return A linked list containing project objects. These are filled with only essential data.
     * @throws EmptyResultDataAccessException Only thrown, if the list is empty.
     */
    public LinkedList<Project> loadAll() throws EmptyResultDataAccessException {
        logger.info("Loading all projects from DB");
        String sqlQuery = "SELECT id, description, name, creation_date, update_date " +
                "FROM project";
        List<Project> list = this.jdbcTemplate.query(
                sqlQuery,
                new RowMapper<Project>() {
                    public Project mapRow(ResultSet resultSet, int i) throws SQLException {
                        Project project = new Project();
                        project.setProjectID(resultSet.getInt("id"));
                        project.setDescription(resultSet.getString("description"));
                        project.setTitle(resultSet.getString("name"));
                        project.setCreationTimeDB(resultSet.getTimestamp("creation_date"));
                        project.setUpdateTimeDB(resultSet.getTimestamp("update_date"));
                        return project;
                    }
                }
        );
        return new LinkedList<Project>(list);
    }

    /**
     * Loads all projects connected to a specific user.
     *
     * @param userID The user email that is stored in the database.
     * @return A linked list containing project objects. These are filled with only essential data.
     * @throws EmptyResultDataAccessException Only thrown, if the list is empty.
     */
    public LinkedList<Project> loadAllByUser(String userID) throws EmptyResultDataAccessException {
        logger.debug("Loading all projects of user <" + userID + "> from DB");
        String sqlQuery = "SELECT project_id, description, name, creation_date, update_date " +
                "FROM project JOIN rel_user_project ON rel_user_project.project_id = project.id " +
                "WHERE rel_user_project.user_mail = ?";
        List<Project> list = this.jdbcTemplate.query(
                sqlQuery,
                new String[]{userID},
                new RowMapper<Project>() {
                    public Project mapRow(ResultSet resultSet, int i) throws SQLException {
                        Project project = new Project();
                        project.setProjectID(resultSet.getInt("project_id"));
                        project.setDescription(resultSet.getString("description"));
                        project.setTitle(resultSet.getString("name"));
                        project.setCreationTimeDB(resultSet.getTimestamp("creation_date"));
                        project.setUpdateTimeDB(resultSet.getTimestamp("update_date"));
                        return project;
                    }
                }
        );
        return new LinkedList<Project>(list);
    }

    public void insertRewardsToProjectBatch(final int pID, final LinkedList<Reward> rewardList){

        String sqlQuery = "INSERT INTO REL_PROJECT_REWARD (ID, PROJECT_ID, REWARD_ID) " +
                "VALUES (?, ?, ?)";

        this.jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {

                Reward reward = rewardList.get(i);

                ps.setInt(1, getNewRewardRelationID());
                ps.setInt(2, pID);
                ps.setInt(3, reward.getId());

            }

            public int getBatchSize() {
                return rewardList.size();
            }

        });


    }

}
