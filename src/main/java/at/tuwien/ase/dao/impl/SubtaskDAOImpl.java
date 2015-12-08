package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.model.Subtask;

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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

        String sqlQuery = "INSERT INTO SUBTASK (ID, TITLE, DESCRIPTION, TASK_ID, STATUS, XP, CREATION_DATE, UPDATE_DATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                subtask.getId(),
                subtask.getTitle(),
                subtask.getDescription(),
                subtask.getTaskId(),
                subtask.getStatus(),
                subtask.getXp(),
                subtask.getCreationDate(),
                subtask.getUpdateDate());
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

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, TASK_ID, STATUS, XP, CREATION_DATE, UPDATE_DATE " +
                "FROM SUBTASK " +
                "WHERE ID = ?";

        return this.jdbcTemplate.queryForObject(
                sqlQuery,
                new Object[]{tID},
                new RowMapper<Subtask>() {
                    public Subtask mapRow(ResultSet rs, int subtaskId) throws SQLException {
                        Subtask subtask = new Subtask();
                        subtask.setId(Integer.valueOf(rs.getString("id")));
                        subtask.setTitle(rs.getString("title"));
                        subtask.setDescription(rs.getString("description"));
                        subtask.setTaskId(rs.getInt("task_id"));
                        subtask.setStatus(rs.getString("status"));
                        subtask.setXp(rs.getInt("xp"));
                        subtask.setCreationDate(rs.getDate("creation_date"));
                        subtask.setUpdateDate(rs.getDate("update_date"));
                        return subtask;
                    }
                });
    }

    public LinkedList<Subtask> loadAll() {

        logger.debug("retrieve from db: all subtasks");

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, TASK_ID, STATUS, XP, CREATION_DATE, UPDATE_DATE " +
                "FROM SUBTASK";

        LinkedList<Subtask> subtasks = new LinkedList<Subtask>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery);

        for (Map<String,Object> row : rows) {

            Subtask subtask = new Subtask();
            subtask.setId((Integer)row.get("id"));
            subtask.setTitle((String)row.get("title"));
            subtask.setDescription((String)row.get("description"));
            subtask.setTaskId((Integer)row.get("task_id"));
            subtask.setStatus((String)row.get("status"));
            subtask.setXp((Integer) row.get("xp"));
            subtask.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            subtask.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));

            subtasks.add(subtask);
        }

        return subtasks;

    }

    public LinkedList<Subtask> loadAllByTask(int tID) {

        logger.debug("retrieve from db: all subtasks by task with id="+tID);

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, TASK_ID, STATUS, XP, CREATION_DATE, UPDATE_DATE " +
                "FROM SUBTASK " +
                "WHERE TASK_ID = ?";

        LinkedList<Subtask> subtasks = new LinkedList<Subtask>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                tID);

        for (Map<String,Object> row : rows) {

            Subtask subtask = new Subtask();
            subtask.setId((Integer)row.get("id"));
            subtask.setTitle((String)row.get("title"));
            subtask.setDescription((String)row.get("description"));
            subtask.setTaskId((Integer)row.get("task_id"));
            subtask.setStatus((String)row.get("status"));
            subtask.setXp((Integer) row.get("xp"));
            subtask.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            subtask.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));

            subtasks.add(subtask);
        }

        return subtasks;

    }

    public LinkedList<Subtask> loadAllByProject(int pID) {

        logger.debug("retrieve from db: all subtasks by project with id="+pID);

        String sqlQuery = "SELECT SUBTASK.ID, SUBTASK.TITLE, SUBTASK.DESCRIPTION, SUBTASK.TASK_ID, SUBTASK.STATUS, SUBTASK.XP, SUBTASK.CREATION_DATE, SUBTASK.UPDATE_DATE " +
                "FROM SUBTASK, TASK " +
                "WHERE TASK.ID = SUBTASK.TASK_ID " +
                "AND TASK.PROJECT_ID = ? ";

        LinkedList<Subtask> subtasks = new LinkedList<Subtask>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                pID);

        for (Map<String,Object> row : rows) {

            Subtask subtask = new Subtask();
            subtask.setId((Integer)row.get("id"));
            subtask.setTitle((String)row.get("title"));
            subtask.setDescription((String)row.get("description"));
            subtask.setTaskId((Integer)row.get("task_id"));
            subtask.setStatus((String)row.get("status"));
            subtask.setXp((Integer) row.get("xp"));
            subtask.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            subtask.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));

            subtasks.add(subtask);
        }

        return subtasks;

    }

    public LinkedList<Subtask> loadAllByUser(String uID) {

        logger.debug("retrieve from db: all subtasks by user with id="+uID);

        String sqlQuery = "SELECT SUBTASK.ID, SUBTASK.TITLE, SUBTASK.DESCRIPTION, SUBTASK.TASK_ID, SUBTASK.STATUS, SUBTASK.XP, SUBTASK.CREATION_DATE, SUBTASK.UPDATE_DATE " +
                "FROM SUBTASK, TASK " +
                "WHERE TASK.ID = SUBTASK.TASK_ID " +
                "AND TASK.USER_MAIL = ? ";

        LinkedList<Subtask> subtasks = new LinkedList<Subtask>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                uID);

        for (Map<String,Object> row : rows) {

            Subtask subtask = new Subtask();
            subtask.setId((Integer)row.get("id"));
            subtask.setTitle((String)row.get("title"));
            subtask.setDescription((String)row.get("description"));
            subtask.setTaskId((Integer)row.get("task_id"));
            subtask.setStatus((String)row.get("status"));
            subtask.setXp((Integer) row.get("xp"));
            subtask.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            subtask.setUpdateDate(new java.sql.Date(((Timestamp)row.get("update_date")).getTime()));

            subtasks.add(subtask);
        }

        return subtasks;

    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_subtask_id')",
                Integer.class);

        return id;
    }
}
