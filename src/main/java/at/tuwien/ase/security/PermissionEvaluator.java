package at.tuwien.ase.security;

import at.tuwien.ase.dao.AuthDAO;
import at.tuwien.ase.dao.SubtaskDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tomislav on 25.01.2016.
 */
@Service
public class PermissionEvaluator {

    @Autowired
    private AuthDAO authDAO;
    @Autowired
    private SubtaskDAO subtaskDAO;

    private static final Logger logger = LogManager.getLogger(PermissionEvaluator.class);

    public boolean hasPermission(String authUserId, Object o, Object o1) {
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
            if(subtaskDAO.findByID(subtaskId) == null) {
                return false;
            }
            Integer taskId = subtaskDAO.findByID(subtaskId).getTaskId();
            if (authDAO.userIsTaskAssignee(authUserId, taskId) || authDAO.userIsManagerInProjectByTask(authUserId, taskId)){
                return true;
            }else{
                return false;
            }
        }
        if (permission.equals("CHANGE_SUBTASK")){
            Integer subtaskId = (Integer) o;
            if(subtaskDAO.findByID(subtaskId) == null) {
                return false;
            }
            Integer taskId = subtaskDAO.findByID(subtaskId).getTaskId();
            if (authDAO.userIsManagerInProjectByTask(authUserId, taskId) || authDAO.userIsTaskAssignee(authUserId, taskId)){
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

}