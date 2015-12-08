package at.tuwien.ase.dao;

import at.tuwien.ase.model.Reward;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface RewardDAO {

    Reward insertReward(Reward reward);
    void removeRewardByID(String rID);

    Reward findByID(int rID);
    LinkedList<Reward> loadAll();
    LinkedList<Reward> loadAllByUser(String uID);
    LinkedList<Reward> loadAllByProject(String pID);

    int getNewID();
}
