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

    public Task getByID(int tID) throws Exception {
        logger.debug("get task with id=" + tID);
        Task task =  taskDAO.findByID(tID);

        //get subtasks

        LinkedList<Subtask> subtaskList = subtaskDAO.loadAllByTask(task.getId());

        if (subtaskList != null){
            task.setSubtaskList(subtaskList);
        }

        return task;
    }

    public void writeTask(int pID, Task task) throws Exception{

        logger.debug("post new task");

        LinkedList<TaskElementJson> taskElementJsonList = new LinkedList<TaskElementJson>();
        LinkedList<Subtask> subtaskList = new LinkedList<Subtask>();
        LinkedList<Task> taskList = new LinkedList<Task>();
        Task t;

        if (task.getTaskStates() == null ||  task.getTaskStates().isEmpty()
                || task.getSubtaskList() == null ||  task.getSubtaskList().isEmpty()
                || task.getExecutionType() == null || task.getExecutionType().equals("")){
            throw new Exception("not all input values are present!");
        }


        //create task list

        if (task.getExecutionType().equals("collaborative_task")) {
            int taskId = taskDAO.getNewID();
            task.setId(taskId);
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

                        int taskId = taskDAO.getNewID();
                        t.setId(taskId);
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

        //create subtask and subtask elements
        createSubtasks(task, taskList, taskElementJsonList, subtaskList);

        //insert tasks
        taskDAO.insertTaskBatch(pID, taskList);

        //add contributors to task
        taskDAO.assignUserToTaskBatch(task.getUserList(), taskList);

        //add states to task state list
        taskDAO.addStateToTaskStatesBatch(task.getTaskStates(), taskList);

        //store subtasks and subtask elements to db
        subtaskDAO.insertSubtaskBatch(subtaskList);

        //add task elements to subtasks
        subtaskDAO.addTaskItemToSubtaskBatch(taskElementJsonList);

    }

    public void deleteTaskByID(int tID) throws Exception {
        logger.debug("delete task with id="+tID);
        taskDAO.removeTaskByID(tID);
    }

    public LinkedList<Task> getAllTasks() throws Exception {
        logger.debug("get all tasks");
        return taskDAO.loadAll();
    }

    public LinkedList<Task> getAllTasksFromUser(String uID) throws Exception {
        logger.debug("get all tasks from user"+uID);
        return taskDAO.loadAllByUser(uID);
    }

    public LinkedList<Task> getAllTasksFromProject(int pID) throws Exception {
        logger.debug("get all tasks from project");
        return taskDAO.loadAllByProject(pID);
    }

    public LinkedList<Task> getAllTasksFromProjectAndUser(int pID, String uID) throws Exception {
        logger.debug("get all tasks from project " + pID + " and from user " + uID);
        return taskDAO.loadAllByProjectAndUser(pID, uID);
    }

    public void assignUserToTask(int tID, String uID) throws Exception {
        logger.debug("assign user with id="+uID+" to task with id="+tID);
        taskDAO.assignUserToTask(tID, uID);
    }

    public void removeUserFromTask(int tID, String uID) throws Exception {
        logger.debug("remove user with id="+uID+" from task with id="+tID);
        taskDAO.removeUserFromTask(tID, uID);
    }

    public JsonStringWrapper addCommentToTask(int tID, Comment comment) throws Exception {
        logger.debug("add comment to task with id="+tID);

        int id;

        id = taskDAO.getNewIDForComments();
        comment.setId(id);
        taskDAO.addCommentToTask(tID, comment);

        return new JsonStringWrapper(id);
    }

    public void deleteCommentFromTask(int tID, int cID) throws Exception {
        logger.debug("remove comment with id="+cID+" from task with id="+tID);
        taskDAO.removeCommentFromTask(tID, cID);
    }


    private void createSubtasks(Task task, LinkedList<Task> taskList, LinkedList<TaskElementJson> taskElementJsonList, LinkedList<Subtask> subtaskList) throws Exception {

        //TODO performance improvement

        int taskId;
        DslTemplate dslTemplate;
        Template template;
        String taskBody;
        List<TaskElement> taskElementList;
        TaskElementJson taskElementJson;
        Task t;


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



                for (int j = 0; j < taskList.size(); j++) {

                    t = taskList.get(j);

                    Subtask s = (Subtask) subtask.clone();

                    s.setTaskBody(taskBody);
                    s.setId(subtaskDAO.getNewID());System.out.println(s.getId());
                    s.setTitle(template.getIdentifier().getTitle());
                    s.setDescription(template.getIdentifier().getDescription());
                    s.setStatus(new String("open"));
                    s.setXp(template.getIdentifier().getEstimatedWorkTime().intValue());
                    s.setTaskId(t.getId());
                    s.setCreationDate(new Date());
                    s.setUpdateDate(new Date());

                    //get task elements from tempalte and add it to subtask
                    if (template.getTaskElements() != null && template.getTaskElements().getTaskElement() != null
                            && !template.getTaskElements().getTaskElement().isEmpty()) {

                        taskElementList = template.getTaskElements().getTaskElement();
                        for (TaskElement taskElement : taskElementList) {

                            taskElementJson = TaskElementJsonFactory.getTaskElement(taskElement);

                            taskElementJson.setSubtaskId(s.getId());
                            taskElementJsonList.add(taskElementJson);

                        }
                    }

                    //add subtask to list
                    subtaskList.add(s);

                }

/*
        int taskId;
        DslTemplate dslTemplate;
        Template template;
        String taskBody;
        List<TaskElement> taskElementList;
        TaskElementJson taskElementJson;
        LinkedList<TaskElementJson> taskElementJsonList = new LinkedList<TaskElementJson>();
        LinkedList<Subtask> subtaskList = new LinkedList<Subtask>();

        taskId = taskDAO.getNewID();
        task.setId(taskId);
        //first state in state list is start state for task
        task.setStatus(((TaskState) (task.getTaskStates().get(0))).getStateName());
        task.setCreationDate(new Date());
        task.setUpdateDate(new Date());

        //insert task to db
        taskDAO.insertTask(pID, task);

        //add contributors to task
        taskDAO.assignUserToTaskBatch(task.getUserList(), task.getId());

        //add states to task state list
        taskDAO.addStateToTaskStatesBatch(task.getTaskStates(), task.getId());

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
                subtask.setStatus(new String("open"));
                subtask.setXp(template.getIdentifier().getEstimatedWorkTime().intValue());
                subtask.setCreationDate(new Date());
                subtask.setUpdateDate(new Date());

                //add subtask to list
                subtaskList.add(subtask);

                //get task elements from tempalte and add it to subtask
                if (template.getTaskElements() != null && template.getTaskElements().getTaskElement() != null
                        && !template.getTaskElements().getTaskElement().isEmpty()) {

                    taskElementList = template.getTaskElements().getTaskElement();
                    for (TaskElement taskElement : taskElementList) {

                        taskElementJson = TaskElementJsonFactory.getTaskElement(taskElement);

                        //add task element to subtask
                        taskElementJson.setId(subtaskDAO.getNewIDForTaskItem());

                        taskElementJson.setSubtaskId(subtask.getId());
                        taskElementJsonList.add(taskElementJson);

                    }
                }
            }

        }*/

            }
        }
    }

}
