package at.tuwien.ase.dao.task.impl;

import at.tuwien.ase.dao.task.RewardDAO;
import at.tuwien.ase.model.miscellaneous.Reward;
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

    public Reward insertReward(Reward reward) {

        logger.debug("insert into db: reward with id=" + reward.getId());

        this.jdbcTemplate.update(
                "INSERT INTO REWARD (ID, USER_MAIL, NAME, DESCRIPTION, XPBASE, IMAGE_LINK, CREATION_DATE) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ? )",
                reward.getId(), reward.getUserMail() ,reward.getName(), reward.getDescription(), reward.getXpbase(), reward.getImageLink(), reward.getCreationDate());

        return null;
    }

    public boolean removeReward(String rID) {
        // TODO

        return false;
    }

    public Reward findByID(int rID) {

        return this.jdbcTemplate.queryForObject(
                "SELECT ID, USER_MAIL, NAME, DESCRIPTION, XPBASE, IMAGE_LINK, CREATION_DATE " +
                        "FROM REWARD " +
                        "WHERE ID = ?",
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

        String sql = "SELECT ID, USER_MAIL, NAME, DESCRIPTION, XPBASE, IMAGE_LINK, CREATION_DATE " +
                "FROM REWARD";

        LinkedList<Reward> rewards = new LinkedList<Reward>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sql);
        for (Map<String,Object> row : rows) {

            Reward reward = new Reward();
            reward.setId((Integer)row.get("id"));
            reward.setUserMail((String)row.get("user_mail"));
            reward.setName((String)row.get("name"));
            reward.setDescription((String)row.get("description"));
            reward.setXpbase((Integer)row.get("xpbase"));
            reward.setImageLink((String)row.get("image_link"));
            reward.setCreationDate((Date)row.get("creation_date"));

            rewards.add(reward);
        }

        return rewards;

    }

    public LinkedList<Reward> loadAllByUser(String uID) {
        // TODO

        return null;
    }

    public LinkedList<Reward> loadAllByProject(String pID) {
        // TODO

        return null;
    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_reward_id')",
                Integer.class);

        return id;
    }
}
