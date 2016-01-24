package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.RewardDAO;
import at.tuwien.ase.model.Issue;
import at.tuwien.ase.model.Reward;
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
public class RewardDAOImpl implements RewardDAO{

    private static final Logger logger = LogManager.getLogger(RewardDAOImpl.class);

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }

    public void insertReward(Reward reward) {

        logger.debug("insert into db: reward with id=" + reward.getId());

        String sqlQuery = "INSERT INTO REWARD (ID, USER_MAIL, NAME, DESCRIPTION, XPBASE, IMAGE_LINK, CREATION_DATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ? )";

        this.jdbcTemplate.update(
                sqlQuery,
                reward.getId(),
                reward.getUserMail().trim(),
                reward.getName(),
                reward.getDescription(),
                reward.getXpbase(),
                reward.getImageLink(),
                reward.getCreationDate());

    }

    public void removeRewardByID(int rID) {

        logger.debug("delete from db: reward with id=" + rID);

        String sqlQuery = "DELETE " +
                "FROM REWARD " +
                "WHERE id = ? ";

        this.jdbcTemplate.update(
                sqlQuery,
                rID
        );
    }

    public Reward findByID(int rID) {

        logger.debug("retrieve from db: reward with id=" + rID);

        String sqlQuery = "SELECT ID, USER_MAIL, NAME, DESCRIPTION, XPBASE, IMAGE_LINK, CREATION_DATE " +
                "FROM REWARD " +
                "WHERE ID = ?";

        return this.jdbcTemplate.queryForObject(
                sqlQuery,
                new Object[]{rID},
                new RowMapper<Reward>() {
                    public Reward mapRow(ResultSet rs, int rewardId) throws SQLException {
                        Reward reward = new Reward();
                        reward.setId(Integer.valueOf(rs.getString("id")));
                        reward.setUserMail(rs.getString("user_mail"));
                        reward.setName(rs.getString("name"));
                        reward.setDescription(rs.getString("description"));
                        reward.setXpbase(rs.getInt("xpbase"));
                        reward.setImageLink(rs.getString("image_link"));
                        reward.setCreationDate(rs.getDate("creation_date"));
                        return reward;
                    }
                });

    }

    public LinkedList<Reward> loadAll() {

        logger.debug("retrieve from db: all rewards");

        String sqlQuery = "SELECT ID, USER_MAIL, NAME, DESCRIPTION, XPBASE, IMAGE_LINK, CREATION_DATE " +
                "FROM REWARD";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery);

        return mapRows(rows);

    }

    public LinkedList<Reward> loadAllRewardsCreatedByUser(String uID) {

        logger.debug("retrieve from db: all rewards by user with id="+uID);

        String sqlQuery = "SELECT ID, USER_MAIL, NAME, DESCRIPTION, XPBASE, IMAGE_LINK, CREATION_DATE " +
                "FROM REWARD " +
                "WHERE USER_MAIL = ? ";


        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                uID.trim());

        return mapRows(rows);

    }

    public LinkedList<Reward> loadAllRewardsAwardedToUser(String uID) {

        logger.debug("retrieve from db: all rewards awarded to user with id="+uID);

        String sqlQuery = "SELECT REWARD.ID, REWARD.USER_MAIL, REWARD.NAME, REWARD.DESCRIPTION, REWARD.XPBASE, REWARD.IMAGE_LINK, REWARD.CREATION_DATE " +
                "FROM REWARD, REL_USER_REWARD_PROJECT " +
                "WHERE REL_USER_REWARD_PROJECT.USER_MAIL = ? " +
                "AND REL_USER_REWARD_PROJECT.REWARD_ID = REWARD.ID";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                uID.trim());

        return mapRows(rows);
    }

    public LinkedList<Reward> loadAllByProject(int pID) {

        logger.debug("retrieve from db: all rewards by project with id="+pID);

        String sqlQuery = "SELECT REWARD.ID, REWARD.USER_MAIL, REWARD.NAME, REWARD.DESCRIPTION, REWARD.XPBASE, REWARD.IMAGE_LINK, REWARD.CREATION_DATE " +
                "FROM REWARD, REL_PROJECT_REWARD " +
                "WHERE REL_PROJECT_REWARD.PROJECT_ID = ? " +
                "AND REL_PROJECT_REWARD.REWARD_ID = REWARD.ID";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                pID);

        return mapRows(rows);
    }

    public LinkedList<Reward> loadAllByProjectAndUser(int pID, String uID) {

        logger.debug("retrieve from db: all rewards from user with id="+uID+" and project with id="+pID);

        String sqlQuery = "SELECT REWARD.ID, REWARD.USER_MAIL, REWARD.NAME, REWARD.DESCRIPTION, REWARD.XPBASE, REWARD.IMAGE_LINK, REWARD.CREATION_DATE " +
                "FROM REWARD, REL_USER_REWARD_PROJECT " +
                "WHERE REL_USER_REWARD_PROJECT.PROJECT_ID = ? " +
                "AND REL_USER_REWARD_PROJECT.USER_MAIL = ? " +
                "AND REL_USER_REWARD_PROJECT.REWARD_ID = REWARD.ID";

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(
                sqlQuery,
                pID,
                uID.trim());

        return mapRows(rows);
    }

    public void assignAwardToUser(int pID, String uID, int rID) {

        logger.debug("insert into db: add reward with id="+rID+" to user with id="+uID+" in project with id="+pID);

        String sqlQuery = "INSERT INTO REL_USER_REWARD_PROJECT (ID, USER_MAIL, REWARD_ID, PROJECT_ID) " +
                "VALUES (?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                this.getNewIDForRelRewardProjectUser(),
                uID.trim(),
                rID,
                pID
        );
    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_reward_id')",
                Integer.class);

        return id;
    }

    public int getNewIDForRelRewardProjectUser() {
        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_user_reward_project_id')",
                Integer.class);

        return id;
    }

    private LinkedList<Reward> mapRows(List<Map<String,Object>> rows){

        LinkedList<Reward> rewards = new LinkedList<Reward>();

        for (Map<String,Object> row : rows) {

            Reward reward = new Reward();
            reward.setId((Integer)row.get("id"));
            reward.setUserMail((String)row.get("user_mail"));
            reward.setName((String)row.get("name"));
            reward.setDescription((String)row.get("description"));
            reward.setXpbase((Integer)row.get("xpbase"));
            reward.setImageLink((String)row.get("image_link"));
            reward.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));

            rewards.add(reward);
        }

        return rewards;
    }

}
