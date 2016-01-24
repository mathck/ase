package at.tuwien.ase.dao.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.tuwien.ase.dao.AuthDAO;
import at.tuwien.ase.dao.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.GrantedAuthority;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;


/**
 * Created by DanielHofer on 15.01.2016.
 */

@Repository
public class AuthDAOImpl implements AuthDAO, UserDetailsService  {

    @Autowired
    private UserDAO userDAO;

    private static final Logger logger = LogManager.getLogger(DslTemplateDAOImpl.class);

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }


    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException
    { UserDetails user = null;
        try {
            System.out.println("Getting access details from employee dao !!");

            // Ideally it should be fetched from database and populated instance of
            // #org.springframework.security.core.userdetails.User should be returned from this method

       at.tuwien.ase.model.User u;
        u = userDAO.authUser(username);
            LinkedList<GrantedAuthority> a = new LinkedList<GrantedAuthority>();
            a.add(new GrantedAuthority() {
                public String getAuthority() {
                    return "ROLE_APP";
                }
            });


             user = new User(u.getUserID().trim(),"a",a);
            // user = new User("a", "a", true, true, true, true, a);

            //UserDetails user = //new User(username, "password", true, true, true, true, new GrantedAuthority[]{ new GrantedAuthorityImpl("ROLE_USER") });

        }catch(Exception e){
            System.out.println("message: "+e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    public boolean userIsTaskAssignee(String userID, Integer tID) {
        logger.debug("retrieve from db: user is task assignee with id=" + tID);
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


        logger.debug("retrieve from db: user is project manager in project where taskId is=" + tID);

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