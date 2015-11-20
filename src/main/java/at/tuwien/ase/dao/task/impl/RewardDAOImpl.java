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
                "INSERT INTO REWARD (ID, USER_MAIL, NAME, DESCRIPTION, XPBASE, IMAGE_LINK, CREATION_DATE) VALUES (?, ?, ?, ?, ?, ?, ? )",
                reward.getId(), reward.getUser_mail() ,reward.getName(), reward.getDescription(), reward.getXpbase(), reward.getImage_link(), new Date());

        return null;
    }

    public boolean removeReward(String rID) {
        return false;
    }

    public Reward findByID(int rID) {

        return this.jdbcTemplate.queryForObject(
                "SELECT ID, USER_MAIL, NAME, DESCRIPTION, XPBASE, IMAGE_LINK, CREATION_DATE FROM REWARD WHERE ID = ?",
                new Object[]{rID},
                new RowMapper<Reward>() {
                    public Reward mapRow(ResultSet rs, int taskId) throws SQLException {
                        Reward reward = new Reward();
                        reward.setId(Integer.valueOf(rs.getString("ID")));
                        reward.setUser_mail(rs.getString("USER_MAIL"));
                        reward.setName(rs.getString("NAME"));
                        reward.setDescription(rs.getString("DESCRIPTION"));
                        reward.setXpbase(rs.getInt("XPBASE"));
                        reward.setImage_link(rs.getString("IMAGE_LINK"));
                        reward.setCreation_date(rs.getDate("CREATION_DATE"));
                        return reward;
                    }
                });

    }

    public LinkedList<Reward> loadAll() {

        String sql = "SELECT ID, USER_MAIL, NAME, DESCRIPTION, XPBASE, IMAGE_LINK, CREATION_DATE FROM REWARD";

        LinkedList<Reward> rewards = new LinkedList<Reward>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sql);
        for (Map<String,Object> row : rows) {

            Reward customer = new Reward();
            customer.setId((Integer)row.get("id"));
            customer.setUser_mail((String)row.get("user_mail"));
            customer.setName((String)row.get("name"));
            customer.setDescription((String)row.get("description"));
            customer.setXpbase((Integer)row.get("xpbase"));
            customer.setImage_link((String)row.get("image_link"));
            customer.setCreation_date((Date)row.get("creation_date"));

            rewards.add(customer);
        }

        return rewards;

    }

    public LinkedList<Reward> loadAllByUser(String uID) {
        return null;
    }

    public LinkedList<Reward> loadAllByProject(String pID) {
        return null;
    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_reward_id')",
                Integer.class);

        return id;
    }
}
