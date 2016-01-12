package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

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
    private SubtaskDAO subtaskDAO;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
        taskType = new String("task");
    }

    public void insertTask(int pID, Task task) {

        logger.debug("insert into db: task with id=" + task.getId());

        String sqlQuery = "INSERT INTO TASK (ID, PROJECT_ID, TITLE, DESCRIPTION, STATUS, TASK_TYPE, CREATION_DATE, UPDATE_DATE, EXECUTION_TYPE, USER_MAIL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                task.getId(),
                pID,
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                this.taskType,
                task.getCreationDate(),
                task.getUpdateDate(),
                task.getExecutionType(),
                task.getUserMail()
        );
    }

    public void insertTaskBatch(final int pID, final List<Task> taskList, final String uuID){

        logger.debug("insert into db: task list");

        String sqlQuery = "INSERT INTO TASK (PROJECT_ID, TITLE, DESCRIPTION, STATUS, TASK_TYPE, CREATION_DATE, UPDATE_DATE, EXECUTION_TYPE, USER_MAIL, BATCH_UUID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {

                Task task = taskList.get(i);
               // ps.setInt(1, task.getId());
                ps.setInt(1, pID);
                ps.setString(2, task.getTitle());
                ps.setString(3, task.getDescription());
                ps.setString(4, task.getStatus());
                ps.setString(5, taskType);
                ps.setTimestamp(6, new java.sql.Timestamp(task.getCreationDate().getTime()));
                ps.setTimestamp(7, new java.sql.Timestamp(task.getUpdateDate().getTime()));
                ps.setString(8, task.getExecutionType());
                ps.setString(9, task.getUserMail());
                ps.setString(10, uuID);

            }

            public int getBatchSize() {
                return taskList.size();
            }
        });
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

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, " +
                "TASK_STATES.ID as states_id, TASK_STATES.STATE_NAME as states_state_name, TASK_STATES.TASK_ID as states_task_id, " +
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname, " +
                "TASK_COMMENTS.ID as comment_id, TASK_COMMENTS.TEXT as comment_text, TASK_COMMENTS.USER_MAIL as comment_mail, TASK_COMMENTS.CREATION_DATE as comment_creation_date " +
                "FROM " +
                "TASK LEFT JOIN TASK_STATES ON TASK.ID = TASK_STATES.TASK_ID " +
                "LEFT JOIN REL_USER_TASK ON TASK.ID = REL_USER_TASK.TASK_ID " +
                "LEFT JOIN TASKIT_USER ON REL_USER_TASK.USER_MAIL = TASKIT_USER.MAIL " +
                "LEFT JOIN TASK_COMMENTS ON TASK_COMMENTS.TASK_ID = TASK.ID " +
                "WHERE TASK.ID = ? AND TASK_TYPE = ? ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                taskId,
                this.taskType);

        LinkedList<Task> taskList = mapRows(rows);

        //return first task element
        if (taskList != null && taskList.size() > 0){
            return taskList.get(0);
        }

        return null;

    }

    public LinkedList<Integer> loadTaskIdsByUuID(String uuID) {

        logger.debug("retrieve from db: all task ids by uuid");

        LinkedList<Integer> idList = new LinkedList<Integer>();

        String sqlQuery = "SELECT ID " +
                "FROM TASK " +
                "WHERE BATCH_UUID = ?" +
                "ORDER BY ID ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                uuID);

        for (Map<String,Object> row : rows) {

            if (row.get("id") != null) {

                idList.add((Integer)row.get("id"));

            }
        }

        return idList;

    }

    public LinkedList<Task> loadAll() {

        logger.debug("retrieve from db: all tasks");

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, " +
                "TASK_STATES.ID as states_id, TASK_STATES.STATE_NAME as states_state_name, TASK_STATES.TASK_ID as states_task_id, " +
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname, " +
                "TASK_COMMENTS.ID as comment_id, TASK_COMMENTS.TEXT as comment_text, TASK_COMMENTS.USER_MAIL as comment_mail, TASK_COMMENTS.CREATION_DATE as comment_creation_date " +
                "FROM " +
                "TASK LEFT JOIN TASK_STATES ON TASK.ID = TASK_STATES.TASK_ID " +
                "LEFT JOIN REL_USER_TASK ON TASK.ID = REL_USER_TASK.TASK_ID " +
                "LEFT JOIN TASKIT_USER ON REL_USER_TASK.USER_MAIL = TASKIT_USER.MAIL " +
                "LEFT JOIN TASK_COMMENTS ON TASK_COMMENTS.TASK_ID = TASK.ID " +
                "WHERE TASK_TYPE = ? ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                this.taskType);

        return mapRows(rows);

    }

    public LinkedList<Task> loadAllByProject(int pID) {

        logger.debug("retrieve from db: all tasks by project with id="+pID);

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, " +
                "TASK_STATES.ID as states_id, TASK_STATES.STATE_NAME as states_state_name, TASK_STATES.TASK_ID as states_task_id, " +
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname, " +
                "TASK_COMMENTS.ID as comment_id, TASK_COMMENTS.TEXT as comment_text, TASK_COMMENTS.USER_MAIL as comment_mail, TASK_COMMENTS.CREATION_DATE as comment_creation_date " +
                "FROM " +
                "TASK LEFT JOIN TASK_STATES ON TASK.ID = TASK_STATES.TASK_ID " +
                "LEFT JOIN REL_USER_TASK ON TASK.ID = REL_USER_TASK.TASK_ID " +
                "LEFT JOIN TASKIT_USER ON REL_USER_TASK.USER_MAIL = TASKIT_USER.MAIL " +
                "LEFT JOIN TASK_COMMENTS ON TASK_COMMENTS.TASK_ID = TASK.ID " +
                "WHERE TASK_TYPE = ? AND PROJECT_ID = ?";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                this.taskType,
                pID);

        return mapRows(rows);

    }

    public LinkedList<Task> loadAllByUser(String uID) {

        logger.debug("retrieve from db: all tasks by user with id="+uID);

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, " +
                "TASK_STATES.ID as states_id, TASK_STATES.STATE_NAME as states_state_name, TASK_STATES.TASK_ID as states_task_id, " +
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname, " +
                "TASK_COMMENTS.ID as comment_id, TASK_COMMENTS.TEXT as comment_text, TASK_COMMENTS.USER_MAIL as comment_mail, TASK_COMMENTS.CREATION_DATE as comment_creation_date " +
                "FROM " +
                "(SELECT REL_USER_TASK.TASK_ID FROM REL_USER_TASK WHERE REL_USER_TASK.USER_MAIL = ?) as user_tasks, " +
                "TASK LEFT JOIN TASK_STATES ON TASK.ID = TASK_STATES.TASK_ID " +
                "LEFT JOIN REL_USER_TASK ON TASK.ID = REL_USER_TASK.TASK_ID " +
                "LEFT JOIN TASKIT_USER ON REL_USER_TASK.USER_MAIL = TASKIT_USER.MAIL " +
                "LEFT JOIN TASK_COMMENTS ON TASK_COMMENTS.TASK_ID = TASK.ID " +
                "WHERE TASK_TYPE = ? AND USER_TASKS.TASK_ID  = TASK.ID ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                uID,
                this.taskType
                );

        return mapRows(rows);
    }

    public void addUserToTask(String uID, int tID) {

        logger.debug("insert into db: add user with id="+uID+" to task with id="+tID);

        String sqlQuery = "INSERT INTO REL_USER_TASK (USER_MAIL, TASK_ID) " +
                "VALUES (?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                uID,
                tID
        );

    }

    public void addStateToTaskStates(TaskState state, int tID) {

        logger.debug("insert into db: add state ="+state.getStateName()+" to task with id="+tID);

        String sqlQuery = "INSERT INTO TASK_STATES (STATE_NAME, TASK_ID) " +
                "VALUES (?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                state.getStateName(),
                tID
        );


    }

    public void addStateToTaskStatesBatch(final List<TaskState> taskStateList, final LinkedList<Integer> taskIds) {

        logger.debug("insert into db: add states to task batch");
/*
        String sqlQuery = "INSERT INTO TASK_STATES (ID, STATE_NAME, TASK_ID) " +
                "VALUES (?, ?, ?)";*/

        String sqlQuery = "INSERT INTO TASK_STATES ( STATE_NAME, TASK_ID) " +
                "VALUES (?, ?)";

        this.jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {

            int j = 0;
            int currentTaskPosition = 0;

            public void setValues(PreparedStatement ps, int i) throws SQLException {

                //insert state for each task in taskList
                if (j >= taskStateList.size()){
                    currentTaskPosition++; //increment counter for taskList
                    j=0; //reset iterator for taskStateList
                }

                TaskState taskState = taskStateList.get(j);
                /*ps.setInt(1, getNewIDForTaskStates());
                ps.setString(2, taskState.getStateName());
                ps.setInt(3, taskList.get(currentTaskPosition).getId());*/


                ps.setString(1, taskState.getStateName());
                ps.setInt(2, taskIds.get(currentTaskPosition));

                j++;

            }

            public int getBatchSize() {
                return (taskStateList.size() * taskIds.size());
            }
        });

    }

    public LinkedList<Task> loadAllByProjectAndUser(int pID, String uID) {

        logger.debug("retrieve from db: all tasks from user with id="+uID+" and project with id="+pID);

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, " +
                "TASK_STATES.ID as states_id, TASK_STATES.STATE_NAME as states_state_name, TASK_STATES.TASK_ID as states_task_id, " +
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname, " +
                "TASK_COMMENTS.ID as comment_id, TASK_COMMENTS.TEXT as comment_text, TASK_COMMENTS.USER_MAIL as comment_mail, TASK_COMMENTS.CREATION_DATE as comment_creation_date " +
                "FROM " +
                "(SELECT REL_USER_TASK.TASK_ID FROM REL_USER_TASK WHERE REL_USER_TASK.USER_MAIL = ?) as user_tasks, " +
                "TASK LEFT JOIN TASK_STATES ON TASK.ID = TASK_STATES.TASK_ID " +
                "LEFT JOIN REL_USER_TASK ON TASK.ID = REL_USER_TASK.TASK_ID " +
                "LEFT JOIN TASKIT_USER ON REL_USER_TASK.USER_MAIL = TASKIT_USER.MAIL " +
                "LEFT JOIN TASK_COMMENTS ON TASK_COMMENTS.TASK_ID = TASK.ID " +
                "WHERE TASK_TYPE = ? AND PROJECT_ID = ? AND USER_TASKS.TASK_ID  = TASK.ID ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                uID,
                this.taskType,
                pID
        );

        return mapRows(rows);
    }

    public void assignUserToTask(int tID, String uID) {

        logger.debug("insert into db: add user with id="+uID+" to task with id="+tID);

        String sqlQuery = "INSERT INTO REL_USER_TASK (USER_MAIL, TASK_ID) " +
                "VALUES (?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                uID,
                tID
        );
    }

    public void assignUserToTaskBatch(final List<User> userList, final Integer taskId){

        logger.debug("insert into db: add user to task batch");

/*        String sqlQuery = "INSERT INTO REL_USER_TASK (ID, USER_MAIL, TASK_ID) " +
                "VALUES (?, ?, ?)";*/

        String sqlQuery = "INSERT INTO REL_USER_TASK (USER_MAIL, TASK_ID) " +
                "VALUES (?, ?)";

        this.jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {

                User user = userList.get(i);
                ps.setString(1, user.getUserID());
                ps.setInt(2,  taskId);


            }

            public int getBatchSize() {
                return userList.size();
            }

        });
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

    public void addCommentToTask(int tID, Comment comment) {

        logger.debug("insert into db: add comment to task with id=" + tID);

        String sqlQuery = "INSERT INTO TASK_COMMENTS (ID, TASK_ID, TEXT, USER_MAIL, CREATION_DATE) " +
                "VALUES (?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                comment.getId(),
                tID,
                comment.getText(),
                comment.getUser_mail(),
                new Date()
        );

    }

    public void removeCommentFromTask(int tID, int cID) {

        logger.debug("delete from db: remove comment with id="+cID+" from task with id="+tID);

        String sqlQuery = "DELETE " +
                "FROM TASK_COMMENTS " +
                "WHERE ID = ? AND TASK_ID = ?";

        this.jdbcTemplate.update(
                sqlQuery,
                cID,
                tID
        );
    }

    public int getNewIDForComments() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_comments_id')",
                Integer.class);

        return id;
    }

    private LinkedList<Task>  mapRows(List<Map<String,Object>> rows){

        int statesId;
        int commentId;
        String userId;
        int taskId;
        LinkedList<Task> tasks = new LinkedList<Task>();
        HashMap<Integer, Task> taskMap = new HashMap<Integer, Task>();
        HashMap<Integer, TaskState> statesMap = new HashMap<Integer, TaskState>();
        HashMap<Integer, Comment> commentsMap = new HashMap<Integer, Comment>();
        HashMap<String, User> usersMap = new HashMap<String, User>();

        Task task;
        TaskState taskState;
        User user;
        Comment comment;

        for (Map<String,Object> row : rows) {

            if (row.get("task_id") != null) {

                taskId = (Integer) row.get("task_id");

                //task already in linked list?
                if (taskMap.get(taskId) == null) {

                    task = new Task();

                    task.setId(taskId);
                    task.setTitle((String) row.get("task_title"));
                    task.setDescription((String) row.get("task_description"));
                    task.setTaskType((String) row.get("task_task_type"));
                    task.setCreationDate(new java.sql.Date(((Timestamp) row.get("task_creation_date")).getTime()));
                    task.setUpdateDate(new java.sql.Date(((Timestamp) row.get("task_update_date")).getTime()));
                    task.setProjectId((Integer) row.get("task_project_id"));
                    task.setUserMail((String) row.get("task_user_mail"));
                    task.setStatus((String) row.get("task_status"));
                    task.setExecutionType((String) row.get("task_execution_type"));

                    tasks.add(task);
                    taskMap.put(taskId, task);

                }

                if (row.get("states_id") != null) {
                    statesId = (Integer) row.get("states_id");
                    //state already in task object?
                    if (statesMap.get(taskId + statesId) == null) {
                        taskState = new TaskState();

                        taskState.setId((Integer) row.get("states_id"));
                        taskState.setStateName((String) row.get("states_state_name"));
                        taskState.setTaskId((Integer) row.get("states_task_id"));

                        //put taskState to task element
                        ((Task) taskMap.get(taskId)).addTaskState(taskState);
                        statesMap.put(taskId + statesId, taskState);

                    }

                }

                if (row.get("users_mail") != null) {

                    userId = (String) row.get("users_mail");
                    //user already in task object?

                    if (usersMap.get(taskId + userId.trim()) == null) {
                        user = new User();

                        user.setUserID((String) row.get("users_mail"));
                        user.setFirstName((String) row.get("users_firstname"));
                        user.setLastName((String) row.get("users_lastname"));

                        //put taskState to task element
                        ((Task) taskMap.get(taskId)).addUser(user);
                        usersMap.put(taskId + userId.trim(), user);

                    }

                }

                if (row.get("comment_id") != null) {

                    commentId = (Integer) row.get("comment_id");
                    //comment already in task object?

                    if (commentsMap.get(taskId + commentId) == null) {
                        comment = new Comment();

                        comment.setUser_mail((String) row.get("comment_mail"));
                        comment.setText((String) row.get("comment_text"));
                        comment.setCreationDate(new java.sql.Date(((Timestamp) row.get("comment_creation_date")).getTime()));

                        //put comment to task element
                        ((Task) taskMap.get(taskId)).addComment(comment);
                        commentsMap.put(taskId + commentId, comment);

                    }

                }

            }

        }

        return tasks;

    }

}
