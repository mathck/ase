package at.tuwien.ase.dao.task.impl;

import at.tuwien.ase.dao.task.IssueDAO;
import at.tuwien.ase.model.task.Issue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;

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

        this.jdbcTemplate.update(
                "INSERT INTO TASK (ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE) VALUES (?, ?, ?, ?, ?, ?)",
                issue.getId(), issue.getTitle(), issue.getDescription(), this.taskType, new Date(), new Date());

    }

    public boolean removeIssue(int iID) {
        return false;
    }

    public Issue findByID(int issueId) {

        logger.debug("retrieve from db: issue with id=" + issueId);

        return this.jdbcTemplate.queryForObject(
                "SELECT ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE, DSL_TEMPLATE_ID, PROJECT_ID, USER_MAIL, STATUS FROM TASK WHERE ID = ?",
                new Object[]{issueId},
                new RowMapper<Issue>() {
                    public Issue mapRow(ResultSet rs, int taskId) throws SQLException {
                        Issue issue = new Issue();
                        issue.setId(Integer.valueOf(rs.getString("ID")));
                        issue.setTitle(rs.getString("TITLE"));
                        issue.setDescription(rs.getString("DESCRIPTION"));
                        issue.setTaskType(rs.getString("TASK_TYPE"));
                        issue.setCreationDate(rs.getDate("CREATION_DATE"));
                        issue.setUpdateDate(rs.getDate("UPDATE_DATE"));
                        issue.setDslTemplateId(rs.getInt("DSL_TEMPLATE_ID"));
                        issue.setProjectId(rs.getInt("PROJECT_ID"));
                        issue.setUserMail(rs.getString("USER_MAIL"));
                        issue.setStatus(rs.getString("STATUS"));
                        return issue;
                    }
                });

    }

    public LinkedList<Issue> loadAll() {
        return null;
    }

    public LinkedList<Issue> loadAllByProject(String pID) {
        return null;
    }

    public LinkedList<Issue> loadAllByUser(String uID) {
        return null;
    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_task_id')",
                Integer.class);

        return id;
    }
}
