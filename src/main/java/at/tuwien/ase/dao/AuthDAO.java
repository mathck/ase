package at.tuwien.ase.dao;


import org.springframework.security.core.userdetails.UserDetails;



/**
 * Created by DanielHofer on 15.01.2016.
 */


public interface AuthDAO {

     UserDetails loadUserByUsername(String username);

     boolean userIsTaskAssignee(String userID, Integer tID);

     boolean userIsCommentCreator(String userID, Integer cID);

     boolean userIsManagerInProjectByTask(String userID, Integer tID);

     boolean userIsInProject(String userID, Integer pID);

     boolean userIsManagerInProject(String userID, Integer pID);

}