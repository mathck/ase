package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.RewardDAO;
import at.tuwien.ase.model.JsonStringWrapper;
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

    public JsonStringWrapper writeReward(Reward reward) {
        logger.debug("create new reward");
        int id;

        id = rewardDAO.getNewID();
        reward.setId(id);
        reward.setCreationDate(new Date());

        rewardDAO.insertReward(reward);

        return new JsonStringWrapper(id);
    }

    public void deleteRewardByID(int rID) {
        logger.debug("delete reward with id="+rID);
        rewardDAO.removeRewardByID(rID);
    }

    public Reward getByID(int rID) {
        logger.debug("get reward with id="+rID);
        return rewardDAO.findByID(rID);
    }

    public LinkedList<Reward> getAllRewards() {
        logger.debug("get all rewards");
        return rewardDAO.loadAll();
    }

    public LinkedList<Reward> getAllRewardsCreatedByUser(String uID) {
        logger.debug("get all rewards created from user with id="+uID);
        return rewardDAO.loadAllRewardsCreatedByUser(uID);
    }

    public LinkedList<Reward> getAllRewardsAwardedToUser(String uID) {
        logger.debug("get all rewards awarded to user with id="+uID);
        return rewardDAO.loadAllRewardsAwardedToUser(uID);
    }

    public LinkedList<Reward> getAllRewardsFromProject(int pID) {
        logger.debug("get all rewards from project with id="+pID);
        return rewardDAO.loadAllByProject(pID);
    }

    public LinkedList<Reward> getAllRewardsFromProjectAndUser(int pID, String uID) {
        logger.debug("get all rewards from project " + pID + " and from user " + uID);
        return rewardDAO.loadAllByProjectAndUser(pID, uID);
    }

    public void assignAwardToUser(int pID, String uID, int rID) {
        logger.debug("assign reward with id="+rID+" to user with id="+uID+" in project with id="+pID);
        rewardDAO.assignAwardToUser(pID, uID, rID);
    }

}
