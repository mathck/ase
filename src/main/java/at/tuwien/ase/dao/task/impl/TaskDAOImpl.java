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
import java.util.Date;
import java.util.LinkedList;

import javax.sql.DataSource;

/**
 * Created by DanielHofer on 09.11.2015.
 */

@Repository
public class TaskDAOImpl implements TaskDAO {

    private static final Logger logger = LogManager.getLogger(TaskDAOImpl.class);

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    String taskType = new String("task");

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }

    public void insertTask(Task task) {

        logger.debug("insert into db: task with id=" + task.getId());

        this.jdbcTemplate.update(
                "INSERT INTO TASK (ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE) VALUES (?, ?, ?, ?, ?, ?)",
                task.getId(), task.getTitle(), task.getDescription(), this.taskType, new Date(), new Date());

    }

    public boolean removeTask(int tID) {
        return false;
    }

    public Task findByID(int taskId) {

        logger.debug("retrieve from db: task with id=" + taskId);

        return this.jdbcTemplate.queryForObject(
                "SELECT ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE, DSL_TEMPLATE_ID, PROJECT_ID, USER_MAIL, STATUS FROM TASK WHERE ID = ?",
                new Object[]{taskId},
                new RowMapper<Task>() {
                    public Task mapRow(ResultSet rs, int taskId) throws SQLException {
                        Task task = new Task();
                        task.setId(Integer.valueOf(rs.getString("ID")));
                        task.setTitle(rs.getString("TITLE"));
                        task.setDescription(rs.getString("DESCRIPTION"));
                        task.setTaskType(rs.getString("TASK_TYPE"));
                        task.setCreationDate(rs.getDate("CREATION_DATE"));
                        task.setUpdateDate(rs.getDate("UPDATE_DATE"));
                        task.setDslTemplateId(rs.getInt("DSL_TEMPLATE_ID"));
                        task.setProjectId(rs.getInt("PROJECT_ID"));
                        task.setUserMail(rs.getString("USER_MAIL"));
                        task.setStatus(rs.getString("STATUS"));
                        return task;
                    }
                });

    }

    public LinkedList<Task> loadAll() {
        return null;
    }

    public LinkedList<Task> loadAllByProject(String pID) {
       return null;
    }

    public LinkedList<Task> loadAllByUser(String uID) {
        return null;
    }

    public void updateIssueToTask(int iID) {

       this.jdbcTemplate.update(
                "UPDATE TASK SET TASK_TYPE = ?, UPDATE_DATE = ? WHERE ID = ?",
                this.taskType, new Date(), iID);

    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_task_id')",
                 Integer.class);

        return id;
    }
}
