package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.model.Subtask;


import at.tuwien.ase.model.SubtaskUpdate;
import at.tuwien.ase.model.TaskElementJson;
import at.tuwien.ase.model.TaskElementJsonUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public int insertSubtask(final Subtask subtask) {

        logger.debug("insert into db: subtask with id=" + subtask.getId());

        final String sqlQuery = "INSERT INTO SUBTASK (TITLE, DESCRIPTION, DSL_TEMPLATE_ID, TASK_ID, STATUS, XP, CREATION_DATE, UPDATE_DATE, TASK_BODY) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sqlQuery.toString(), new String[] { "id" });
                ps.setString(1, subtask.getTitle());
                ps.setString(2, subtask.getDescription());
                ps.setInt(3, subtask.getDslTemplateId());
                ps.setInt(4, subtask.getTaskId());
                ps.setString(5, subtask.getStatus());
                ps.setInt(6, subtask.getXp());
                ps.setTimestamp(7, new java.sql.Timestamp(subtask.getCreationDate().getTime()));
                ps.setTimestamp(8, new java.sql.Timestamp(subtask.getUpdateDate().getTime()));
                ps.setString(9, subtask.getTaskBody());

                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().intValue();

    }

    public void insertSubtaskBatch(final List<Subtask> subtaskList, final LinkedList<Integer> taskIds, final String uuID){

        logger.debug("insert into db: subtask list");

        String sqlQuery = "INSERT INTO SUBTASK (TITLE, DESCRIPTION, DSL_TEMPLATE_ID, TASK_ID, STATUS, XP, CREATION_DATE, UPDATE_DATE, TASK_BODY, BATCH_UUID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {

            int j = 0;
            int currentTaskPosition = 0;

            public void setValues(PreparedStatement ps, int i) throws SQLException {

                //insert subtasks for each task (inner loop)
                if (j >= subtaskList.size()){
                    currentTaskPosition++; //increment counter for taskIds
                    j=0; //reset iterator for subtaskList
                }

                Subtask subtask = subtaskList.get(j);
                ps.setString(1, subtask.getTitle());
                ps.setString(2, subtask.getDescription());
                ps.setInt(3, subtask.getDslTemplateId());
                ps.setInt(4, taskIds.get(currentTaskPosition));
                ps.setString(5, subtask.getStatus());
                ps.setInt(6, subtask.getXp());
                ps.setTimestamp(7, new java.sql.Timestamp(subtask.getCreationDate().getTime()));
                ps.setTimestamp(8, new java.sql.Timestamp(subtask.getUpdateDate().getTime()));
                ps.setString(9, subtask.getTaskBody());
                ps.setString(10, uuID);

                j++;

            }

            public int getBatchSize() {
                return (subtaskList.size() * taskIds.size());
            }

        });
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

        String sqlQuery = "SELECT SUBTASK.ID as subtasks_id, SUBTASK.TITLE as subtasks_title, SUBTASK.DESCRIPTION as subtasks_description, SUBTASK.DSL_TEMPLATE_ID as subtasks_dsl_template_id, SUBTASK.PERCENTAGE_REACHED as subtasks_percentage_reached, " +
                "SUBTASK.TASK_ID as subtasks_task_id, SUBTASK.STATUS as subtasks_status, SUBTASK.TASK_BODY as subtasks_task_body, SUBTASK.XP as subtasks_xp, SUBTASK.CREATION_DATE as subtasks_creation_date, SUBTASK.UPDATE_DATE as subtasks_update_date, " +
                "TASK_ITEM.ID as taskitems_id, TASK_ITEM.ITEM_TYPE as taskitems_item_type, TASK_ITEM.LINK as taskitems_link, TASK_ITEM.STATUS as taskitems_status, TASK_ITEM.ITEM_VALUE as taskitems_value, TASK_ITEM.SUBTASK_ID as taskitems_subtask_id, TASK_ITEM.ITEM_ID as taskitems_item_id, TASK_ITEM.DSL_TEMPLATE_ID as taskitems_dsl_template_id, TASK_ITEM.SOLUTION as taskitems_solution  " +
                "FROM " +
                "TASK LEFT JOIN SUBTASK ON TASK.ID = SUBTASK.TASK_ID " +
                "LEFT JOIN TASK_ITEM ON SUBTASK.ID = TASK_ITEM.SUBTASK_ID " +
                "WHERE SUBTASK.ID = ? ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                tID
        );

        LinkedList<Subtask> subtaskList = mapRows(rows);

        //return first task element
        if (subtaskList != null && subtaskList.size() > 0){
            return subtaskList.get(0);
        }

        return null;
    }

    public TaskElementJson findTaskItemByID(int tID, int sID) {

        logger.debug("retrieve from db: task item with id=" + tID);

        LinkedList<TaskElementJson> taskElementJsonList = new LinkedList<TaskElementJson>();
        TaskElementJson taskElementJson;

        String sqlQuery = "SELECT ID, STATUS, ITEM_VALUE, LINK, ITEM_TYPE, SUBTASK_ID, ITEM_ID, DSL_TEMPLATE_ID, SOLUTION " +
                "FROM " +
                "TASK_ITEM " +
                "WHERE ID = ? AND SUBTASK_ID = ? ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                tID,
                sID
        );

        for (Map<String,Object> row : rows) {

            taskElementJson = new TaskElementJson();

            taskElementJson.setId((Integer) row.get("id"));
            taskElementJson.setStatus((String)row.get("status"));
            taskElementJson.setValue((String)row.get("item_value"));
            taskElementJson.setLink((String)row.get("link"));
            taskElementJson.setItemType((String)row.get("item_type"));
            taskElementJson.setSubtaskId((Integer) row.get("subtask_id"));
            taskElementJson.setItemId((Integer) row.get("item_id"));
            taskElementJson.setDslTemplateId((Integer) row.get("dsl_template_id"));
            taskElementJson.setSolution((String)row.get("solution"));

            taskElementJsonList.add(taskElementJson);
        }

        //return first element
        if (taskElementJsonList != null && taskElementJsonList.size() > 0){
            return taskElementJsonList.get(0);
        }

        return null;
    }

    public HashMap<Integer, LinkedList<Subtask>> loadSubtaskIdsByUuID(String uuID) {

        logger.debug("retrieve from db: all subtask ids by uuid");

        HashMap<Integer, LinkedList<Subtask>> subtaskHashMap = new HashMap<Integer, LinkedList<Subtask>>();
        LinkedList<Subtask> subtaskList;
        Subtask subtask;
        int id;
        int dslTemplateId;

        String sqlQuery = "SELECT ID, DSL_TEMPLATE_ID " +
                "FROM SUBTASK " +
                "WHERE BATCH_UUID = ? " +
                "ORDER BY ID ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                uuID);

        for (Map<String,Object> row : rows) {

            if (row.get("id") != null && row.get("dsl_template_id") != null) {

                id = (Integer)row.get("id");
                dslTemplateId = (Integer)row.get("dsl_template_id");

                subtask = new Subtask();
                subtask.setId(id);
                subtask.setDslTemplateId(dslTemplateId);

                if (subtaskHashMap.get(dslTemplateId) != null){
                    //add subtask to existing list
                    subtaskHashMap.get(dslTemplateId).add(subtask);

                }else{
                    //create new SubtaskList and add it to HashMap
                    subtaskList = new LinkedList<Subtask>() ;
                    subtaskList.add(subtask);
                    subtaskHashMap.put(dslTemplateId, subtaskList);
                }
            }
        }

        return subtaskHashMap;

    }

    public LinkedList<Subtask> loadAll() {

        logger.debug("retrieve from db: all subtasks");

        String sqlQuery = "SELECT SUBTASK.ID as subtasks_id, SUBTASK.TITLE as subtasks_title, SUBTASK.DESCRIPTION as subtasks_description, SUBTASK.DSL_TEMPLATE_ID as subtasks_dsl_template_id, SUBTASK.PERCENTAGE_REACHED as subtasks_percentage_reached, " +
                "SUBTASK.TASK_ID as subtasks_task_id, SUBTASK.STATUS as subtasks_status, SUBTASK.TASK_BODY as subtasks_task_body, SUBTASK.XP as subtasks_xp, SUBTASK.CREATION_DATE as subtasks_creation_date, SUBTASK.UPDATE_DATE as subtasks_update_date, " +
                "TASK_ITEM.ID as taskitems_id, TASK_ITEM.ITEM_TYPE as taskitems_item_type, TASK_ITEM.LINK as taskitems_link, TASK_ITEM.STATUS as taskitems_status, TASK_ITEM.ITEM_VALUE as taskitems_value, TASK_ITEM.SUBTASK_ID as taskitems_subtask_id, TASK_ITEM.ITEM_ID as taskitems_item_id, TASK_ITEM.DSL_TEMPLATE_ID as taskitems_dsl_template_id, TASK_ITEM.SOLUTION as taskitems_solution  " +
                "FROM " +
                "TASK LEFT JOIN SUBTASK ON TASK.ID = SUBTASK.TASK_ID " +
                "LEFT JOIN TASK_ITEM ON SUBTASK.ID = TASK_ITEM.SUBTASK_ID ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery
        );

        return mapRows(rows);

    }

    public LinkedList<Subtask> loadAllByTask(int tID) {

        logger.debug("retrieve from db: all subtasks by task with id="+tID);

        String sqlQuery = "SELECT SUBTASK.ID as subtasks_id, SUBTASK.TITLE as subtasks_title, SUBTASK.DESCRIPTION as subtasks_description, SUBTASK.DSL_TEMPLATE_ID as subtasks_dsl_template_id, SUBTASK.PERCENTAGE_REACHED as subtasks_percentage_reached, " +
                "SUBTASK.TASK_ID as subtasks_task_id, SUBTASK.STATUS as subtasks_status, SUBTASK.TASK_BODY as subtasks_task_body, SUBTASK.XP as subtasks_xp, SUBTASK.CREATION_DATE as subtasks_creation_date, SUBTASK.UPDATE_DATE as subtasks_update_date, " +
                "TASK_ITEM.ID as taskitems_id, TASK_ITEM.ITEM_TYPE as taskitems_item_type, TASK_ITEM.LINK as taskitems_link, TASK_ITEM.STATUS as taskitems_status, TASK_ITEM.ITEM_VALUE as taskitems_value, TASK_ITEM.SUBTASK_ID as taskitems_subtask_id, TASK_ITEM.ITEM_ID as taskitems_item_id, TASK_ITEM.DSL_TEMPLATE_ID as taskitems_dsl_template_id, TASK_ITEM.SOLUTION as taskitems_solution  " +
                "FROM " +
                "TASK LEFT JOIN SUBTASK ON TASK.ID = SUBTASK.TASK_ID " +
                "LEFT JOIN TASK_ITEM ON SUBTASK.ID = TASK_ITEM.SUBTASK_ID " +
                "WHERE TASK.ID = ?";

       List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                tID
        );

        return mapRows(rows);

    }

    public LinkedList<Subtask> loadAllByProject(int pID) {

        logger.debug("retrieve from db: all subtasks by project with id="+pID);

        String sqlQuery = "SELECT SUBTASK.ID as subtasks_id, SUBTASK.TITLE as subtasks_title, SUBTASK.DESCRIPTION as subtasks_description, SUBTASK.DSL_TEMPLATE_ID as subtasks_dsl_template_id, SUBTASK.PERCENTAGE_REACHED as subtasks_percentage_reached, " +
                "SUBTASK.TASK_ID as subtasks_task_id, SUBTASK.STATUS as subtasks_status, SUBTASK.TASK_BODY as subtasks_task_body, SUBTASK.XP as subtasks_xp, SUBTASK.CREATION_DATE as subtasks_creation_date, SUBTASK.UPDATE_DATE as subtasks_update_date, " +
                "TASK_ITEM.ID as taskitems_id, TASK_ITEM.ITEM_TYPE as taskitems_item_type, TASK_ITEM.LINK as taskitems_link, TASK_ITEM.STATUS as taskitems_status, TASK_ITEM.ITEM_VALUE as taskitems_value, TASK_ITEM.SUBTASK_ID as taskitems_subtask_id, TASK_ITEM.ITEM_ID as taskitems_item_id, TASK_ITEM.DSL_TEMPLATE_ID as taskitems_dsl_template_id, TASK_ITEM.SOLUTION as taskitems_solution  " +
                "FROM " +
                "TASK LEFT JOIN SUBTASK ON TASK.ID = SUBTASK.TASK_ID " +
                "LEFT JOIN TASK_ITEM ON SUBTASK.ID = TASK_ITEM.SUBTASK_ID " +
                "WHERE TASK.PROJECT_ID = ? ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                pID
        );

        return mapRows(rows);
    }

    public LinkedList<Subtask> loadAllByUser(String uID) {

        logger.debug("retrieve from db: all subtasks by user with id="+uID);

        String sqlQuery = "SELECT SUBTASK.ID as subtasks_id, SUBTASK.TITLE as subtasks_title, SUBTASK.DESCRIPTION as subtasks_description, SUBTASK.DSL_TEMPLATE_ID as subtasks_dsl_template_id, SUBTASK.PERCENTAGE_REACHED as subtasks_percentage_reached, " +
                "SUBTASK.TASK_ID as subtasks_task_id, SUBTASK.STATUS as subtasks_status, SUBTASK.TASK_BODY as subtasks_task_body, SUBTASK.XP as subtasks_xp, SUBTASK.CREATION_DATE as subtasks_creation_date, SUBTASK.UPDATE_DATE as subtasks_update_date, " +
                "TASK_ITEM.ID as taskitems_id, TASK_ITEM.ITEM_TYPE as taskitems_item_type, TASK_ITEM.LINK as taskitems_link, TASK_ITEM.STATUS as taskitems_status, TASK_ITEM.ITEM_VALUE as taskitems_value, TASK_ITEM.SUBTASK_ID as taskitems_subtask_id, TASK_ITEM.ITEM_ID as taskitems_item_id, TASK_ITEM.DSL_TEMPLATE_ID as taskitems_dsl_template_id, TASK_ITEM.SOLUTION as taskitems_solution  " +
                "FROM " +
                "TASK LEFT JOIN SUBTASK ON TASK.ID = SUBTASK.TASK_ID " +
                "LEFT JOIN TASK_ITEM ON SUBTASK.ID = TASK_ITEM.SUBTASK_ID " +
                "WHERE TASK.USER_MAIL = ? AND SUBTASK.ID IS NOT NULL";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                uID.trim()
        );

        return mapRows(rows);

    }

    public void addTaskItemToSubtask(TaskElementJson taskItem, int sID) throws Exception {

        logger.debug("insert into db: add task item to subtask with id="+sID);

        String sqlQuery = "INSERT INTO TASK_ITEM (STATUS, ITEM_VALUE, LINK, ITEM_TYPE, ITEM_ID, SUBTASK_ID, DSL_TEMPLATE_ID, SOLUTION) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                taskItem.getStatus(),
                taskItem.getValue(),
                taskItem.getLink(),
                taskItem.getItemType(),
                taskItem.getItemId(),
                sID,
                taskItem.getDslTemplateId(),
                taskItem.getSolution()
        );

    }

    public void addTaskItemToSubtaskBatch(final List<TaskElementJson> taskElementJsonList){

        logger.debug("insert into db: add task item to subtask batch");

         String sqlQuery = "INSERT INTO TASK_ITEM (STATUS, ITEM_VALUE, LINK, ITEM_TYPE, ITEM_ID, SUBTASK_ID, DSL_TEMPLATE_ID, SOLUTION) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {

                TaskElementJson taskElementJson = taskElementJsonList.get(i);
                ps.setString(1, taskElementJson.getStatus());
                ps.setString(2, taskElementJson.getValue());
                ps.setString(3, taskElementJson.getLink());
                ps.setString(4, taskElementJson.getItemType());
                ps.setInt(5, taskElementJson.getItemId());
                ps.setInt(6, taskElementJson.getSubtaskId());
                ps.setInt(7, taskElementJson.getDslTemplateId());
                ps.setString(8, taskElementJson.getSolution());
            }

            public int getBatchSize() {
                return taskElementJsonList.size();
            }
        });

    }

    public int updateSubtaskById(int sID, SubtaskUpdate subtask) throws Exception {

        logger.debug("update db: subtask with id="+sID);

        String sqlQuery = "UPDATE SUBTASK " +
                "SET TITLE = ?, DESCRIPTION = ?, XP = ?, PERCENTAGE_REACHED = ?, STATUS = ?, UPDATE_DATE = ? " +
                "WHERE ID = ?";

        return this.jdbcTemplate.update(
                sqlQuery,
                subtask.getTitle(),
                subtask.getDescription(),
                subtask.getXp(),
                subtask.getPercentageReached(),
                subtask.getStatus(),
                new Date(), //updatedate
                sID
        );
    }

    public void updateTaskItemBatch(final LinkedList<TaskElementJsonUpdate> taskItemList) throws Exception {

        logger.debug("update db: task items batch ");

        String sqlQuery = "UPDATE TASK_ITEM " +
                "SET STATUS = ?, ITEM_VALUE = ?, LINK = ? " +
                "WHERE ID = ?";

        this.jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {

                TaskElementJsonUpdate taskItem = taskItemList.get(i);
                ps.setString(1, taskItem.getStatus());
                ps.setString(2, taskItem.getValue());
                ps.setString(3, taskItem.getLink());
                ps.setInt(4, taskItem.getId());

            }

            public int getBatchSize() {
                return taskItemList.size();
            }

        });
    }

    public HashMap<Integer, TaskElementJson> loadAllTaskItemsBySubtaskId(Integer sID){

        logger.debug("retrieve from db: all task items from subtask with id=" + sID);

        HashMap<Integer, TaskElementJson> taskElementMap = new HashMap<Integer, TaskElementJson>();
        TaskElementJson taskElementJson;

        String sqlQuery = "SELECT ID, STATUS, ITEM_VALUE, LINK, ITEM_TYPE, SUBTASK_ID, ITEM_ID, DSL_TEMPLATE_ID, SOLUTION " +
                "FROM " +
                "TASK_ITEM " +
                "WHERE SUBTASK_ID = ? ";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                sID
        );

        for (Map<String,Object> row : rows) {

            taskElementJson = new TaskElementJson();

            taskElementJson.setId((Integer) row.get("id"));
            taskElementJson.setStatus((String)row.get("status"));
            taskElementJson.setValue((String)row.get("item_value"));
            taskElementJson.setLink((String)row.get("link"));
            taskElementJson.setItemType((String)row.get("item_type"));
            taskElementJson.setSubtaskId((Integer) row.get("subtask_id"));
            taskElementJson.setItemId((Integer) row.get("item_id"));
            taskElementJson.setDslTemplateId((Integer) row.get("dsl_template_id"));
            taskElementJson.setSolution((String)row.get("solution"));

            taskElementMap.put(taskElementJson.getId(), taskElementJson);
        }

        return taskElementMap;

    }

    private LinkedList<Subtask>  mapRows(List<Map<String,Object>> rows){
        int subtaskId;
        int taskitemId;
        LinkedList<Subtask> subtasks = new LinkedList<Subtask>();
        HashMap<Integer, Subtask> subtaskMap = new HashMap<Integer, Subtask>();
        HashMap<String, TaskElementJson> taskElementJsonMap = new HashMap<String, TaskElementJson>();
        Subtask subtask;
        String combinedId;
        TaskElementJson taskElementJson;

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
                    subtask.setPercentageReached((Integer)row.get("subtasks_percentage_reached"));

                    //add subtask to linked list and to hashmap
                    subtasks.add(subtask);
                    subtaskMap.put(subtaskId, subtask);
                }

                if (row.get("taskitems_id") != null) {

                    taskitemId = (Integer) row.get("taskitems_id");

                    combinedId = String.valueOf(subtaskId)+"_"+String.valueOf(taskitemId);

                    //subtask already in task object?
                    if (taskElementJsonMap.get(combinedId) == null) {
                        //create task item and add it to subtask
                        taskElementJson = new TaskElementJson();

                        subtaskId = (Integer)row.get("taskitems_subtask_id");

                        taskElementJson.setId((Integer)row.get("taskitems_id"));
                        taskElementJson.setItemType((String)row.get("taskitems_item_type"));
                        taskElementJson.setLink((String)row.get("taskitems_link"));
                        taskElementJson.setStatus((String)row.get("taskitems_status"));
                        taskElementJson.setValue((String)row.get("taskitems_value"));
                        taskElementJson.setItemId((Integer)row.get("taskitems_item_id"));
                        taskElementJson.setSubtaskId(subtaskId);
                        taskElementJson.setDslTemplateId((Integer)row.get("taskitems_dsl_template_id"));
                        taskElementJson.setSolution((String)row.get("taskitems_solution"));

                        //add task element to subtask
                        ((Subtask)subtaskMap.get(subtaskId)).addTaskElement(taskElementJson);
                        taskElementJsonMap.put(combinedId, taskElementJson);

                    }

                }
            }

        }
        return subtasks;
    }


}
