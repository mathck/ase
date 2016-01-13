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
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

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

    public JsonStringWrapper writeSubtask(Subtask subtask) {
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


        //get subtask from db
        s = subtaskDAO.findByID(sID);

        if (s != null){

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
                        taskElementJsonDb = subtaskDAO.findTaskItemByID(t.getId());

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

                            //if valiation is ok --> update taskElement
                            if (subtaskDAO.updateTaskItemById(t) == 0){
                                throw new Exception("error while updating task items");
                            }

                        }else{
                            throw new ValidationException("task item with id "+t.getId()+" could not be found");
                        }

                    }
                 }

                //close subtask?
                if (subtask.getStatus().trim().toLowerCase().equals("closed")){

                    int corretCount = 0;
                    int correctPercentage = 0;
                    int countTaskItems = 0;

                    //check solution
                    if (subtask.getTaskElements() != null && !subtask.getTaskElements().isEmpty()) {

                        //loop over taskElements from request
                        for (TaskElementJsonUpdate t : subtask.getTaskElements()) {

                            //get original task element from db
                            taskElementJsonDb = subtaskDAO.findTaskItemByID(t.getId());

                            //validate taskelEment status
                            if (taskElementJsonDb != null){

                                //compare solution from request with solution from db
                                if (!taskElementJsonDb.getItemType().trim().toLowerCase().equals("image")
                                        && !taskElementJsonDb.getItemType().trim().toLowerCase().equals("file")) {

                                    countTaskItems++;

                                    if (taskElementJsonDb.getSolution().trim().toLowerCase().equals(t.getStatus().trim().toLowerCase())) {
                                        corretCount++;
                                    }

                                }
                            }
                        }
                    }

                    //calc correct percentage
                    correctPercentage = (corretCount * 100) / countTaskItems;
                    subtask.setPercentageReached(correctPercentage);

                }else{
                    subtask.setStatus("open");
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
