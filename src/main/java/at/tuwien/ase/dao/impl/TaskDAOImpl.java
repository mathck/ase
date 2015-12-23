package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.model.Subtask;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.model.TaskState;
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
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
        taskType = new String("task");
    }

    public void insertTask(Task task) {

        logger.debug("insert into db: task with id=" + task.getId());

        String sqlQuery = "INSERT INTO TASK (ID, PROJECT_ID, TITLE, DESCRIPTION, STATUS, TASK_TYPE, CREATION_DATE, UPDATE_DATE, EXECUTION_TYPE, USER_MAIL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                task.getId(),
                task.getProjectId(),
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
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname " +
                "FROM " +
                "TASK LEFT JOIN TASK_STATES ON TASK.ID = TASK_STATES.TASK_ID " +
                "LEFT JOIN REL_USER_TASK ON TASK.ID = REL_USER_TASK.TASK_ID " +
                "LEFT JOIN TASKIT_USER ON REL_USER_TASK.USER_MAIL = TASKIT_USER.MAIL " +
                "WHERE TASK.ID = ? AND TASK_TYPE = ? ";


        int statesId;
        String userId;
        ArrayList<Integer> statesList = new ArrayList<Integer>();
        ArrayList<String>  usersList = new ArrayList<String> ();
        Task task = new Task();
        TaskState taskState;
        User user;

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
                task.setProjectId((Integer) row.get("task_project_id"));
                task.setUserMail((String)row.get("task_user_mail"));
                task.setStatus((String)row.get("task_status"));
                task.setExecutionType((String) row.get("task_execution_type"));
            }

            statesId = (Integer)row.get("states_id");
            //state already in task object?
            if (!statesList.contains(statesId)){
                taskState = new TaskState();

                taskState.setId((Integer)row.get("states_id"));
                taskState.setStateName((String)row.get("states_state_name"));
                taskState.setTaskId((Integer)row.get("states_task_id"));

                task.addTaskState(taskState);
                statesList.add(statesId);
            }

            userId = (String)row.get("users_mail");
            //user already in task object?

            if (!usersList.contains(userId)){
                user = new User();

                user.setUserID((String)row.get("users_mail"));
                user.setFirstName((String)row.get("users_firstname"));
                user.setLastName((String)row.get("users_lastname"));

                task.addUser(user);
                usersList.add(userId);
            }


        }

        return task;
    }

    public LinkedList<Task> loadAll() {

        logger.debug("retrieve from db: all tasks");

        String sqlQuery = "SELECT TASK.ID as task_id, TASK.TITLE as task_title, TASK.DESCRIPTION as task_description, TASK.TASK_TYPE as task_task_type, TASK.CREATION_DATE as task_creation_date, TASK.UPDATE_DATE as task_update_date, TASK.PROJECT_ID as task_project_id, TASK.USER_MAIL as task_user_mail, TASK.STATUS as task_status, TASK.EXECUTION_TYPE as task_execution_type, " +
                "TASK_STATES.ID as states_id, TASK_STATES.STATE_NAME as states_state_name, TASK_STATES.TASK_ID as states_task_id, " +
                "TASKIT_USER.MAIL as users_mail, TASKIT_USER.FIRSTNAME as users_firstname, TASKIT_USER.LASTNAME as users_lastname " +
                "FROM " +
                "TASK LEFT JOIN TASK_STATES ON TASK.ID = TASK_STATES.TASK_ID " +
                "LEFT JOIN REL_USER_TASK ON TASK.ID = REL_USER_TASK.TASK_ID " +
                "LEFT JOIN TASKIT_USER ON REL_USER_TASK.USER_MAIL = TASKIT_USER.MAIL " +
                "WHERE TASK_TYPE = ? ";

        int statesId;
        String userId;
        int taskId;
        LinkedList<Task> tasks = new LinkedList<Task>();
        HashMap<Integer, Task> taskMap = new HashMap<Integer, Task>();
        HashMap<Integer, TaskState> statesMap = new HashMap<Integer, TaskState>();
        HashMap<String, User> usersMap = new HashMap<String, User>();

        Task task;
        TaskState taskState;
        User user;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                this.taskType);
        for (Map<String,Object> row : rows) {

            if (row.get("task_id") != null){

                taskId = (Integer)row.get("task_id");

                //task already in linked list?
                if (taskMap.get(taskId) == null){

                    task = new Task();

                    task.setId(taskId);
                    task.setTitle((String)row.get("task_title"));
                    task.setDescription((String)row.get("task_description"));
                    task.setTaskType((String)row.get("task_task_type"));
                    task.setCreationDate(new java.sql.Date(((Timestamp)row.get("task_creation_date")).getTime()));
                    task.setUpdateDate(new java.sql.Date(((Timestamp)row.get("task_update_date")).getTime()));
                    task.setProjectId((Integer) row.get("task_project_id"));
                    task.setUserMail((String)row.get("task_user_mail"));
                    task.setStatus((String)row.get("task_status"));
                    task.setExecutionType((String) row.get("task_execution_type"));

                    tasks.add(task);
                    taskMap.put(taskId, task);

                }

                if (row.get("states_id") != null){
                    statesId = (Integer)row.get("states_id");
                    //state already in task object?
                    if (statesMap.get(taskId+statesId) == null){
                        taskState = new TaskState();

                        taskState.setId((Integer)row.get("states_id"));
                        taskState.setStateName((String)row.get("states_state_name"));
                        taskState.setTaskId((Integer)row.get("states_task_id"));

                        //put taskState to task element
                        ((Task)taskMap.get(taskId)).addTaskState(taskState);
                        statesMap.put(taskId+statesId, taskState);

                    }

                }

                if (row.get("users_mail") != null){

                    userId = (String)row.get("users_mail");
                    //user already in task object?

                    if (usersMap.get(taskId+userId.trim()) == null){
                        user = new User();

                        user.setUserID((String)row.get("users_mail"));
                        user.setFirstName((String)row.get("users_firstname"));
                        user.setLastName((String)row.get("users_lastname"));

                        //put taskState to task element
                        ((Task)taskMap.get(taskId)).addUser(user);
                        usersMap.put(taskId+userId.trim(), user);

                    }

                }

            }

        }

        return tasks;
    }

    public LinkedList<Task> loadAllByProject(int pID) {

        logger.debug("retrieve from db: all tasks by project with id="+pID);

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, TASK_TYPE, CREATION_DATE, UPDATE_DATE, PROJECT_ID, USER_MAIL, STATUS, EXECUTION_TYPE " +
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
            task.setProjectId((Integer)row.get("project_id"));
            task.setUserMail((String)row.get("user_mail"));
            task.setStatus((String)row.get("status"));
            task.setExecutionType((String)row.get("execution_type"));

            tasks.add(task);
        }

        return tasks;
    }

    public LinkedList<Task> loadAllByUser(String uID) {

        logger.debug("retrieve from db: all tasks by user with id="+uID);

        String sqlQuery = "SELECT TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.TASK_TYPE, TASK.CREATION_DATE, TASK.UPDATE_DATE, TASK.PROJECT_ID, TASK.USER_MAIL, TASK.STATUS, TASK.EXECUTION_TYPE " +
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
            task.setProjectId((Integer)row.get("project_id"));
            task.setUserMail((String)row.get("user_mail"));
            task.setStatus((String)row.get("status"));
            task.setExecutionType((String)row.get("execution_type"));

            tasks.add(task);
        }

        return tasks;
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

    public void addStateToTaskStates(TaskState state, int tID) {

        logger.debug("insert into db: add state ="+state.getStateName()+" to task with id="+tID);

        String sqlQuery = "INSERT INTO TASK_STATES (ID, STATE_NAME, TASK_ID) " +
                "VALUES (?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                this.getNewIDForTaskStates(),
                state.getStateName(),
                tID
        );


    }

    public LinkedList<Task> loadAllByProjectAndUser(int pID, String uID) {

        logger.debug("retrieve from db: all tasks from user with id="+uID+" and project with id="+pID);

        String sqlQuery = "SELECT TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.TASK_TYPE, TASK.CREATION_DATE, TASK.UPDATE_DATE, TASK.PROJECT_ID, TASK.USER_MAIL, TASK.STATUS, TASK.EXECUTION_TYPE, TASKIT_USER.FIRSTNAME, TASKIT_USER.LASTNAME, TASKIT_USER.MAIL, TASKIT_USER.AVATAR_URL " +
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
            task.setProjectId((Integer) row.get("project_id"));
            task.setUserMail((String)row.get("user_mail"));
            task.setStatus((String)row.get("status"));
            task.setExecutionType((String)row.get("execution_type"));

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

    public int getNewIDForTaskStates() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_task_states_id')",
                Integer.class);

        return id;
    }

}
