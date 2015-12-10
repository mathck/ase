package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.model.Subtask;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

        String sqlQuery = "INSERT INTO TASK (ID, DSL_TEMPLATE_ID, PROJECT_ID, TITLE, DESCRIPTION, STATUS, TASK_TYPE, CREATION_DATE, UPDATE_DATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                task.getId(),
                task.getDslTemplateId(),
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                this.taskType,
                task.getCreationDate(),
                task.getUpdateDate());

    }

    public void removeTaskByID(int tID) {

        logger.debug("delete from db: task with id=" + tID);

        String sqlQuery = "DELETE " +
                "FROM TASK " +
                "WHERE ID = ? AND TASK_TYPE = ? ";

        this.jdbcTemplate.update(
                sqlQuery,
                tID,
                this.taskType
        );
    }

    public Task findByID(int taskId) {

        logger.debug("retrieve from db: task with id=" + taskId);

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.DSL_TEMPLATE_ID as task_dsl_template_id, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, SUBTASK.ID as subtask_id, SUBTASK.TASK_ID as subtask_task_id, SUBTASK.TITLE as subtask_title, SUBTASK.DESCRIPTION as subtask_description, SUBTASK.STATUS as subtask_status, SUBTASK.XP as subtask_xp, SUBTASK.CREATION_DATE as subtask_creation_date, SUBTASK.UPDATE_DATE as subtask_update_date " +
                "FROM TASK, SUBTASK " +
                "WHERE TASK.ID = ? AND TASK_TYPE = ? " +
                "AND TASK.ID = SUBTASK.TASK_ID";

        Subtask subtask;
        LinkedList<Subtask> subtaskList;
        Task task = new Task();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                taskId,
                this.taskType);
        for (Map<String,Object> row : rows) {

            //set task information only in first iteration
            if (task.getId() == null){
                task.setId((Integer)row.get("task_id"));
                task.setTitle((String)row.get("task_title"));
                task.setDescription((String)row.get("task_description"));
                task.setTaskType((String)row.get("task_task_type"));
                task.setCreationDate(new java.sql.Date(((Timestamp)row.get("task_creation_date")).getTime()));
                task.setUpdateDate(new java.sql.Date(((Timestamp)row.get("task_update_date")).getTime()));
                task.setDslTemplateId((Integer)row.get("task_dsl_template_id"));
                task.setProjectId((Integer) row.get("task_project_id"));
                task.setUserMail((String)row.get("task_user_mail"));
                task.setStatus((String)row.get("task_status"));
            }

            //create new subtask
            subtask = new Subtask();

            subtask.setId((Integer)row.get("subtask_id"));
            subtask.setTitle((String)row.get("subtask_title"));
            subtask.setDescription((String)row.get("subtask_description"));
            subtask.setTaskId((Integer) row.get("subtask_task_id"));
            subtask.setStatus((String)row.get("subtask_status"));
            subtask.setXp((Integer) row.get("subtask_xp"));
            subtask.setCreationDate(new java.sql.Date(((Timestamp)row.get("subtask_creation_date")).getTime()));
            subtask.setUpdateDate(new java.sql.Date(((Timestamp)row.get("subtask_update_date")).getTime()));

            //add subtask to task
            task.addSubtask(subtask);

        }

        return task;
    }

    public LinkedList<Task> loadAll() {

        logger.debug("retrieve from db: all tasks");

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE, DSL_TEMPLATE_ID, PROJECT_ID, USER_MAIL, STATUS " +
                "FROM TASK " +
                "WHERE TASK_TYPE = ?";

        LinkedList<Task> tasks = new LinkedList<Task>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                this.taskType);

        for (Map<String,Object> row : rows) {

            Task task = new Task();
            task.setId((Integer)row.get("id"));
            task.setTitle((String)row.get("title"));
            task.setDescription((String)row.get("description"));
            task.setTaskType((String)row.get("task_type"));
            task.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            task.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));
            task.setDslTemplateId((Integer)row.get("dsl_template_id"));
            task.setProjectId((Integer)row.get("project_id"));
            task.setUserMail((String)row.get("user_mail"));
            task.setStatus((String)row.get("status"));

            tasks.add(task);
        }

        return tasks;
    }

    public LinkedList<Task> loadAllByProject(int pID) {

        logger.debug("retrieve from db: all tasks by project with id="+pID);

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE, DSL_TEMPLATE_ID, PROJECT_ID, USER_MAIL, STATUS " +
                "FROM TASK " +
                "WHERE TASK_TYPE = ? " +
                    "AND PROJECT_ID = ?";

        LinkedList<Task> tasks = new LinkedList<Task>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                this.taskType,
                Integer.valueOf(pID));

        for (Map<String,Object> row : rows) {

            Task task = new Task();
            task.setId((Integer)row.get("id"));
            task.setTitle((String)row.get("title"));
            task.setDescription((String)row.get("description"));
            task.setTaskType((String)row.get("task_type"));
            task.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            task.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));
            task.setDslTemplateId((Integer)row.get("dsl_template_id"));
            task.setProjectId((Integer)row.get("project_id"));
            task.setUserMail((String)row.get("user_mail"));
            task.setStatus((String)row.get("status"));

            tasks.add(task);
        }

        return tasks;
    }

    public LinkedList<Task> loadAllByUser(String uID) {

        logger.debug("retrieve from db: all tasks by user with id="+uID);

        String sqlQuery = "SELECT TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.TASK_TYPE, TASK.CREATION_DATE, TASK.UPDATE_DATE, TASK.DSL_TEMPLATE_ID, TASK.PROJECT_ID, TASK.USER_MAIL, TASK.STATUS " +
                "FROM TASK, REL_USER_TASK, TASKIT_USER " +
                "WHERE TASK_TYPE = ? " +
                    "AND REL_USER_TASK.USER_MAIL = ? " +
                    "AND REL_USER_TASK.USER_MAIL = TASKIT_USER.MAIL " +
                    "AND REL_USER_TASK.TASK_ID = TASK.ID";


        LinkedList<Task> tasks = new LinkedList<Task>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                this.taskType,
                uID);

        for (Map<String,Object> row : rows) {

            Task task = new Task();
            task.setId((Integer)row.get("id"));
            task.setTitle((String)row.get("title"));
            task.setDescription((String)row.get("description"));
            task.setTaskType((String)row.get("task_type"));
            task.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            task.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));
            task.setDslTemplateId((Integer)row.get("dsl_template_id"));
            task.setProjectId((Integer)row.get("project_id"));
            task.setUserMail((String)row.get("user_mail"));
            task.setStatus((String)row.get("status"));

            tasks.add(task);
        }

        return tasks;
    }

    public void updateIssueToTask(int iID) {

        logger.debug("update issue with id="+iID+" to task");

        String sqlQuery = "UPDATE TASK " +
                "SET TASK_TYPE = ?, UPDATE_DATE = ?, STATUS = ? " +
                "WHERE ID = ?";

                this.jdbcTemplate.update(
                        sqlQuery,
                        this.taskType,
                        new Date(),
                        new String("open"),
                        iID);

    }

    public void addUserToTask(String uID, int tID) {

        logger.debug("insert into db: add user with id="+uID+" to task with id="+tID);

        String sqlQuery = "INSERT INTO REL_USER_TASK (ID, USER_MAIL, TASK_ID) " +
                "VALUES (?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                this.getNewIDForRelTaskUser(),
                uID,
                tID
        );

    }

    public LinkedList<Task> loadAllByProjectAndUser(int pID, String uID) {

        logger.debug("retrieve from db: all tasks from user with id="+uID+" and project with id="+pID);

        String sqlQuery = "SELECT TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.TASK_TYPE, TASK.CREATION_DATE, TASK.UPDATE_DATE, TASK.DSL_TEMPLATE_ID, TASK.PROJECT_ID, TASK.USER_MAIL, TASK.STATUS, TASKIT_USER.FIRSTNAME, TASKIT_USER.LASTNAME, TASKIT_USER.MAIL, TASKIT_USER.AVATAR_URL " +
                "FROM TASK, TASKIT_USER " +
                "WHERE TASK_TYPE = ? " +
                "AND TASK.USER_MAIL = ? " +
                "AND TASK.PROJECT_ID = ? " +
                "AND TASK.USER_MAIL = TASKIT_USER.MAIL";

        LinkedList<Task> tasks = new LinkedList<Task>();
        User user;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                this.taskType,
                uID,
                pID);

        for (Map<String,Object> row : rows) {

            Task task = new Task();
            task.setId((Integer)row.get("id"));
            task.setTitle((String)row.get("title"));
            task.setDescription((String)row.get("description"));
            task.setTaskType((String)row.get("task_type"));
            task.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            task.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));
            task.setDslTemplateId((Integer)row.get("dsl_template_id"));
            task.setProjectId((Integer) row.get("project_id"));
            task.setUserMail((String)row.get("user_mail"));
            task.setStatus((String)row.get("status"));

            //create user
            user = new User();
            user.setFirstName((String)row.get("firstname"));
            user.setLastName((String)row.get("lastname"));
            user.setUserID((String)row.get("mail"));
            user.setAvatar((String)row.get("avatar_url"));

            //add user to issue
            task.setUser(user);

            tasks.add(task);
        }

        return tasks;
    }

    public void assignUserToTask(int tID, String uID) {

        logger.debug("insert into db: add user with id="+uID+" to task with id="+tID);

        String sqlQuery = "INSERT INTO REL_USER_TASK (ID, USER_MAIL, TASK_ID) " +
                "VALUES (?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                this.getNewIDForRelTaskUser(),
                uID,
                tID
        );
    }

    public void removeUserFromTask(int tID, String uID) {

        logger.debug("delete from db: remove user with id="+uID+" from task with id="+tID);

        String sqlQuery = "DELETE " +
                "FROM REL_USER_TASK " +
                "WHERE USER_MAIL = ? AND TASK_ID = ?";

        this.jdbcTemplate.update(
                sqlQuery,
                uID,
                tID
        );

    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_task_id')",
                Integer.class);

        return id;
    }

    public int getNewIDForRelTaskUser() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_user_task_id')",
                Integer.class);

        return id;
    }

}
