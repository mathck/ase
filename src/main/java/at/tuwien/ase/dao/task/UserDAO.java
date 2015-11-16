package at.tuwien.ase.dao.task;

import at.tuwien.ase.model.user.User;

/**
 * Created by DanielHofer on 16.16.11.2015.
 */
public interface UserDAO {

    /**
     * used to insert new User objects to db
     *
     * @param user
     * @return task id from db
     */
    public int insertUser(User user);

    /**
     * used to find user by id
     *
     * @param userId
     * @return user object
     */
    public User findByUserId(int userId);

}
