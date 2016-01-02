package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.model.Subtask;

import at.tuwien.ase.model.TaskElementJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by DanielHofer on 20.11.2015.
 */

@Repository
public class SubtaskDAOImpl implements SubtaskDAO {

    private static final Logger logger = LogManager.getLogger(SubtaskDAOImpl.class);

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }

    public void insertSubtask(Subtask subtask) {

        logger.debug("insert into db: subtask with id=" + subtask.getId());

        String sqlQuery = "INSERT INTO SUBTASK (ID, TITLE, DESCRIPTION, DSL_TEMPLATE_ID, TASK_ID, STATUS, XP, CREATION_DATE, UPDATE_DATE, TASK_BODY) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                subtask.getId(),
                subtask.getTitle(),
                subtask.getDescription(),
                subtask.getDslTemplateId(),
                subtask.getTaskId(),
                subtask.getStatus(),
                subtask.getXp(),
                subtask.getCreationDate(),
                subtask.getUpdateDate(),
                subtask.getTaskBody()
        );
    }

    public void removeSubtaskByID(int tID) {

        logger.debug("delete from db: subtask with id=" + tID);

        String sqlQuery = "DELETE " +
                "FROM subtask " +
                "WHERE id = ? ";

        this.jdbcTemplate.update(
                sqlQuery,
                tID
        );
    }

    public Subtask findByID(int tID) {

        logger.debug("retrieve from db: subtask with id=" + tID);

        String sqlQuery = "SELECT SUBTASK.ID as subtasks_id, SUBTASK.TITLE as subtasks_title, SUBTASK.DESCRIPTION as subtasks_description, SUBTASK.DSL_TEMPLATE_ID as subtasks_dsl_template_id, " +
                "SUBTASK.TASK_ID as subtasks_task_id, SUBTASK.STATUS as subtasks_status, SUBTASK.TASK_BODY as subtasks_task_body, SUBTASK.XP as subtasks_xp, SUBTASK.CREATION_DATE as subtasks_creation_date, SUBTASK.UPDATE_DATE as subtasks_update_date, " +
                "TASK_ITEM.ID as taskitems_id, TASK_ITEM.ITEM_TYPE as taskitems_item_type, TASK_ITEM.LINK as taskitems_link, TASK_ITEM.STATUS as taskitems_status, TASK_ITEM.ITEM_VALUE as taskitems_value, TASK_ITEM.SUBTASK_ID as taskitems_subtask_id " +
                "FROM " +
                "TASK LEFT JOIN SUBTASK ON TASK.ID = SUBTASK.TASK_ID " +
                "LEFT JOIN TASK_ITEM ON SUBTASK.ID = TASK_ITEM.SUBTASK_ID " +
                "WHERE SUBTASK.ID = ? ";

        int subtaskId;
        Subtask subtask = new Subtask();
        HashMap<Integer, Subtask> subtaskMap = new HashMap<Integer, Subtask>();
        TaskElementJson taskElementJson;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                tID
        );

        for (Map<String,Object> row : rows) {


            if (row.get("subtasks_id") != null){

                subtaskId = (Integer)row.get("subtasks_id");

                //subtask already in linked list?
                if (subtask.getId() == null){

                    subtask.setId((Integer)row.get("subtasks_id"));
                    subtask.setTitle((String)row.get("subtasks_title"));
                    subtask.setDescription((String)row.get("subtasks_description"));
                    subtask.setDslTemplateId((Integer)row.get("subtasks_dsl_template_id"));
                    subtask.setTaskId((Integer)row.get("subtasks_task_id"));
                    subtask.setStatus((String)row.get("subtasks_status"));
                    subtask.setTaskBody((String)row.get("subtasks_task_body"));
                    subtask.setXp((Integer) row.get("subtasks_xp"));
                    subtask.setCreationDate(new java.sql.Date(((Timestamp)row.get("subtasks_creation_date")).getTime()));
                    subtask.setUpdateDate(new java.sql.Date(((Timestamp)row.get("subtasks_update_date")).getTime()));

                }

                //task item present?
                if (row.get("taskitems_subtask_id") != null){

                    //create task item and add it to subtask
                    taskElementJson = new TaskElementJson();

                    subtaskId = (Integer)row.get("taskitems_subtask_id");

                    taskElementJson.setId((Integer)row.get("taskitems_id"));
                    taskElementJson.setItemType((String)row.get("taskitems_item_type"));
                    taskElementJson.setLink((String)row.get("taskitems_link"));
                    taskElementJson.setStatus((String)row.get("taskitems_status"));
                    taskElementJson.setValue((String)row.get("taskitems_value"));
                    taskElementJson.setSubtaskId(subtaskId);

                    //add task element to subtask
                    subtask.addTaskElement(taskElementJson);
                }
            }
        }

        return subtask ;
    }

    public LinkedList<Subtask> loadAll() {

        logger.debug("retrieve from db: all subtasks");

        String sqlQuery = "SELECT SUBTASK.ID as subtasks_id, SUBTASK.TITLE as subtasks_title, SUBTASK.DESCRIPTION as subtasks_description, SUBTASK.DSL_TEMPLATE_ID as subtasks_dsl_template_id, " +
                "SUBTASK.TASK_ID as subtasks_task_id, SUBTASK.STATUS as subtasks_status, SUBTASK.TASK_BODY as subtasks_task_body, SUBTASK.XP as subtasks_xp, SUBTASK.CREATION_DATE as subtasks_creation_date, SUBTASK.UPDATE_DATE as subtasks_update_date, " +
                "TASK_ITEM.ID as taskitems_id, TASK_ITEM.ITEM_TYPE as taskitems_item_type, TASK_ITEM.LINK as taskitems_link, TASK_ITEM.STATUS as taskitems_status, TASK_ITEM.ITEM_VALUE as taskitems_value, TASK_ITEM.SUBTASK_ID as taskitems_subtask_id " +
                "FROM " +
                "TASK LEFT JOIN SUBTASK ON TASK.ID = SUBTASK.TASK_ID " +
                "LEFT JOIN TASK_ITEM ON SUBTASK.ID = TASK_ITEM.SUBTASK_ID ";

        int subtaskId;
        LinkedList<Subtask> subtasks = new LinkedList<Subtask>();
        HashMap<Integer, Subtask> subtaskMap = new HashMap<Integer, Subtask>();
        Subtask subtask;
        TaskElementJson taskElementJson;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery
        );

        for (Map<String,Object> row : rows) {


            if (row.get("subtasks_id") != null){

                subtaskId = (Integer)row.get("subtasks_id");

                //subtask already in linked list?
                if (subtaskMap.get(subtaskId) == null){

                    //create subtask
                    subtask = new Subtask();

                    subtask.setId((Integer)row.get("subtasks_id"));
                    subtask.setTitle((String)row.get("subtasks_title"));
                    subtask.setDescription((String)row.get("subtasks_description"));
                    subtask.setDslTemplateId((Integer)row.get("subtasks_dsl_template_id"));
                    subtask.setTaskId((Integer)row.get("subtasks_task_id"));
                    subtask.setStatus((String)row.get("subtasks_status"));
                    subtask.setTaskBody((String)row.get("subtasks_task_body"));
                    subtask.setXp((Integer) row.get("subtasks_xp"));
                    subtask.setCreationDate(new java.sql.Date(((Timestamp)row.get("subtasks_creation_date")).getTime()));
                    subtask.setUpdateDate(new java.sql.Date(((Timestamp)row.get("subtasks_update_date")).getTime()));

                    //add subtask to linked list and to hashmap
                    subtasks.add(subtask);
                    subtaskMap.put(subtaskId, subtask);
                }

                //task item present?
                if (row.get("taskitems_subtask_id") != null){

                    //create task item and add it to subtask
                    taskElementJson = new TaskElementJson();

                    subtaskId = (Integer)row.get("taskitems_subtask_id");

                    taskElementJson.setId((Integer)row.get("taskitems_id"));
                    taskElementJson.setItemType((String)row.get("taskitems_item_type"));
                    taskElementJson.setLink((String)row.get("taskitems_link"));
                    taskElementJson.setStatus((String)row.get("taskitems_status"));
                    taskElementJson.setValue((String)row.get("taskitems_value"));
                    taskElementJson.setSubtaskId(subtaskId);

                    //add task element to subtask
                    ((Subtask)subtaskMap.get(subtaskId)).addTaskElement(taskElementJson);
                }
            }
        }

        return subtasks;

    }

    public LinkedList<Subtask> loadAllByTask(int tID) {

        logger.debug("retrieve from db: all subtasks by task with id="+tID);

        String sqlQuery = "SELECT SUBTASK.ID as subtasks_id, SUBTASK.TITLE as subtasks_title, SUBTASK.DESCRIPTION as subtasks_description, SUBTASK.DSL_TEMPLATE_ID as subtasks_dsl_template_id, " +
                "SUBTASK.TASK_ID as subtasks_task_id, SUBTASK.STATUS as subtasks_status, SUBTASK.TASK_BODY as subtasks_task_body, SUBTASK.XP as subtasks_xp, SUBTASK.CREATION_DATE as subtasks_creation_date, SUBTASK.UPDATE_DATE as subtasks_update_date, " +
                "TASK_ITEM.ID as taskitems_id, TASK_ITEM.ITEM_TYPE as taskitems_item_type, TASK_ITEM.LINK as taskitems_link, TASK_ITEM.STATUS as taskitems_status, TASK_ITEM.ITEM_VALUE as taskitems_value, TASK_ITEM.SUBTASK_ID as taskitems_subtask_id " +
                "FROM " +
                "TASK LEFT JOIN SUBTASK ON TASK.ID = SUBTASK.TASK_ID " +
                "LEFT JOIN TASK_ITEM ON SUBTASK.ID = TASK_ITEM.SUBTASK_ID " +
                "WHERE TASK.ID = ?";

        int subtaskId;
        LinkedList<Subtask> subtasks = new LinkedList<Subtask>();
        HashMap<Integer, Subtask> subtaskMap = new HashMap<Integer, Subtask>();
        Subtask subtask;
        TaskElementJson taskElementJson;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                tID
        );

        for (Map<String,Object> row : rows) {


            if (row.get("subtasks_id") != null){

                subtaskId = (Integer)row.get("subtasks_id");

                //subtask already in linked list?
                if (subtaskMap.get(subtaskId) == null){

                    //create subtask
                    subtask = new Subtask();

                    subtask.setId((Integer)row.get("subtasks_id"));
                    subtask.setTitle((String)row.get("subtasks_title"));
                    subtask.setDescription((String)row.get("subtasks_description"));
                    subtask.setDslTemplateId((Integer)row.get("subtasks_dsl_template_id"));
                    subtask.setTaskId((Integer)row.get("subtasks_task_id"));
                    subtask.setStatus((String)row.get("subtasks_status"));
                    subtask.setTaskBody((String)row.get("subtasks_task_body"));
                    subtask.setXp((Integer) row.get("subtasks_xp"));
                    subtask.setCreationDate(new java.sql.Date(((Timestamp)row.get("subtasks_creation_date")).getTime()));
                    subtask.setUpdateDate(new java.sql.Date(((Timestamp)row.get("subtasks_update_date")).getTime()));

                    //add subtask to linked list and to hashmap
                    subtasks.add(subtask);
                    subtaskMap.put(subtaskId, subtask);
                }

                //task item present?
                if (row.get("taskitems_subtask_id") != null){

                    //create task item and add it to subtask
                    taskElementJson = new TaskElementJson();

                    subtaskId = (Integer)row.get("taskitems_subtask_id");

                    taskElementJson.setId((Integer)row.get("taskitems_id"));
                    taskElementJson.setItemType((String)row.get("taskitems_item_type"));
                    taskElementJson.setLink((String)row.get("taskitems_link"));
                    taskElementJson.setStatus((String)row.get("taskitems_status"));
                    taskElementJson.setValue((String)row.get("taskitems_value"));
                    taskElementJson.setSubtaskId(subtaskId);

                    //add task element to subtask
                    ((Subtask)subtaskMap.get(subtaskId)).addTaskElement(taskElementJson);
                }
            }
        }

        return subtasks;

    }

    public LinkedList<Subtask> loadAllByProject(int pID) {

        logger.debug("retrieve from db: all subtasks by project with id="+pID);

        String sqlQuery = "SELECT SUBTASK.ID as subtasks_id, SUBTASK.TITLE as subtasks_title, SUBTASK.DESCRIPTION as subtasks_description, SUBTASK.DSL_TEMPLATE_ID as subtasks_dsl_template_id, " +
                "SUBTASK.TASK_ID as subtasks_task_id, SUBTASK.STATUS as subtasks_status, SUBTASK.TASK_BODY as subtasks_task_body, SUBTASK.XP as subtasks_xp, SUBTASK.CREATION_DATE as subtasks_creation_date, SUBTASK.UPDATE_DATE as subtasks_update_date, " +
                "TASK_ITEM.ID as taskitems_id, TASK_ITEM.ITEM_TYPE as taskitems_item_type, TASK_ITEM.LINK as taskitems_link, TASK_ITEM.STATUS as taskitems_status, TASK_ITEM.ITEM_VALUE as taskitems_value, TASK_ITEM.SUBTASK_ID as taskitems_subtask_id " +
                "FROM " +
                "TASK LEFT JOIN SUBTASK ON TASK.ID = SUBTASK.TASK_ID " +
                "LEFT JOIN TASK_ITEM ON SUBTASK.ID = TASK_ITEM.SUBTASK_ID " +
                "WHERE TASK.PROJECT_ID = ? ";

        int subtaskId;
        LinkedList<Subtask> subtasks = new LinkedList<Subtask>();
        HashMap<Integer, Subtask> subtaskMap = new HashMap<Integer, Subtask>();
        Subtask subtask;
        TaskElementJson taskElementJson;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                pID
        );

        for (Map<String,Object> row : rows) {


            if (row.get("subtasks_id") != null){

                subtaskId = (Integer)row.get("subtasks_id");

                //subtask already in linked list?
                if (subtaskMap.get(subtaskId) == null){

                    //create subtask
                    subtask = new Subtask();

                    subtask.setId((Integer)row.get("subtasks_id"));
                    subtask.setTitle((String)row.get("subtasks_title"));
                    subtask.setDescription((String)row.get("subtasks_description"));
                    subtask.setDslTemplateId((Integer)row.get("subtasks_dsl_template_id"));
                    subtask.setTaskId((Integer)row.get("subtasks_task_id"));
                    subtask.setStatus((String)row.get("subtasks_status"));
                    subtask.setTaskBody((String)row.get("subtasks_task_body"));
                    subtask.setXp((Integer) row.get("subtasks_xp"));
                    subtask.setCreationDate(new java.sql.Date(((Timestamp)row.get("subtasks_creation_date")).getTime()));
                    subtask.setUpdateDate(new java.sql.Date(((Timestamp)row.get("subtasks_update_date")).getTime()));

                    //add subtask to linked list and to hashmap
                    subtasks.add(subtask);
                    subtaskMap.put(subtaskId, subtask);
                }

                //task item present?
                if (row.get("taskitems_subtask_id") != null){

                    //create task item and add it to subtask
                    taskElementJson = new TaskElementJson();

                    subtaskId = (Integer)row.get("taskitems_subtask_id");

                    taskElementJson.setId((Integer)row.get("taskitems_id"));
                    taskElementJson.setItemType((String)row.get("taskitems_item_type"));
                    taskElementJson.setLink((String)row.get("taskitems_link"));
                    taskElementJson.setStatus((String)row.get("taskitems_status"));
                    taskElementJson.setValue((String)row.get("taskitems_value"));
                    taskElementJson.setSubtaskId(subtaskId);

                    //add task element to subtask
                    ((Subtask)subtaskMap.get(subtaskId)).addTaskElement(taskElementJson);
                }
            }
        }

        return subtasks;

    }

    public LinkedList<Subtask> loadAllByUser(String uID) {

        logger.debug("retrieve from db: all subtasks by user with id="+uID);

        String sqlQuery = "SELECT SUBTASK.ID as subtasks_id, SUBTASK.TITLE as subtasks_title, SUBTASK.DESCRIPTION as subtasks_description, SUBTASK.DSL_TEMPLATE_ID as subtasks_dsl_template_id, " +
                "SUBTASK.TASK_ID as subtasks_task_id, SUBTASK.STATUS as subtasks_status, SUBTASK.TASK_BODY as subtasks_task_body, SUBTASK.XP as subtasks_xp, SUBTASK.CREATION_DATE as subtasks_creation_date, SUBTASK.UPDATE_DATE as subtasks_update_date, " +
                "TASK_ITEM.ID as taskitems_id, TASK_ITEM.ITEM_TYPE as taskitems_item_type, TASK_ITEM.LINK as taskitems_link, TASK_ITEM.STATUS as taskitems_status, TASK_ITEM.ITEM_VALUE as taskitems_value, TASK_ITEM.SUBTASK_ID as taskitems_subtask_id " +
                "FROM " +
                "TASK LEFT JOIN SUBTASK ON TASK.ID = SUBTASK.TASK_ID " +
                "LEFT JOIN TASK_ITEM ON SUBTASK.ID = TASK_ITEM.SUBTASK_ID " +
                "WHERE TASK.USER_MAIL = ? AND SUBTASK.ID IS NOT NULL";

        int subtaskId;
        LinkedList<Subtask> subtasks = new LinkedList<Subtask>();
        HashMap<Integer, Subtask> subtaskMap = new HashMap<Integer, Subtask>();
        Subtask subtask;
        TaskElementJson taskElementJson;

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                uID
        );

        for (Map<String,Object> row : rows) {


            if (row.get("subtasks_id") != null){

                subtaskId = (Integer)row.get("subtasks_id");

                //subtask already in linked list?
                if (subtaskMap.get(subtaskId) == null){

                    //create subtask
                    subtask = new Subtask();

                    subtask.setId((Integer)row.get("subtasks_id"));
                    subtask.setTitle((String)row.get("subtasks_title"));
                    subtask.setDescription((String)row.get("subtasks_description"));
                    subtask.setDslTemplateId((Integer)row.get("subtasks_dsl_template_id"));
                    subtask.setTaskId((Integer)row.get("subtasks_task_id"));
                    subtask.setStatus((String)row.get("subtasks_status"));
                    subtask.setTaskBody((String)row.get("subtasks_task_body"));
                    subtask.setXp((Integer) row.get("subtasks_xp"));
                    subtask.setCreationDate(new java.sql.Date(((Timestamp)row.get("subtasks_creation_date")).getTime()));
                    subtask.setUpdateDate(new java.sql.Date(((Timestamp)row.get("subtasks_update_date")).getTime()));

                    //add subtask to linked list and to hashmap
                    subtasks.add(subtask);
                    subtaskMap.put(subtaskId, subtask);
                }

                //task item present?
                if (row.get("taskitems_subtask_id") != null){

                    //create task item and add it to subtask
                    taskElementJson = new TaskElementJson();

                    subtaskId = (Integer)row.get("taskitems_subtask_id");

                    taskElementJson.setId((Integer)row.get("taskitems_id"));
                    taskElementJson.setItemType((String)row.get("taskitems_item_type"));
                    taskElementJson.setLink((String)row.get("taskitems_link"));
                    taskElementJson.setStatus((String)row.get("taskitems_status"));
                    taskElementJson.setValue((String)row.get("taskitems_value"));
                    taskElementJson.setSubtaskId(subtaskId);

                    //add task element to subtask
                    ((Subtask)subtaskMap.get(subtaskId)).addTaskElement(taskElementJson);
                }
            }
        }

        return subtasks;

    }

    public void addTaskItemToSubtask(TaskElementJson taskItem, int sID) throws Exception {

        logger.debug("insert into db: add task item to subtask with id="+sID);

        String sqlQuery = "INSERT INTO TASK_ITEM (ID, STATUS, ITEM_VALUE, LINK, ITEM_TYPE, ITEM_ID, SUBTASK_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                taskItem.getId(),
                taskItem.getStatus(),
                taskItem.getValue(),
                taskItem.getLink(),
                taskItem.getItemType(),
                taskItem.getItemId(),
                sID
        );

    }

    public int updateSubtaskById(int sID, Subtask subtask) throws Exception {

        logger.debug("update db: subtask with id="+sID);

        String sqlQuery = "UPDATE SUBTASK " +
                "SET TITLE = ?, DESCRIPTION = ?, TASK_BODY = ?, XP = ?, UPDATE_DATE = ? " +
                "WHERE ID = ?";

        return this.jdbcTemplate.update(
                sqlQuery,
                subtask.getTitle(),
                subtask.getDescription(),
                subtask.getTaskBody(),
                subtask.getXp(),
                new Date(), //updatedate
                sID
        );
    }

    public int updateTaskItemById(TaskElementJson taskItem) throws Exception {

        logger.debug("update db: task item with id="+taskItem.getId());

        String sqlQuery = "UPDATE TASK_ITEM " +
                "SET STATUS = ?, ITEM_VALUE = ?, LINK = ?, ITEM_TYPE = ?, ITEM_ID = ? " +
                "WHERE ID = ?";

        return this.jdbcTemplate.update(
                sqlQuery,
                taskItem.getStatus(),
                taskItem.getValue(),
                taskItem.getLink(),
                taskItem.getItemType(),
                taskItem.getItemId(),
                taskItem.getId()
        );
    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_subtask_id')",
                Integer.class);

        return id;
    }

    public int getNewIDForTaskItem() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_task_item_id')",
                Integer.class);

        return id;
    }

}
