package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.*;
import at.tuwien.ase.services.DslTemplateService;
import at.tuwien.ase.services.ProjectService;
import at.tuwien.ase.services.TaskService;
import at.tuwien.ase.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * The controller implementation for the test data.
 *
 * @author Mateusz Czernecki
 * @version 1.0, 15.12.2015
 */

@RestController
public class TestdataController {

    private static final Logger logger = LogManager.getLogger(TestdataController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DslTemplateService dslService;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    @RequestMapping(value = "/testdata/1/generate/5796e83c-f5fa-4730-9915-a47bfcecad6d", method = RequestMethod.POST)
    @ResponseBody
    public void generateTestData1() throws Exception {

        // create principal
        User admin = new User("admin@school.com", "1234qwer");
        admin.setFirstName("Admin");
        admin.setLastName("School");
        admin.setAvatar("img/avatars/4.png");
        userService.writeUser(admin);

        // create 10.000 students
        List<User> students = new ArrayList<User>();
        for(int i = 0; i < 100; i++) {
            User currentUser = new User("student" + _userCounter + "@mail.com", "1234qwer");
            currentUser.setFirstName("user");
            currentUser.setLastName("student");
            currentUser.setAvatar("img/avatars/" + _userCounter % 30 + ".png");

            userService.writeUser(currentUser);
            students.add(currentUser);
            _userCounter++;
        }

        List<User> teachers = new ArrayList<User>();
        // create 1.000 teachers
        for(int i = 0; i < 10; i++) {

            User currentUser = new User("teacher" + _userCounter + "@mail.com", "1234qwer");
            currentUser.setFirstName("user");
            currentUser.setLastName("teacher");
            currentUser.setAvatar("img/avatars/" + _userCounter % 30 + ".png");

            userService.writeUser(currentUser);
            teachers.add(currentUser);
            _userCounter++;
        }

        String firstCode = "<template><!-- GENERAL INFORMATION --><identifier><title>Your Title</title><description>Your Description</description><estimatedWorkTime>60</estimatedWorkTime><deadline>2090-01-01</deadline><githook>false</githook></identifier><!-- CONTENT --><taskBody><h1>Nice Subtask</h1>{taskElement:1}</taskBody><!-- CONTENT ELEMENTS --><taskElements><taskElement id='1'><status>checked</status><value>angry</value><solution>checked</solution><link></link><type>checkbox</type></taskElement></taskElements></template>";

        // add all dsl templates
        for(int i = 0; i < 5; i++) {
            dslService.writeDslTemplate(new DslTemplate("userStory" + i, "description", firstCode, admin.getUserID()), "create");
        }

        // create 1.000 class projects for every teacher
        for(int i = 0; i < 10; i++) {
            Project project = new Project(i+1, "Class " + (i+1) + "A", "Class project " + (i+1));

            // 5 tasks (homeworks) for every class

            for(int j = 0; j < 5; j++) {
                LinkedList<User> users = new LinkedList<User>();
                for(int k = i * 10; k < ((i * 10) + 10); k++) {
                    users.add(students.get(k));
                }

                Task task = new Task("Open",
                        "single_task",
                        "Homework " + (i + j),
                        "Do this and do that",
                        "task",
                        new Date(),
                        new Date(),
                        project.getProjectID(),
                        teachers.get(i).getUserID(),
                        new LinkedList<TaskState>(Arrays.asList(new TaskState(0, "asdasd", 0))),
                        new LinkedList<Subtask>(Arrays.asList(new Subtask("subtask title", "description", 0, "Open", 50, new Date(), new Date(), 1))),
                        users);

                project.addTask(task);
                //taskService.writeTask(i+1, task);
            }

            // 1 admin and 1 teacher for every class
            project.addUser(admin.getUserID(), "ADMIN");
            project.addUser(teachers.get(i).getUserID(), "ADMIN");

            // 10 students for every class
            for(int k = i * 10; k < ((i * 10) + 10); k++) {
                project.addUser(students.get(k).getUserID(), "CONTRIBUTOR");
            }

            projectService.writeProject(project);
        }
    }

    private int _userCounter = 1;

    private String getMailByName(String username) {
        return username + "@" + "mail" + ".com";
    }
}
