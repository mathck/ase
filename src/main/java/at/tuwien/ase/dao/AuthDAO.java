package at.tuwien.ase.dao;


import org.springframework.security.core.userdetails.UserDetails;



/**
 * Created by DanielHofer on 15.01.2016.
 */


public interface AuthDAO {

     UserDetails loadUserByUsername(String username);

     boolean userIsTaskAssignee(String userID, Integer tID);

     boolean userIsManagerInProjectByTask(String userID, Integer tID);


}