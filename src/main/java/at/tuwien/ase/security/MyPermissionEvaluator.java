package at.tuwien.ase.security;

import at.tuwien.ase.dao.AuthDAO;
import at.tuwien.ase.dao.SubtaskDAO;
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
    @Autowired
    private SubtaskDAO subtaskDAO;

    public boolean hasPermission(Authentication authentication, Object o, Object o1) {

        String authUserId = authentication.getName();
        String permission = (String) o1;

        if (permission.equals("VIEW_TASK")){
            Integer taskId = (Integer) o;
            if (authDAO.userIsTaskAssignee(authUserId, taskId) || authDAO.userIsManagerInProjectByTask(authUserId, taskId)){
                return true;
            }else{
                return false;
            }
        }

        if(permission.equals("CHANGE_TASK")) {
            Integer taskId = (Integer) o;
            if (authDAO.userIsManagerInProjectByTask(authUserId, taskId)){
                return true;
            }else{
                return false;
            }
        }

        if(permission.equals("CHANGE_COMMENT")) {
            Integer commentId = (Integer) o;
            if (authDAO.userIsCommentCreator(authUserId, commentId)){
                return true;
            }else{
                return false;
            }
        }

        if (permission.equals("VIEW_SUBTASK")){
            Integer subtaskId = (Integer) o;
            Integer taskId = subtaskDAO.findByID(subtaskId).getTaskId();
            if (authDAO.userIsTaskAssignee(authUserId, taskId) || authDAO.userIsManagerInProjectByTask(authUserId, taskId)){
                return true;
            }else{
                return false;
            }
        }
        if (permission.equals("CHANGE_SUBTASK")){
            Integer subtaskId = (Integer) o;
            Integer taskId = subtaskDAO.findByID(subtaskId).getTaskId();
            if (authDAO.userIsManagerInProjectByTask(authUserId, taskId)){
                return true;
            }else{
                return false;
            }
        }

        if (permission.equals("CHANGE_USER")) {
            String userId = (String) o;
            if (userId.trim().toLowerCase().equals(authUserId.trim().toLowerCase())){
                return true;
            }else{
                return false;
            }
        }

        if(permission.equals("VIEW_PROJECT")) {
            int projectId = (Integer) o;
            if(authDAO.userIsInProject(authUserId,projectId)) {
                return true;
            } else {
                return false;
            }
        }

        if(permission.equals("CHANGE_PROJECT")) {
            int projectId = (Integer) o;
            if(authDAO.userIsManagerInProject(authUserId,projectId)) {
                return true;
            } else {
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

