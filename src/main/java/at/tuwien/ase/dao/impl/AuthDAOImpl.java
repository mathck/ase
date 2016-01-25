package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.AuthDAO;
import at.tuwien.ase.dao.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by Tomislav on 25.01.2016.
 */
@Repository
public class AuthDAOImpl implements AuthDAO {

    @Autowired
    private UserDAO userDAO;

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    private static final Logger logger = LogManager.getLogger(AuthDAOImpl.class);

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }

    public boolean userIsTaskAssignee(String userID, Integer tID) {
        String sqlQuery = "SELECT COUNT(*) as cnt " +
                "FROM " +
                "TASK LEFT JOIN REL_USER_TASK ON TASK.ID = REL_USER_TASK.TASK_ID " +
                "WHERE REL_USER_TASK.USER_MAIL = ? AND REL_USER_TASK.TASK_ID = ?";
        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                userID,
                tID
        );
        for (Map<String,Object> row : rows) {
            if (row.get("cnt") != null) {
                if ((Long)row.get("cnt") > 0){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    public boolean userIsManagerInProjectByTask(String userID, Integer tID) {
        String sqlQuery = "SELECT COUNT(*) as cnt " +
                "FROM " +
                "TASK LEFT JOIN REL_USER_PROJECT ON TASK.PROJECT_ID = REL_USER_PROJECT.PROJECT_ID " +
                "WHERE REL_USER_PROJECT.USER_MAIL = ? AND TASK.ID = ? AND REL_USER_PROJECT.ROLE = ?";
        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                userID,
                tID,
                new String("MANAGER")
        );
        for (Map<String,Object> row : rows) {
            if (row.get("cnt") != null) {
                if ((Long)row.get("cnt") > 0){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    public boolean userIsCommentCreator(String userID, Integer cID) {
        String sqlQuery = "SELECT COUNT(*) AS cnt " +
                "FROM task_comments " +
                "WHERE user_mail = ? AND id = ?";
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(
                sqlQuery,
                userID,
                cID
        );
        for (Map<String, Object> row : rows) {
            if (row.get("cnt") != null) {
                if ((Long) row.get("cnt") > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean userIsInProject(String userID, Integer pID) {
        String sqlQuery = "SELECT COUNT(*) as cnt " +
                "FROM rel_user_project " +
                "WHERE user_mail = ? AND project_id = ?";
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(
                sqlQuery,
                userID,
                pID
        );
        for (Map<String, Object> row : rows) {
            if (row.get("cnt") != null) {
                if ((Long) row.get("cnt") > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean userIsManagerInProject(String userID, Integer pID) {
        String sqlQuery = "SELECT COUNT(*) as cnt " +
                "FROM rel_user_project " +
                "WHERE user_mail = ? AND project_id = ? AND role = ?";
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(
                sqlQuery,
                userID,
                pID,
                new String("MANAGER")
        );
        for (Map<String, Object> row : rows) {
            if (row.get("cnt") != null) {
                if ((Long) row.get("cnt") > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

}
