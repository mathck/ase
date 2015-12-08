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

        this.jdbcTemplate.update(
                "INSERT INTO SUBTASK (ID, TITLE, DESCRIPTION, TASK_ID, STATUS, XP, CREATION_DATE, UPDATE_DATE) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                subtask.getId(), subtask.getTitle(), subtask.getDescription(), subtask.getTaskId(), subtask.getStatus(), subtask.getXp(), subtask.getCreationDate(), subtask.getUpdateDate());
    }

    public void removeSubtaskByID(int tID) {
        // TODO


    }

    public Subtask findByID(int tID) {
        return this.jdbcTemplate.queryForObject(
                "SELECT ID, TITLE, DESCRIPTION, TASK_ID, STATUS, XP, CREATION_DATE, UPDATE_DATE " +
                        "FROM SUBTASK " +
                        "WHERE ID = ?",
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

        String sql = "SELECT ID, TITLE, DESCRIPTION, TASK_ID, STATUS, XP, CREATION_DATE, UPDATE_DATE " +
                "FROM SUBTASK";

        LinkedList<Subtask> subtasks = new LinkedList<Subtask>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sql);
        for (Map<String,Object> row : rows) {

            Subtask subtask = new Subtask();
            subtask.setId((Integer)row.get("id"));
            subtask.setTitle((String)row.get("title"));
            subtask.setDescription((String)row.get("description"));
            subtask.setTaskId((Integer)row.get("task_id"));
            subtask.setStatus((String)row.get("status"));
            subtask.setXp((Integer) row.get("xp"));
            subtask.setCreationDate((Date)row.get("creation_date"));
            subtask.setUpdateDate((Date)row.get("update_date"));

            subtasks.add(subtask);
        }

        return subtasks;

    }

    public LinkedList<Subtask> loadAllByTask(String tID) {
        // TODO

        return null;
    }

    public LinkedList<Subtask> loadAllByProject(String pID) {
        // TODO

        return null;
    }

    public LinkedList<Subtask> loadAllByUser(String uID) {
        // TODO

        return null;
    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_subtask_id')",
                Integer.class);

        return id;
    }
}
