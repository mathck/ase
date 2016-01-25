package at.tuwien.ase.dao;

/**
 * Created by Tomislav on 25.01.2016.
 */
public interface AuthDAO {

    boolean userIsTaskAssignee(String userID, Integer tID);

    boolean userIsCommentCreator(String userID, Integer cID);

    boolean userIsManagerInProjectByTask(String userID, Integer tID);

    boolean userIsInProject(String userID, Integer pID);

    boolean userIsManagerInProject(String userID, Integer pID);

}
