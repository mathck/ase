package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.model.Subtask;
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

    @Autowired
    private ProjectDAO projectDAO;

    private static final Logger logger = LogManager.getLogger(SubtaskServiceImpl.class);

    public int writeSubtask(Subtask subtask) {
        logger.debug("create new subtask");
        int id;

        id = subtaskDAO.getNewID();
        subtask.setId(id);
        subtask.setCreationDate(new Date());
        subtask.setUpdateDate(new Date());

        subtaskDAO.insertSubtask(subtask);

        return id;
    }

    public boolean deleteSubtask(String pID, int sID) {
        logger.debug("delete subtask with id=" + sID);
        return subtaskDAO.removeSubtask(sID);
    }

    public Subtask getByID(int sID) {
        logger.debug("get subtask with id=" + sID);
        return subtaskDAO.findByID(sID);
    }

    public LinkedList<Subtask> getAllSubtasks() {
        logger.debug("get all subtasks");
        return subtaskDAO.loadAll();
    }

    public LinkedList<Subtask> getAllSubtasksFromTask(String tID) {
        logger.debug("get all subtasks from task with id="+tID);
        return subtaskDAO.loadAllByTask(tID);
    }

    public LinkedList<Subtask> getAllSubtasksFromUser(String uID) {
        logger.debug("get all subtasks from user with id="+uID);
        return subtaskDAO.loadAllByUser(uID);
    }

    public LinkedList<Subtask> getAllSubtasksFromProject(String pID) {
        logger.debug("get all subtasks from project with id="+pID);
        return subtaskDAO.loadAllByProject(pID);
    }


}
