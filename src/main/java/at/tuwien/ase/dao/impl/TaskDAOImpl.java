package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    public void insertTask(int pID, Task task) throws DataAccessException {

        logger.debug("insert into db: task with id=" + task.getId());

        String sqlQuery = "INSERT INTO TASK (ID, PROJECT_ID, TITLE, DESCRIPTION, STATUS, TASK_TYPE, CREATION_DATE, UPDATE_DATE, EXECUTION_TYPE, USER_MAIL, COMMENTS_ALLOWED) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                task.getUserMail().trim(),
                task.isCommentsAllowed()
        );
    }

    public void insertTaskBatch(final int pID, final List<Task> taskList, final String uuID) throws DataAccessException{

        logger.debug("insert into db: task list");

        String sqlQuery = "INSERT INTO TASK (PROJECT_ID, TITLE, DESCRIPTION, STATUS, TASK_TYPE, CREATION_DATE, UPDATE_DATE, EXECUTION_TYPE, USER_MAIL, BATCH_UUID, COMMENTS_ALLOWED) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                ps.setString(9, task.getUserMail().trim());
                ps.setString(10, uuID);
                ps.setBoolean(11, task.isCommentsAllowed());

            }

            public int getBatchSize() {
                return taskList.size();
            }
        });
    }

    public void removeTaskByID(int tID) throws DataAccessException {

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

    public Task findByID(int taskId) throws DataAccessException {

        logger.debug("retrieve from db: task with id=" + taskId);

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, TASK.COMMENTS_ALLOWED as task_comments_allowed, " +
                "TASK_STATES.ID as states_id, TASK_STATES.STATE_NAME as states_state_name, TASK_STATES.TASK_ID as states_task_id, " +
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname, TASKIT_USER.AVATAR_URL as users_avatar_url, " +
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

    public LinkedList<Integer> loadTaskIdsByUuID(String uuID) throws DataAccessException {

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

    public LinkedList<Task> loadAll() throws DataAccessException {

        logger.debug("retrieve from db: all tasks");

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, TASK.COMMENTS_ALLOWED as task_comments_allowed, " +
                "TASK_STATES.ID as states_id, TASK_STATES.STATE_NAME as states_state_name, TASK_STATES.TASK_ID as states_task_id, " +
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname, TASKIT_USER.AVATAR_URL as users_avatar_url, " +
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

    public LinkedList<Task> loadAllByProject(int pID)  throws DataAccessException{

        logger.debug("retrieve from db: all tasks by project with id="+pID);

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, TASK.COMMENTS_ALLOWED as task_comments_allowed, " +
                "TASK_STATES.ID as states_id, TASK_STATES.STATE_NAME as states_state_name, TASK_STATES.TASK_ID as states_task_id, " +
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname, TASKIT_USER.AVATAR_URL as users_avatar_url, " +
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

    public LinkedList<Task> loadAllByUser(String uID)  throws DataAccessException{

        logger.debug("retrieve from db: all tasks by user with id="+uID);

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, TASK.COMMENTS_ALLOWED as task_comments_allowed, " +
                "TASK_STATES.ID as states_id, TASK_STATES.STATE_NAME as states_state_name, TASK_STATES.TASK_ID as states_task_id, " +
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname, TASKIT_USER.AVATAR_URL as users_avatar_url, " +
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
                uID.trim(),
                this.taskType
                );

        return mapRows(rows);
    }

    public LinkedList<Comment> loadAllCommentsByTask(int tID) throws DataAccessException{

        logger.debug("retrieve from db: all comments from tasks with id="+tID);


        String sqlQuery = "SELECT TASK_COMMENTS.ID as comment_id, TASK_COMMENTS.TEXT as comment_text, TASK_COMMENTS.CREATION_DATE as comment_creation_date, " +
                "TASKIT_USER.MAIL as comment_users_mail, TASKIT_USER.FIRSTNAME as comment_users_firstname, TASKIT_USER.LASTNAME as comment_users_lastname, TASKIT_USER.AVATAR_URL as comment_users_avatar_url " +
                "FROM " +
                "TASK_COMMENTS LEFT JOIN TASKIT_USER ON TASK_COMMENTS.USER_MAIL = TASKIT_USER.MAIL " +
                "WHERE TASK_COMMENTS.TASK_ID = ? ";


        LinkedList<Comment> commentList = new LinkedList<Comment>();
        Comment comment;
        User user;
        int commentId;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                tID
        );

        for (Map<String,Object> row : rows) {
            if (row.get("comment_id") != null) {

                commentId = (Integer) row.get("comment_id");

                comment = new Comment();

                comment.setId(commentId);
                comment.setText((String) row.get("comment_text"));
                comment.setCreationDate(((Timestamp) row.get("comment_creation_date")).toString());

                user = new User();

                user.setUserID((String) row.get("comment_users_mail"));
                user.setFirstName((String) row.get("comment_users_firstname"));
                user.setLastName((String) row.get("comment_users_lastname"));;
                user.setAvatar((String) row.get("comment_users_avatar_url"));

                //add user to comment
                comment.setUser(user);

                //add comment to commentList
                commentList.add(comment);

            }
        }

        return commentList;

    }

    public LinkedList<User> loadAllUsersByTask(int tID) throws DataAccessException{

        logger.debug("retrieve from db: all users from tasks with id="+tID);

        String sqlQuery = "SELECT USER_MAIL " +
                "FROM REL_USER_TASK " +
                "WHERE TASK_ID = ? ";

        LinkedList<User> userList = new LinkedList<User>();
        User user;
        String userId;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                tID
        );

        for (Map<String,Object> row : rows) {

            if (row.get("user_mail") != null) {

                userId = (String) row.get("user_mail");

                user = new User();

                user.setUserID(userId.trim());

                //add user to userList
                userList.add(user);

            }

        }

        return userList;
    }

    public void addUserToTask(String uID, int tID)  throws DataAccessException{

        logger.debug("insert into db: add user with id="+uID+" to task with id="+tID);

        String sqlQuery = "INSERT INTO REL_USER_TASK (USER_MAIL, TASK_ID) " +
                "VALUES (?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                uID.trim(),
                tID
        );

    }

    public void addStateToTaskStates(TaskState state, int tID) throws DataAccessException{

        logger.debug("insert into db: add state ="+state.getStateName()+" to task with id="+tID);

        String sqlQuery = "INSERT INTO TASK_STATES (STATE_NAME, TASK_ID) " +
                "VALUES (?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                state.getStateName(),
                tID
        );
    }

    public void addStateToTaskStatesBatch(final List<TaskState> taskStateList, final LinkedList<Integer> taskIds) throws DataAccessException {

        logger.debug("insert into db: add states to task batch");

        String sqlQuery = "INSERT INTO TASK_STATES ( STATE_NAME, TASK_ID) " +
                "VALUES (?, ?)";

        this.jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {

            int j = 0;
            int currentTaskPosition = 0;

            public void setValues(PreparedStatement ps, int i) throws SQLException {

                //insert state for each task in taskList (inner loop)
                if (j >= taskStateList.size()){
                    currentTaskPosition++; //increment counter for taskList
                    j=0; //reset iterator for taskStateList
                }

                TaskState taskState = taskStateList.get(j);
                ps.setString(1, taskState.getStateName());
                ps.setInt(2, taskIds.get(currentTaskPosition));

                j++;

            }

            public int getBatchSize() {
                return (taskStateList.size() * taskIds.size());
            }
        });

    }

    public LinkedList<Task> loadAllByProjectAndUser(int pID, String uID)  throws DataAccessException{

        logger.debug("retrieve from db: all tasks from user with id="+uID+" and project with id="+pID);

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, TASK.COMMENTS_ALLOWED as task_comments_allowed, " +
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
                uID.trim(),
                this.taskType,
                pID
        );

        return mapRows(rows);
    }

    public void assignUserToTask(int tID, String uID)  throws DataAccessException{

        logger.debug("insert into db: add user with id="+uID+" to task with id="+tID);

        String sqlQuery = "INSERT INTO REL_USER_TASK (USER_MAIL, TASK_ID) " +
                "VALUES (?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                uID.trim(),
                tID
        );
    }

    public void assignUserToTaskBatch(final List<User> userList, final Integer taskId) throws DataAccessException{

        logger.debug("insert into db: add user to task batch");

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

    public void removeUserFromTask(int tID, String uID)  throws DataAccessException{

        logger.debug("delete from db: remove user with id="+uID+" from task with id="+tID);

        String sqlQuery = "DELETE " +
                "FROM REL_USER_TASK " +
                "WHERE USER_MAIL = ? AND TASK_ID = ?";

        this.jdbcTemplate.update(
                sqlQuery,
                uID.trim(),
                tID
        );

    }

    public void addCommentToTask(int tID, Comment comment)  throws DataAccessException{

        logger.debug("insert into db: add comment to task with id=" + tID);

        String sqlQuery = "INSERT INTO TASK_COMMENTS (ID, TASK_ID, TEXT, USER_MAIL, CREATION_DATE) " +
                "VALUES (?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                comment.getId(),
                tID,
                comment.getText(),
                comment.getUser_mail().trim(),
                new Date()
        );

    }

    public void removeCommentFromTask(int tID, int cID)  throws DataAccessException{

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

    public int getNewIDForComments()  throws DataAccessException{

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
        String combinedId;
        LinkedList<Task> tasks = new LinkedList<Task>();
        HashMap<Integer, Task> taskMap = new HashMap<Integer, Task>();
        HashMap<String, TaskState> statesMap = new HashMap<String, TaskState>();
        HashMap<String, Comment> commentsMap = new HashMap<String, Comment>();
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
                    task.setCommentsAllowed((Boolean) row.get("task_comments_allowed"));

                    tasks.add(task);
                    taskMap.put(taskId, task);

                }

                if (row.get("states_id") != null) {
                    statesId = (Integer) row.get("states_id");

                    combinedId = String.valueOf(taskId)+"_"+String.valueOf(statesId);

                    //state already in task object?
                    if (statesMap.get(combinedId) == null) {
                        taskState = new TaskState();

                        taskState.setId((Integer) row.get("states_id"));
                        taskState.setStateName((String) row.get("states_state_name"));
                        taskState.setTaskId((Integer) row.get("states_task_id"));

                        //put taskState to task element
                        ((Task) taskMap.get(taskId)).addTaskState(taskState);
                        statesMap.put(combinedId, taskState);

                    }

                }

                if (row.get("users_mail") != null) {

                    userId = (String) row.get("users_mail");

                    combinedId = String.valueOf(taskId)+"_"+String.valueOf(userId.trim());

                    //user already in task object?
                    if (usersMap.get(combinedId) == null) {
                        user = new User();

                        user.setUserID((String) row.get("users_mail"));
                        user.setFirstName((String) row.get("users_firstname"));
                        user.setLastName((String) row.get("users_lastname"));
                        user.setAvatar((String) row.get("users_avatar_url"));

                        //put taskState to task element
                        ((Task) taskMap.get(taskId)).addUser(user);
                        usersMap.put(combinedId, user);

                    }

                }

                if (row.get("comment_id") != null) {

                    commentId = (Integer) row.get("comment_id");

                    combinedId = String.valueOf(taskId)+"_"+String.valueOf(commentId);

                    //comment already in task object?
                    if (commentsMap.get(combinedId) == null) {
                        comment = new Comment();

                        comment.setId(commentId);
                        comment.setUser_mail((String) row.get("comment_mail"));
                        comment.setText((String) row.get("comment_text"));
                        comment.setCreationDate(((Timestamp) row.get("comment_creation_date")).toString());

                        //put comment to task element
                        ((Task) taskMap.get(taskId)).addComment(comment);
                        commentsMap.put(combinedId, comment);

                    }

                }

            }

        }

        return tasks;

    }

}
