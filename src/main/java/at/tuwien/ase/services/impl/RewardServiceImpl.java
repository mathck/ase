package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.RewardDAO;
import at.tuwien.ase.model.Reward;
import at.tuwien.ase.services.RewardService;

import java.util.Date;
import java.util.LinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DanielHofer on 20.11.2015.
 */
@Service
public class RewardServiceImpl implements RewardService {

    @Autowired
    private RewardDAO rewardDAO;

    private static final Logger logger = LogManager.getLogger(IssueServiceImpl.class);

    public int writeReward(Reward reward) {
        logger.debug("create new reward");
        int id;

        id = this.getNewID();
        reward.setId(id);
        reward.setCreationDate(new Date());

        rewardDAO.insertReward(reward);

        return id;
    }

    public boolean deleteReward(String rID) {
        logger.debug("delete reward with id="+rID);
        return rewardDAO.removeReward(rID);
    }

    public Reward getByID(int rID) {
        logger.debug("get reward with id="+rID);
        return rewardDAO.findByID(rID);
    }

    public LinkedList<Reward> getAllRewards() {
        logger.debug("get all rewards");
        return rewardDAO.loadAll();
    }

    public LinkedList<Reward> getAllRewardsFromUser(String uID) {
        logger.debug("get all rewards from user with id="+uID);
        return rewardDAO.loadAllByUser(uID);
    }

    public LinkedList<Reward> getAllRewardsFromProject(String pID) {
        logger.debug("get all rewards from project with id="+pID);
        return rewardDAO.loadAllByProject(pID);
    }

    public int getNewID() {
        return rewardDAO.getNewID();
    }
}
