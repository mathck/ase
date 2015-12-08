package at.tuwien.ase.dao;

import at.tuwien.ase.model.Reward;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface RewardDAO {

    void insertReward(Reward reward);
    void removeRewardByID(int rID);

    Reward findByID(int rID);
    LinkedList<Reward> loadAll();
    LinkedList<Reward> loadAllRewardsCreatedByUser(String uID);
    LinkedList<Reward> loadAllRewardsAwardedToUser(String uID);
    LinkedList<Reward> loadAllByProject(int pID);
    LinkedList<Reward> loadAllByProjectAndUser(int pID, String uID);

    void assignAwardToUser(int pID, String uID, int rID);

    int getNewID();
    int getNewIDForRelRewardProjectUser();
}
