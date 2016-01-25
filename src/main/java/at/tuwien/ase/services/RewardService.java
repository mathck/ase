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
     * Write {@link Reward} to db.
     *
     * @param reward   {@link Reward} object.
     * @return         Id of {@link Reward} wrapped in {@link JsonStringWrapper}
     * @throws ValidationException      if an validation
     *                                  exception occurred
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    JsonStringWrapper writeReward(Reward reward) throws ValidationException, DataAccessException;

    /**
     *
     * Delete {@link Reward} by Id.
     *
     * @param rID  Id of {@link Reward}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void deleteRewardByID(int rID) throws DataAccessException;

    /**
     *
     * Get {@link Reward} by Id.
     *
     * @param rID  Id of {@link Reward}.
     * @return     {@link Reward} object.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    Reward getByID(int rID) throws DataAccessException;

    /**
     *
     * Get all {@link Reward} from db.
     *
     * @return {@link LinkedList} of {@link Reward}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Reward> getAllRewards() throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Reward} by specific {@link at.tuwien.ase.model.User}.
     *
     * @param uID    Id of {@link at.tuwien.ase.model.User}.
     * @return       {@link LinkedList} of {@link Reward}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Reward> getAllRewardsCreatedByUser(String uID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Reward} awarded to specific {@link at.tuwien.ase.model.User}.
     *
     * @param uID    Id of {@link at.tuwien.ase.model.User}.
     * @return       {@link LinkedList} of {@link Reward}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Reward> getAllRewardsAwardedToUser(String uID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Reward} from specific {@link at.tuwien.ase.model.Project}.
     *
     * @param pID    Id of {@link at.tuwien.ase.model.Project}.
     * @return       {@link LinkedList} of {@link Reward}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Reward> getAllRewardsFromProject(int pID) throws DataAccessException;

    /**
     *
     * Get {@link LinkedList} of {@link Reward} from specific {@link at.tuwien.ase.model.Project}
     * and {@link at.tuwien.ase.model.User}.
     *
     * @param pID    Id of {@link at.tuwien.ase.model.Project}.
     * @param uID    Id of {@link at.tuwien.ase.model.User}.
     * @return       {@link LinkedList} of {@link Reward}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<Reward> getAllRewardsFromProjectAndUser(int pID, String uID) throws DataAccessException;

    /**
     *
     * Assign {@link Reward} to {@link at.tuwien.ase.model.User}.
     *
     * @param pID    Id of {@link at.tuwien.ase.model.Project}.
     * @param uID    Id of {@link at.tuwien.ase.model.User}.
     * @param rID    Id of {@link Reward}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void assignAwardToUser(int pID, String uID, int rID) throws DataAccessException;

}
