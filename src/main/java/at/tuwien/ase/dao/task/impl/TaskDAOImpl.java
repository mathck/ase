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
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Created by DanielHofer on 09.11.2015.
 */

@Repository
public class TaskDAOImpl implements TaskDAO {

    private static final Logger logger = LogManager.getLogger(TaskDAOImpl.class);

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    String taskType;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
        taskType = new String("task");
    }

    public void insertTask(Task task) {

        logger.debug("insert into db: task with id=" + task.getId());

        this.jdbcTemplate.update(
                "INSERT INTO TASK (ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE) VALUES (?, ?, ?, ?, ?, ?)",
                task.getId(), task.getTitle(), task.getDescription(), this.taskType, task.getCreationDate(), task.getUpdateDate());

    }

    public boolean removeTask(int tID) {
        // TODO

        return false;
    }

    public Task findByID(int taskId) {

        logger.debug("retrieve from db: task with id=" + taskId);

        return this.jdbcTemplate.queryForObject(
                "SELECT ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE, DSL_TEMPLATE_ID, PROJECT_ID, USER_MAIL, STATUS " +
                        "FROM TASK " +
                        "WHERE ID = ?",
                new Object[]{taskId},
                new RowMapper<Task>() {
                    public Task mapRow(ResultSet rs, int taskId) throws SQLException {
                        Task task = new Task();
                        task.setId(Integer.valueOf(rs.getString("id")));
                        task.setTitle(rs.getString("title"));
                        task.setDescription(rs.getString("description"));
                        task.setTaskType(rs.getString("task_type"));
                        task.setCreationDate(rs.getDate("creation_date"));
                        task.setUpdateDate(rs.getDate("update_date"));
                        task.setDslTemplateId(rs.getInt("dsl_template_id"));
                        task.setProjectId(rs.getInt("project_id"));
                        task.setUserMail(rs.getString("user_mail"));
                        task.setStatus(rs.getString("status"));
                        return task;
                    }
                });

    }

    public LinkedList<Task> loadAll() {
        String sql = "SELECT ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE, DSL_TEMPLATE_ID, PROJECT_ID, USER_MAIL, STATUS " +
                "FROM TASK " +
                "WHERE TASK_TYPE = ?";

        LinkedList<Task> tasks = new LinkedList<Task>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sql, this.taskType);
        for (Map<String,Object> row : rows) {

            Task task = new Task();
            task.setId((Integer)row.get("id"));
            task.setTitle((String)row.get("title"));
            task.setDescription((String)row.get("description"));
            task.setTaskType((String)row.get("task_type"));
            task.setCreationDate((Date)row.get("creation_date"));
            task.setUpdateDate((Date)row.get("update_date"));
            task.setDslTemplateId((Integer)row.get("dsl_template_id"));
            task.setProjectId((Integer)row.get("project_id"));
            task.setUserMail((String)row.get("user_mail"));
            task.setStatus((String)row.get("status"));

            tasks.add(task);
        }

        return tasks;
    }

    public LinkedList<Task> loadAllByProject(int pID)
    {
        String sql = "SELECT ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE, DSL_TEMPLATE_ID, PROJECT_ID, USER_MAIL, STATUS " +
                "FROM TASK " +
                "WHERE TASK_TYPE = ? AND PROJECT_ID = ?";

        LinkedList<Task> tasks = new LinkedList<Task>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sql, this.taskType, Integer.valueOf(pID));
        for (Map<String,Object> row : rows) {

            Task task = new Task();
            task.setId((Integer)row.get("id"));
            task.setTitle((String)row.get("title"));
            task.setDescription((String)row.get("description"));
            task.setTaskType((String)row.get("task_type"));
            task.setCreationDate((Date)row.get("creation_date"));
            task.setUpdateDate((Date)row.get("update_date"));
            task.setDslTemplateId((Integer)row.get("dsl_template_id"));
            task.setProjectId((Integer)row.get("project_id"));
            task.setUserMail((String)row.get("user_mail"));
            task.setStatus((String)row.get("status"));

            tasks.add(task);
        }

        return tasks;
    }

    public LinkedList<Task> loadAllByUser(String uID) {
        String sql = "SELECT TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.TASK_TYPE, TASK.CREATION_DATE, TASK.UPDATE_DATE, TASK.DSL_TEMPLATE_ID, TASK.PROJECT_ID, TASK.USER_MAIL, TASK.STATUS " +
                "FROM TASK, REL_USER_TASK, TASKIT_USER " +
                "WHERE TASK_TYPE = ? AND REL_USER_TASK.USER_MAIL = ? AND REL_USER_TASK.USER_MAIL = TASKIT_USER.MAIL AND REL_USER_TASK.TASK_ID = TASK.ID";


        LinkedList<Task> tasks = new LinkedList<Task>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sql, this.taskType, uID);
        for (Map<String,Object> row : rows) {

            Task task = new Task();
            task.setId((Integer)row.get("id"));
            task.setTitle((String)row.get("title"));
            task.setDescription((String)row.get("description"));
            task.setTaskType((String)row.get("task_type"));
            task.setCreationDate((Date)row.get("creation_date"));
            task.setUpdateDate((Date)row.get("update_date"));
            task.setDslTemplateId((Integer)row.get("dsl_template_id"));
            task.setProjectId((Integer)row.get("project_id"));
            task.setUserMail((String)row.get("user_mail"));
            task.setStatus((String)row.get("status"));

            tasks.add(task);
        }

        return tasks;
    }

    public void updateIssueToTask(int iID, String uID) {

       this.jdbcTemplate.update(
                "UPDATE TASK SET TASK_TYPE = ?, UPDATE_DATE = ?, USER_MAIL = ? " +
                        "WHERE ID = ?",
                this.taskType, new Date(), uID, iID);

    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_task_id')",
                 Integer.class);

        return id;
    }
}
