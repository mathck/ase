package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.IssueDAO;
import at.tuwien.ase.model.Issue;
import at.tuwien.ase.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Created by Daniel Hofer on 14.11.2015.
 */

@Repository
public class IssueDAOImpl implements IssueDAO {

    private static final Logger logger = LogManager.getLogger(TaskDAOImpl.class);

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;
    String taskType = new String("issue");

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }

    public void insertIssue(Issue issue) {

        logger.debug("insert into db: issue with id=" + issue.getId());

        String sqlQuery = "INSERT INTO TASK (ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE, PROJECT_ID, USER_MAIL)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                this.jdbcTemplate.update(
                        sqlQuery,
                issue.getId(), issue.getTitle(), issue.getDescription(), this.taskType, issue.getCreationDate(), issue.getUpdateDate(), issue.getProjectId(), issue.getUserId());

    }

    public void removeIssueByID(int iID) {
        logger.debug("delete from db: issue with id=" + iID);
        String sqlQuery = "DELETE " +
                "FROM task " +
                "WHERE id = ? AND TASK_TYPE = ? ";
        this.jdbcTemplate.update(
                sqlQuery,
                iID,
                this.taskType
        );
    }

    public Issue findByID(int issueId) {

        logger.debug("retrieve from db: issue with id=" + issueId);

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE, DSL_TEMPLATE_ID, PROJECT_ID, USER_MAIL, STATUS " +
                "FROM TASK " +
                "WHERE ID = ? AND TASK_TYPE = ?";

        return this.jdbcTemplate.queryForObject(
                sqlQuery,
                new Object[]{issueId, this.taskType},
                new RowMapper<Issue>() {
                    public Issue mapRow(ResultSet rs, int taskId) throws SQLException {
                        Issue issue = new Issue();
                        issue.setId(Integer.valueOf(rs.getString("id")));
                        issue.setTitle(rs.getString("title"));
                        issue.setDescription(rs.getString("description"));
                        issue.setCreationDate(rs.getDate("creation_date"));
                        issue.setUpdateDate(rs.getDate("update_date"));
                        issue.setProjectId(rs.getInt("project_id"));
                        issue.setUserId(rs.getString("user_mail"));
                        return issue;
                    }
                });

    }

    public LinkedList<Issue> loadAll() {

        logger.debug("retrieve from db: all issues");

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, CREATION_DATE, UPDATE_DATE, PROJECT_ID, USER_MAIL " +
                "FROM TASK " +
                "WHERE TASK_TYPE = ?";

        LinkedList<Issue> issues = new LinkedList<Issue>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sqlQuery, this.taskType);
        for (Map<String,Object> row : rows) {

            Issue issue = new Issue();
            issue.setId((Integer)row.get("id"));
            issue.setTitle((String)row.get("title"));
            issue.setDescription((String)row.get("description"));
            issue.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            issue.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));
            issue.setProjectId((Integer)row.get("project_id"));
            issue.setUserId((String)row.get("user_mail"));

            issues.add(issue);
        }

        return issues;

    }

    public LinkedList<Issue> loadAllByProject(int pID) {

        logger.debug("retrieve from db: all issues by project with id="+pID);

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, CREATION_DATE, UPDATE_DATE, PROJECT_ID, USER_MAIL " +
                "FROM TASK " +
                "WHERE PROJECT_ID = ? AND TASK_TYPE = ?";

        LinkedList<Issue> issues = new LinkedList<Issue>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sqlQuery, pID, this.taskType);
        for (Map<String,Object> row : rows) {

            Issue issue = new Issue();
            issue.setId((Integer)row.get("id"));
            issue.setTitle((String)row.get("title"));
            issue.setDescription((String)row.get("description"));
            issue.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            issue.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));
            issue.setProjectId((Integer)row.get("project_id"));
            issue.setUserId((String)row.get("user_mail"));

            issues.add(issue);
        }

        return issues;

    }

    public LinkedList<Issue> loadAllByUser(String uID) {
        logger.debug("retrieve from db: all issues by user with id="+uID);

        String sqlQuery = "SELECT TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.CREATION_DATE, TASK.UPDATE_DATE, TASK.PROJECT_ID, TASK.USER_MAIL, TASKIT_USER.FIRSTNAME, TASKIT_USER.LASTNAME, TASKIT_USER.MAIL, TASKIT_USER.AVATAR_URL " +
                "FROM TASK, TASKIT_USER " +
                "WHERE TASK_TYPE = ? " +
                "AND TASK.USER_MAIL = ? " +
                "AND TASK.USER_MAIL = TASKIT_USER.MAIL";

        LinkedList<Issue> issues = new LinkedList<Issue>();
        User user;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sqlQuery, this.taskType, uID);
        for (Map<String,Object> row : rows) {

            Issue issue = new Issue();
            issue.setId((Integer)row.get("id"));
            issue.setTitle((String)row.get("title"));
            issue.setDescription((String)row.get("description"));
            issue.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            issue.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));
            issue.setProjectId((Integer)row.get("project_id"));
            issue.setUserId((String)row.get("user_mail"));

            //create user
            user = new User();
            user.setFirstName((String)row.get("firstname"));
            user.setLastName((String)row.get("lastname"));
            user.setUserID((String)row.get("mail"));
            user.setAvatar((String)row.get("avatar_url"));

            //add user to issue
            issue.setUser(user);

            issues.add(issue);
        }

        return issues;
    }

    public LinkedList<Issue> loadAllByProjectAndUser(int pID, String uID) {
        logger.debug("retrieve from db: all issues from user with id="+uID+" and project with id="+pID);

        String sqlQuery = "SELECT TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.CREATION_DATE, TASK.UPDATE_DATE, TASK.PROJECT_ID, TASK.USER_MAIL, TASKIT_USER.FIRSTNAME, TASKIT_USER.LASTNAME, TASKIT_USER.MAIL, TASKIT_USER.AVATAR_URL " +
                "FROM TASK, TASKIT_USER " +
                "WHERE TASK_TYPE = ? " +
                "AND TASK.USER_MAIL = ? " +
                "AND TASK.PROJECT_ID = ? " +
                "AND TASK.USER_MAIL = TASKIT_USER.MAIL";

        LinkedList<Issue> issues = new LinkedList<Issue>();
        User user;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sqlQuery, this.taskType, uID, pID);
        for (Map<String,Object> row : rows) {

            Issue issue = new Issue();
            issue.setId((Integer)row.get("id"));
            issue.setTitle((String)row.get("title"));
            issue.setDescription((String)row.get("description"));
            issue.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            issue.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));
            issue.setProjectId((Integer)row.get("project_id"));
            issue.setUserId((String)row.get("user_mail"));

            //create user
            user = new User();
            user.setFirstName((String)row.get("firstname"));
            user.setLastName((String)row.get("lastname"));
            user.setUserID((String)row.get("mail"));
            user.setAvatar((String)row.get("avatar_url"));

            //add user to issue
            issue.setUser(user);

            issues.add(issue);
        }

        return issues;
    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_task_id')",
                Integer.class);

        return id;
    }
}
