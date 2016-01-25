package at.tuwien.ase.services.impl;

import java.util.*;

import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.model.*;
import at.tuwien.ase.model.javax.TaskElement;
import at.tuwien.ase.model.javax.Template;
import at.tuwien.ase.services.DslTemplateService;
import at.tuwien.ase.services.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * Created by Daniel Hofer on 16.11.2015.
 */

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDAO taskDAO;

    @Autowired
    private SubtaskDAO subtaskDAO;

    @Autowired
    private DslTemplateService dslTemplateService;

    @Autowired
    private Validator validator;


    private static final Logger logger = LogManager.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl() {
    }

    public TaskServiceImpl(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public Task getByID(int tID) throws DataAccessException {
        logger.debug("get task with id=" + tID);
        Task task =  taskDAO.findByID(tID);

        //get subtasks
        LinkedList<Subtask> subtaskList = subtaskDAO.loadAllByTask(task.getId());

        if (subtaskList != null){
            task.setSubtaskList(subtaskList);
        }

        return task;
    }

    public LinkedList<Integer> writeTask(int pID, Task task) throws Exception{

        LinkedList<TaskElementJson> taskElementJsonList = new LinkedList<TaskElementJson>();
        LinkedList<TaskElementJson> taskElementJsonListForDbBatch = new LinkedList<TaskElementJson>();
        LinkedList<Subtask> subtaskList = new LinkedList<Subtask>();
        LinkedList<Task> taskList = new LinkedList<Task>();
        LinkedList<Integer> taskIds = new LinkedList<Integer>();
        HashMap<Integer, LinkedList<Subtask>> subtaskHashMap = new HashMap<Integer, LinkedList<Subtask>>();
        LinkedList<Subtask> subtaskIdList = new LinkedList<Subtask>();
        TaskElementJson taskElementJson;
        Task t;
        int dslTempalteId;
        Subtask subtask;
        String uuID;

        logger.debug("post new task");

        //Validate task Json
        Set<ConstraintViolation<Task>> constraintViolationsSubtask = validator.validate(task);
        if (!constraintViolationsSubtask.isEmpty()){
            Iterator<ConstraintViolation<Task>> flavoursIter = constraintViolationsSubtask.iterator();
            String validationError = new String("");

            while (flavoursIter.hasNext()){
                ConstraintViolation<Task> violation =  flavoursIter.next();
                validationError += violation.getPropertyPath()+": "+violation.getMessage()+"\n";
            }

            throw new ValidationException(validationError);
        }

        if (task.getTaskStates() == null ||  task.getTaskStates().isEmpty()
                || task.getSubtaskList() == null ||  task.getSubtaskList().isEmpty()
                || task.getExecutionType() == null || task.getExecutionType().equals("")){
            throw new Exception("not all input values are present!");
        }

        //create task list
        if (task.getExecutionType().equals("collaborative_task")) {

            task.setStatus(task.getTaskStates().get(0).getStateName());
            task.setCreationDate(new Date());
            task.setUpdateDate(new Date());
            taskList.add(task);

        }else{

            if (task.getExecutionType().equals("single_task")) {
                String taskTitle = task.getTitle().trim();
                if (task.getUserList() != null && !task.getUserList().isEmpty()) {
                    for (User u : task.getUserList()) {

                        t = (Task) task.clone();
                        t.setTitle(u.getFirstName() + ": " + taskTitle);
                        t.setStatus(t.getTaskStates().get(0).getStateName());
                        t.setCreationDate(new Date());
                        t.setUpdateDate(new Date());

                        //single task --> only one contributor
                        LinkedList<User> userList = new LinkedList<User>();
                        userList.add(u);
                        t.setUserList(userList);

                        taskList.add(t);
                    }
                }
            }else{
                throw new Exception("execution type not supported");
            }
        }

        //generate random uuid for batch insert
        uuID  = UUID.randomUUID().toString();

        //create subtask and subtask elements
        createSubtasks(task, taskElementJsonList, subtaskList);

        //insert tasks
        taskDAO.insertTaskBatch(pID, taskList, uuID);
        taskIds = taskDAO.loadTaskIdsByUuID(uuID); //get ids from inserted tasks

        for (int i = 0; i < taskList.size(); i++) {
            //add contributors to task
            taskDAO.assignUserToTaskBatch(taskList.get(i).getUserList(), taskIds.get(i));
        }

        //add states to task state list
        taskDAO.addStateToTaskStatesBatch(task.getTaskStates(), taskIds);

        //store subtasks to db
        subtaskDAO.insertSubtaskBatch(subtaskList, taskIds, uuID);
        //load subtask hashmap
        subtaskHashMap = subtaskDAO.loadSubtaskIdsByUuID(uuID);


        //create taskItemJson List and label taskItems with subtask ids from db
        for (int i = 0; i < taskElementJsonList.size(); i++){

            taskElementJson = taskElementJsonList.get(i);

            if (taskElementJson != null && taskElementJson.getDslTemplateId() != null){

                dslTempalteId = taskElementJson.getDslTemplateId();

                //link taskItems and subtasks via dslTemplateId
                if (subtaskHashMap.get(dslTempalteId) != null){

                    subtaskIdList = subtaskHashMap.get(dslTempalteId);

                    for (int j = 0; j < subtaskIdList.size(); j++){

                        subtask = subtaskIdList.get(j);

                        if (subtask.getId() != null) {
                            //set subtask db id and add taskItem to list
                            taskElementJson.setSubtaskId(subtask.getId());
                            taskElementJsonListForDbBatch.add((TaskElementJson) taskElementJson.clone());
                        }

                    }
                }
            }
        }

        //insert taskItem list
        subtaskDAO.addTaskItemToSubtaskBatch(taskElementJsonListForDbBatch);

        return taskIds;

    }

    public void deleteTaskByID(int tID) throws DataAccessException {
        logger.debug("delete task with id="+tID);
        taskDAO.removeTaskByID(tID);
    }

    public LinkedList<Task> getAllTasks() throws DataAccessException {
        logger.debug("get all tasks");
        return taskDAO.loadAll();
    }

    public LinkedList<Task> getAllTasksFromUser(String uID) throws DataAccessException {
        logger.debug("get all tasks from user"+uID);
        return taskDAO.loadAllByUser(uID);
    }

    public LinkedList<Task> getAllTasksFromProject(int pID)throws DataAccessException {
        logger.debug("get all tasks from project");
        return taskDAO.loadAllByProject(pID);
    }

    public LinkedList<Task> getAllTasksFromProjectAndUser(int pID, String uID) throws DataAccessException {
        logger.debug("get all tasks from project " + pID + " and from user " + uID);
        return taskDAO.loadAllByProjectAndUser(pID, uID);
    }

    public LinkedList<Comment> getAllCommentsByTask(int tID) throws DataAccessException{
        logger.debug("get all comments from task with id=" + tID);
        return taskDAO.loadAllCommentsByTask(tID);
    }

    public LinkedList<User> getAllUserFromTask(int tID) throws DataAccessException{
        logger.debug("get all users from task with id=" + tID);
        return taskDAO.loadAllUsersByTask(tID);
    }

    public void assignUserToTask(int tID, String uID)throws DataAccessException {
        logger.debug("assign user with id="+uID+" to task with id="+tID);
        taskDAO.assignUserToTask(tID, uID);
    }

    public void removeUserFromTask(int tID, String uID) throws DataAccessException {
        logger.debug("remove user with id="+uID+" from task with id="+tID);
        taskDAO.removeUserFromTask(tID, uID);
    }

    public JsonStringWrapper addCommentToTask(int tID, Comment comment) throws ValidationException, DataAccessException {

        logger.debug("add comment to task with id="+tID);

        int id;

        //Validate task Json
        Set<ConstraintViolation<Comment>> constraintViolationsSubtask = validator.validate(comment);
        if (!constraintViolationsSubtask.isEmpty()){
            Iterator<ConstraintViolation<Comment>> flavoursIter = constraintViolationsSubtask.iterator();
            String validationError = new String("");

            while (flavoursIter.hasNext()){
                ConstraintViolation<Comment> violation =  flavoursIter.next();
                validationError += violation.getPropertyPath()+": "+violation.getMessage()+"\n";
            }

            throw new ValidationException(validationError);
        }

        Task task = taskDAO.findByID(tID);

        if (task != null){
            //are comments allowed for this task?
            if (task.isCommentsAllowed()){
                id = taskDAO.getNewIDForComments();
                comment.setId(id);
                taskDAO.addCommentToTask(tID, comment);

                return new JsonStringWrapper(id);
            }else{
                throw new ValidationException("comments are not allowed for this task");
            }
        }else{
            throw new ValidationException("Task with id "+tID+" could not be found");
        }

    }

    public void deleteCommentFromTask(int tID, int cID) throws DataAccessException {
        logger.debug("remove comment with id="+cID+" from task with id="+tID);
        taskDAO.removeCommentFromTask(tID, cID);
    }


    private void createSubtasks(Task task, LinkedList<TaskElementJson> taskElementJsonList, LinkedList<Subtask> subtaskList) throws Exception {
        DslTemplate dslTemplate;
        Template template;
        String taskBody;
        boolean elementAlreadyInList;
        String combinedId1;
        String combinedId2;
        List<TaskElement> taskElementList;
        TaskElementJson taskElementJson;

        //create subtasks from dsl template
        if (task.getSubtaskList() != null && !task.getSubtaskList().isEmpty()) {

            //for all subtasks
            for (Subtask subtask : task.getSubtaskList()) {

                //get dsl template for subtask from db
                dslTemplate = dslTemplateService.getByID(subtask.getDslTemplateId());

                //unmarshal dsl template to java objects
                template = dslTemplateService.unmarshalTemplateXml(dslTemplate);

                //convert task body to string and add it to Subtask
                taskBody = dslTemplateService.convertTaskBodyToString(template.getTaskBody().getContent());

                //for (int j = 0; j < taskList.size(); j++) {

                // t = taskList.get(j);

                Subtask s = (Subtask) subtask.clone();

                s.setTaskBody(taskBody);
                s.setTitle(template.getIdentifier().getTitle());
                s.setDescription(template.getIdentifier().getDescription());
                s.setStatus(new String("open"));
                s.setXp(template.getIdentifier().getEstimatedWorkTime().intValue());
                s.setGitHookAllowed(template.getIdentifier().isGithook());
                s.setCreationDate(new Date());
                s.setUpdateDate(new Date());

                //get task elements from tempalte and add it to subtask
                if (template.getTaskElements() != null && template.getTaskElements().getTaskElement() != null
                        && !template.getTaskElements().getTaskElement().isEmpty()) {

                    taskElementList = template.getTaskElements().getTaskElement();
                    for (TaskElement taskElement : taskElementList) {

                        taskElementJson = TaskElementJsonFactory.getTaskElement(taskElement);
                        taskElementJson.setDslTemplateId(s.getDslTemplateId());
                        taskElementJson.setSubtaskId(s.getId());

                        //do not add duplicates (=twice a taskElement of the same dsl where the itemId matches)
                        elementAlreadyInList = false;
                        for (TaskElementJson taskElementJsonListElement : taskElementJsonList) {

                            combinedId1 = taskElementJsonListElement.getItemId()+"_"+taskElementJsonListElement.getDslTemplateId();
                            combinedId2 = taskElementJson.getItemId()+"_"+taskElementJson.getDslTemplateId();

                            if (combinedId1.equals(combinedId2)){
                                elementAlreadyInList = true;
                                break;
                            }

                        }

                        //add element to list
                        if (!elementAlreadyInList) {
                            taskElementJsonList.add(taskElementJson);
                        }

                    }

                }

                //add subtask to list
                subtaskList.add(s);

            }
        }
    }

}
