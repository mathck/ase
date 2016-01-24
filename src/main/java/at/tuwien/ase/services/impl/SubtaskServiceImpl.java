package at.tuwien.ase.services.impl;

import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.model.*;
import at.tuwien.ase.services.DslTemplateService;
import at.tuwien.ase.services.SubtaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

/**
 * Created by DanielHofer on 20.11.2015.
 */
@Service
public class SubtaskServiceImpl implements SubtaskService {

    @Autowired
    private SubtaskDAO subtaskDAO;

    @Autowired
    private Validator validator;

    private static final Logger logger = LogManager.getLogger(SubtaskServiceImpl.class);

    public JsonStringWrapper writeSubtask(Subtask subtask)  {
        logger.debug("create new subtask");
        int id;

        //set id and date
        subtask.setCreationDate(new Date());
        subtask.setUpdateDate(new Date());

        //insert subtask
        id = subtaskDAO.insertSubtask(subtask);

        return new JsonStringWrapper(id);
    }

    public void deleteSubtaskByID(int sID) {
        logger.debug("delete subtask with id=" + sID);
        subtaskDAO.removeSubtaskByID(sID);
    }

    public void updateSubtask(int sID, SubtaskUpdate subtask) throws Exception {

        logger.debug("update subtask with id=" + sID);

        Subtask s;
        String subtaskStatus;
        int corretCount = 0;
        int correctPercentage = 0;
        int countTaskItems = 0;
        HashMap<Integer, TaskElementJson> taskElementMap;

        //Validate subtask
        Set<ConstraintViolation<SubtaskUpdate>> constraintViolationsSubtask = validator.validate(subtask);
        if (!constraintViolationsSubtask.isEmpty()){
            Iterator<ConstraintViolation<SubtaskUpdate>> flavoursIter = constraintViolationsSubtask.iterator();
            String validationError = new String("");

            while (flavoursIter.hasNext()){
                ConstraintViolation<SubtaskUpdate> violation =  flavoursIter.next();
                validationError += violation.getPropertyPath()+": "+violation.getMessage()+"\n";
            }

            throw new ValidationException(validationError);
        }


        subtaskStatus = subtask.getStatus();

        //get subtask from db
        s = subtaskDAO.findByID(sID);

        if (s != null){

            //get all taskElements for subtask
            taskElementMap = subtaskDAO.loadAllTaskItemsBySubtaskId(sID);

            //taskItem count differs?
            if (taskElementMap == null
                    || taskElementMap != null && subtask.getTaskElements() != null && taskElementMap.size() != subtask.getTaskElements().size()){
                throw new ValidationException("not all task items are present");
            }

            //subtask is not yet closed
            if (!s.getStatus().trim().toLowerCase().equals("closed")){

                TaskElementJson taskElementJsonDb;

                if (subtask.getTaskElements() != null && !subtask.getTaskElements().isEmpty()) {

                    //loop over taskElements from request
                    for (TaskElementJsonUpdate t : subtask.getTaskElements()) {

                        //validate task element
                        Set<ConstraintViolation<TaskElementJsonUpdate>> constraintViolationsTaskElement = validator.validate(t);
                        if (!constraintViolationsTaskElement.isEmpty()){
                            Iterator<ConstraintViolation<TaskElementJsonUpdate>> flavoursIter = constraintViolationsTaskElement.iterator();
                            String validationError = new String("");

                            while (flavoursIter.hasNext()){
                                ConstraintViolation<TaskElementJsonUpdate> violation =  flavoursIter.next();
                                validationError += violation.getPropertyPath()+": "+violation.getMessage()+"\n";
                            }
                            throw new ValidationException(validationError);
                        }

                        //get original task element from db
                        //taskElementJsonDb = subtaskDAO.findTaskItemByID(t.getId(), sID);
                        taskElementJsonDb = taskElementMap.get(t.getId());

                        //validate taskelEment status
                        if (taskElementJsonDb != null){

                            if (taskElementJsonDb.getItemType().trim().equals("checkbox")){
                                if (!t.getStatus().trim().toLowerCase().equals("checked") && !t.getStatus().trim().toLowerCase().equals("unchecked")){
                                    throw new ValidationException("Wrong status for taskItem with id "+t.getId()+". Allowed: checked or unchecked");
                                }
                            }

                            if (taskElementJsonDb.getItemType().trim().equals("slider")){
                                if (!taskElementJsonDb.getValue().trim().toLowerCase().contains("|"+t.getStatus().trim().toLowerCase())
                                        && !taskElementJsonDb.getValue().trim().toLowerCase().contains("|"+t.getStatus().trim().toLowerCase()+"|")
                                        && !taskElementJsonDb.getValue().trim().toLowerCase().contains(t.getStatus().trim().toLowerCase()+"|")
                                        ){
                                    throw new ValidationException("Wrong status for taskItem with id "+t.getId());
                                }
                            }

                            //close subtask? --> check solution of taskItem
                            if (subtaskStatus != null && subtaskStatus.trim().toLowerCase().equals("closed")) {
                                //compare solution from request with solution from db
                                if (!taskElementJsonDb.getItemType().trim().toLowerCase().equals("image")
                                        && !taskElementJsonDb.getItemType().trim().toLowerCase().equals("file")) {

                                    countTaskItems++;

                                    if (taskElementJsonDb.getSolution().trim().toLowerCase().equals(t.getStatus().trim().toLowerCase())) {
                                        corretCount++;
                                    }
                                }
                            }

                        }else{
                            throw new ValidationException("task item with id "+t.getId()+" could not be found");
                        }
                    }

                    //update all taskItems
                    subtaskDAO.updateTaskItemBatch(subtask.getTaskElements());

                    //close subtask? --> calc percentage of correct solved taskItems
                    if (subtaskStatus != null && subtaskStatus.trim().toLowerCase().equals("closed")) {
                        //calc correct percentage
                        correctPercentage = (corretCount * 100) / countTaskItems;
                        subtask.setPercentageReached(correctPercentage);
                    }else{
                        subtask.setPercentageReached(0);
                        subtask.setStatus("open");
                    }
                 }

                //update subtask
                subtaskDAO.updateSubtaskById(sID, subtask);


            }else{
                throw new ValidationException("subtask is closed");
            }

        }else{
            throw new ValidationException("subtask with ID="+sID+" could not be found");
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
