package at.tuwien.ase.dao.task.impl;

import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.model.task.Task;
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

import javax.sql.DataSource;

/**
 * Created by DanielHofer on 09.11.2015.
 */

@Repository
public class TaskDAOImpl implements TaskDAO {

    private static final Logger logger = LogManager.getLogger(TaskDAOImpl.class);
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }

    public int insertTask(final Task task) {

        final String sql = "INSERT INTO TASK (TITLE, DESCRIPTION) VALUES (?, ?)";

        logger.debug("insert into db: task with id=" + task.getId());

        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(sql, new String[]{"id"});
                        ps.setString(1, task.getTitle());
                        ps.setString(2, task.getDescription());
                        return ps;
                    }
                },
                keyHolder);

        return keyHolder.getKey().intValue();

    }

    public Task findByTaskId(int taskId) {

        logger.debug("retrieve from db: task with id=" + taskId);

        Task task = this.jdbcTemplate.queryForObject(
                "SELECT ID, TITLE, DESCRIPTION FROM TASK WHERE ID = ?",
                new Object[]{taskId},
                new RowMapper<Task>() {
                    public Task mapRow(ResultSet rs, int taskId) throws SQLException {
                        Task task = new Task();
                        task.setId(Integer.valueOf(rs.getString("ID")));
                        task.setTitle(rs.getString("TITLE"));
                        task.setDescription(rs.getString("DESCRIPTION"));
                        return task;
                    }
                });

        return task;

    }
}
