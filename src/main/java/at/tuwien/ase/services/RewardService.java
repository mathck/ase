package at.tuwien.ase.services;

import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Reward;
import org.springframework.dao.DataAccessException;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface RewardService {

    /**
     *
     * @param reward
     * @return
     * @throws ValidationException
     * @throws DataAccessException
     */
    JsonStringWrapper writeReward(Reward reward) throws ValidationException, DataAccessException;

    /**
     *
     * @param rID
     * @throws DataAccessException
     */
    void deleteRewardByID(int rID)throws DataAccessException;

    /**
     *
     * @param rID
     * @return
     * @throws DataAccessException
     */
    Reward getByID(int rID)throws DataAccessException;

    /**
     *
     * @return
     * @throws DataAccessException
     */
    LinkedList<Reward> getAllRewards()throws DataAccessException;

    /**
     *
     * @param uID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Reward> getAllRewardsCreatedByUser(String uID)throws DataAccessException;

    /**
     *
     * @param uID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Reward> getAllRewardsAwardedToUser(String uID)throws DataAccessException;

    /**
     *
     * @param pID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Reward> getAllRewardsFromProject(int pID)throws DataAccessException;

    /**
     *
     * @param pID
     * @param uID
     * @return
     * @throws DataAccessException
     */
    LinkedList<Reward> getAllRewardsFromProjectAndUser(int pID, String uID)throws DataAccessException;

    /**
     *
     * @param pID
     * @param uID
     * @param rID
     * @throws DataAccessException
     */
    void assignAwardToUser(int pID, String uID, int rID)throws DataAccessException;

}
