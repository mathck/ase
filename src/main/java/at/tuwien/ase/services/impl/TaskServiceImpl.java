package at.tuwien.ase.services.impl;

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
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

    private static final Logger logger = LogManager.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl() {
    }

    public TaskServiceImpl(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public Task getByID(int tID) {
        logger.debug("get task with id=" + tID);
        return taskDAO.findByID(tID);
    }

    public void writeTask(int pID, Task task) throws Exception{

        logger.debug("post new task");

        if (task.getTaskStates() == null ||  task.getTaskStates().isEmpty()
                || task.getSubtaskList() == null ||  task.getSubtaskList().isEmpty()
                || task.getExecutionType() == null || task.getExecutionType().equals("")){
            throw new Exception("not all input values are present!");
        }

        if (task.getExecutionType().equals("collaborative_task")) {

            writeTaskAndSubtask(pID, task);

        }else{

            if (task.getExecutionType().equals("single_task")) {

                String taskTitle = task.getTitle().trim();
                if (task.getUserList() != null && !task.getUserList().isEmpty()) {
                    for (User u : task.getUserList()) {
                        task.setTitle(u.getFirstName() + ": " + taskTitle);
                        writeTaskAndSubtask(pID, task);

                    }
                }

            }else{
                throw new Exception("execution type not supported");
            }
        }

    }



    public void deleteTaskByID(int tID) {
        logger.debug("delete task with id="+tID);
        taskDAO.removeTaskByID(tID);
    }

    public LinkedList<Task> getAllTasks() {
        logger.debug("get all tasks");
        return taskDAO.loadAll();
    }

    public LinkedList<Task> getAllTasksFromUser(String uID) {
        logger.debug("get all tasks from user"+uID);
        return taskDAO.loadAllByUser(uID);
    }

    public LinkedList<Task> getAllTasksFromProject(int pID) {
        logger.debug("get all tasks from project");
        return taskDAO.loadAllByProject(pID);
    }

    public LinkedList<Task> getAllTasksFromProjectAndUser(int pID, String uID) {
        logger.debug("get all tasks from project " + pID + " and from user " + uID);
        return taskDAO.loadAllByProjectAndUser(pID, uID);
    }

    public void assignUserToTask(int tID, String uID) {
        logger.debug("assign user with id="+uID+" to task with id="+tID);
        taskDAO.assignUserToTask(tID, uID);
    }

    public void removeUserFromTask(int tID, String uID) {
        logger.debug("remove user with id="+uID+" from task with id="+tID);
        taskDAO.removeUserFromTask(tID, uID);
    }


    private void writeTaskAndSubtask(int pID, Task task) throws Exception {

        int taskId;
        DslTemplate dslTemplate;
        Template template;
        String taskBody;
        List<TaskElement> taskElementList;
        TaskElementJson taskElementJson;

        taskId = taskDAO.getNewID();
        task.setId(taskId);
        //first state in state list is start state for task
        task.setStatus(((TaskState) (task.getTaskStates().get(0))).getStateName());
        task.setCreationDate(new Date());
        task.setUpdateDate(new Date());

        //insert task to db
        taskDAO.insertTask(pID, task);

        //add contributors to task
        if (task.getUserList() != null && !task.getUserList().isEmpty()) {
            for (User u : task.getUserList()) {
                //assign user to task
                taskDAO.addUserToTask(u.getUserID(), task.getId());
            }
        }

        //add states to task state list
        if (task.getTaskStates() != null && !task.getTaskStates().isEmpty()) {
            for (TaskState s : task.getTaskStates()) {
                //add state to state list
                taskDAO.addStateToTaskStates(s, task.getId());
            }
        }

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
                subtask.setTaskBody(taskBody);
                subtask.setId(subtaskDAO.getNewID());
                subtask.setTaskId(taskId);
                subtask.setTitle(template.getIdentifier().getTitle());
                subtask.setDescription(template.getIdentifier().getDescription());
                //s.setStatus(); // TODO
                subtask.setXp(template.getIdentifier().getEstimatedWorkTime().intValue());
                subtask.setCreationDate(new Date());
                subtask.setUpdateDate(new Date());

                //add subtask to db
                subtaskDAO.insertSubtask(subtask);

                //get task elements from tempalte and add it to subtask
                if (template.getTaskElements() != null && template.getTaskElements().getTaskElement() != null
                        && !template.getTaskElements().getTaskElement().isEmpty()) {

                    taskElementList = template.getTaskElements().getTaskElement();
                    for (TaskElement taskElement : taskElementList) {

                        taskElementJson = TaskElementJsonFactory.getTaskElement(taskElement);

                        //db insert: add task element to subtask
                        taskElementJson.setId(subtaskDAO.getNewIDForTaskItem());
                        subtaskDAO.addTaskItemToSubtask(taskElementJson, subtask.getId());

                    }

                }
            }
        }

    }

}
