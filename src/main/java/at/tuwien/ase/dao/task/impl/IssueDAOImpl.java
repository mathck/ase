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

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }

    public int insertIssue(final Issue issue) {

        final String sql = "INSERT INTO ISSUE (TITLE, DESCRIPTION) VALUES (?, ?)";

        logger.debug("insert into db: issue with id=" + issue.getId());

        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(sql, new String[]{"id"});
                        ps.setString(1, issue.getTitle());
                        ps.setString(2, issue.getDescription());
                        return ps;
                    }
                },
                keyHolder);

        return keyHolder.getKey().intValue();

    }

    public boolean removeIssue(int iID) {
        return false;
    }

    public Issue findByID(int issueId) {

        logger.debug("retrieve from db: issue with id=" + issueId);

        return this.jdbcTemplate.queryForObject(
                "SELECT ID, TITLE, DESCRIPTION FROM ISSUE WHERE ID = ?",
                new Object[]{issueId},
                new RowMapper<Issue>() {
                    public Issue mapRow(ResultSet rs, int taskId) throws SQLException {
                        Issue task = new Issue();
                        task.setId(Integer.valueOf(rs.getString("ID")));
                        task.setTitle(rs.getString("TITLE"));
                        task.setDescription(rs.getString("DESCRIPTION"));
                        return task;
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
        return 0;
    }
}
