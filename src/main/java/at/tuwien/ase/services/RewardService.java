package at.tuwien.ase.services;

import at.tuwien.ase.model.Reward;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface RewardService {

    int writeReward(Reward reward);

    void deleteRewardByID(int rID);

    Reward getByID(int rID);
    LinkedList<Reward> getAllRewards();
    LinkedList<Reward> getAllRewardsCreatedByUser(String uID);
    LinkedList<Reward> getAllRewardsAwardedToUser(String uID);
    LinkedList<Reward> getAllRewardsFromProject(int pID);
    LinkedList<Reward> getAllRewardsFromProjectAndUser(int pID, String uID);

    void assignAwardToUser(int pID, String uID, int rID);

}
