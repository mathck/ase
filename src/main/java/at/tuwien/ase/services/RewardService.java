package at.tuwien.ase.services;

import at.tuwien.ase.model.Reward;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface RewardService {

    int writeReward(Reward reward);

    boolean deleteReward(String rID);

    Reward getByID(int rID);
    LinkedList<Reward> getAllRewards();
    LinkedList<Reward> getAllRewardsFromUser(String uID);
    LinkedList<Reward> getAllRewardsFromProject(String pID);

    int getNewID();
}
