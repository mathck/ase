package at.tuwien.ase.security;

import at.tuwien.ase.dao.AuthDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * Created by DanielHofer on 15.01.2016.
 */
public class MyPermissionEvaluator implements PermissionEvaluator {


    @Autowired
    private AuthDAO authDAO;

    public boolean hasPermission(Authentication authentication, Object o, Object o1) {

        String authUserId = authentication.getName();
        String permission = (String) o1;

        if (permission.equals("VIEW_TASK")){

            Integer taskId = (Integer) o;

            //permission view task: user is task assignee or project manager
            if (authDAO.userIsTaskAssignee(authUserId, taskId) || authDAO.userIsManagerInProjectByTask(authUserId, taskId)){
                return true;
            }else{
                return false;
            }
        }


        if (permission.equals("DELETE_USER")) {

            String userId = (String) o;

            //only the authenticated user is permitted to delete his own account
            if (userId.trim().toLowerCase().equals(authUserId.trim().toLowerCase())){
                return true;
            }else{
                return false;
            }
        }



        //check further permissions...


        //default
        return false;
    }

    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {


        return false;
    }
}

