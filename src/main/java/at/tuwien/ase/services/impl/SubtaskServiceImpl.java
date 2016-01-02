package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Subtask;
import at.tuwien.ase.model.TaskElementJson;
import at.tuwien.ase.services.DslTemplateService;
import at.tuwien.ase.services.SubtaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by DanielHofer on 20.11.2015.
 */
@Service
public class SubtaskServiceImpl implements SubtaskService {

    @Autowired
    private SubtaskDAO subtaskDAO;

    private static final Logger logger = LogManager.getLogger(SubtaskServiceImpl.class);

    public JsonStringWrapper writeSubtask(Subtask subtask) {
        logger.debug("create new subtask");
        int id;

        //set id and date
        id = subtaskDAO.getNewID();
        subtask.setId(id);
        subtask.setCreationDate(new Date());
        subtask.setUpdateDate(new Date());

        //insert subtask
        subtaskDAO.insertSubtask(subtask);

        return new JsonStringWrapper(id);
    }

    public void deleteSubtaskByID(int sID) {
        logger.debug("delete subtask with id=" + sID);
        subtaskDAO.removeSubtaskByID(sID);
    }

    public void updateSubtask(int sID, Subtask subtask) throws Exception {
        logger.debug("update subtask with id=" + sID);

        if (subtaskDAO.updateSubtaskById(sID, subtask) == 0){
            throw new Exception("subtask with ID="+sID+" could not be found");
        }


        //TODO improvement: Is it even allowed to add new task elements or to delete/modify existing ones?
        //TODO close subtasks and tasks

        if (subtask.getTaskElements() != null && !subtask.getTaskElements().isEmpty()) {

            for (TaskElementJson t : subtask.getTaskElements()) {


                //if no task item id set --> insert
                if (t.getId() == null){
                    //create new task item id
                    t.setId(subtaskDAO.getNewIDForTaskItem());
                    subtaskDAO.addTaskItemToSubtask(t, sID);
                }else {
                    //if task item id set --> update
                    if (subtaskDAO.updateTaskItemById(t) == 0){
                        throw new Exception("error while updating task items");
                    }
                }

            }

        }

    }

    public Subtask getByID(int sID) {
        logger.debug("get subtask with id=" + sID);
        return subtaskDAO.findByID(sID);
    }

    public LinkedList<Subtask> getAllSubtasks() {
        logger.debug("get all subtasks");
        return subtaskDAO.loadAll();
    }

    public LinkedList<Subtask> getAllSubtasksFromTask(int tID) {
        logger.debug("get all subtasks from task with id="+tID);
        return subtaskDAO.loadAllByTask(tID);
    }

    public LinkedList<Subtask> getAllSubtasksFromUser(String uID) {
        logger.debug("get all subtasks from user with id="+uID);
        return subtaskDAO.loadAllByUser(uID);
    }

    public LinkedList<Subtask> getAllSubtasksFromProject(int pID) {
        logger.debug("get all subtasks from project with id="+pID);
        return subtaskDAO.loadAllByProject(pID);
    }


}
