package at.tuwien.ase.dao;

import at.tuwien.ase.model.Reward;
import org.springframework.dao.DataAccessException;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
public interface RewardDAO {

    /**
     *
     * Insert {@link Reward} object to db
     *
     * @param reward {@link Reward} for db insertion.
     */
    void insertReward(Reward reward) throws DataAccessException;

    /**
     *
     * Remove {@link Reward} from db by id.
     *
     * @param rID Id of {@link Reward} to remove from db.
     */
    void removeRewardByID(int rID) throws DataAccessException;

    /**
     *
     * Get {@link Reward} from db by id.
     *
     * @param rID Id of {@link Reward} to select from db.
     * @return    {@link Reward} selected in db.
     */
    Reward findByID(int rID) throws DataAccessException;

    /**
     *
     * Get all {@link Reward} from db.
     *
     * @return {@link LinkedList} of {@link Reward}.
     */
    LinkedList<Reward> loadAll() throws DataAccessException;

    /**
     *
     * Get all {@link Reward} created by {@link at.tuwien.ase.model.User}.
     * {@link at.tuwien.ase.model.User} must be specified by id.
     *
     * @param uID Id of {@link at.tuwien.ase.model.User}.
     * @return {@link LinkedList} of {@link Reward}.
     */
    LinkedList<Reward> loadAllRewardsCreatedByUser(String uID) throws DataAccessException;

    /**
     *
     * Get all {@link Reward} awarded to {@link at.tuwien.ase.model.User}.
     * {@link at.tuwien.ase.model.User} must be specified by id.
     *
     * @param uID Id of {@link at.tuwien.ase.model.User}.
     * @return {@link LinkedList} of {@link Reward}.
     */
    LinkedList<Reward> loadAllRewardsAwardedToUser(String uID) throws DataAccessException;

    /**
     *
     * Get all {@link Reward} of a specific {@link at.tuwien.ase.model.Project}.
     * {@link at.tuwien.ase.model.Project} must be specified by id.
     *
     * @param pID Id of {@link at.tuwien.ase.model.Project}.
     * @return {@link LinkedList} of {@link Reward}.
     */
    LinkedList<Reward> loadAllByProject(int pID) throws DataAccessException;

    /**
     *
     * Get all {@link Reward} of a specific {@link at.tuwien.ase.model.Project} and {@link at.tuwien.ase.model.User}.
     * {@link at.tuwien.ase.model.Project} and {@link at.tuwien.ase.model.User} must be specified by id.
     *
     * @param pID Id of {@link at.tuwien.ase.model.Project}.
     * @param uID Id of {@link at.tuwien.ase.model.User}.
     * @return {@link LinkedList} of {@link Reward}.
     */
    LinkedList<Reward> loadAllByProjectAndUser(int pID, String uID) throws DataAccessException;

    /**
     *
     * Award {@link at.tuwien.ase.model.User} with {@link Reward} in {@link at.tuwien.ase.model.Project}.
     *
     * @param pID Id of {@link at.tuwien.ase.model.Project}.
     * @param uID Id of {@link at.tuwien.ase.model.User}.
     * @param rID Id of {@link Reward}.
     */
    void assignAwardToUser(int pID, String uID, int rID) throws DataAccessException;

    /**
     *
     * Get new Id from db sequence for {@link Reward} insertion
     *
     * @return Generated id
     */
    int getNewID() throws DataAccessException;

    /**
     *
     *  Get new Id from db sequence in order to award {@link at.tuwien.ase.model.User}
     *  with {@link Reward}.
     *
     * @return Generated id
     */
    int getNewIDForRelRewardProjectUser() throws DataAccessException;
}
