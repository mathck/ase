package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.*;
import at.tuwien.ase.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private IssueService issueService;

    @Autowired
    private DslTemplateService dslService;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    @RequestMapping(value = "/testdata/all/generate/5796e83c-f5fa-4730-9915-a47bfcecad6d", method = RequestMethod.POST)
    @ResponseBody
    public void generateTestData() throws Exception {
        generateTestData(1);
        generateTestData(5);
    }

    @RequestMapping(value = "/testdata/5/generate/5796e83c-f5fa-4730-9915-a47bfcecad6d", method = RequestMethod.POST)
    @ResponseBody
    public void generateTestData5() throws Exception {
        generateTestData(5);
    }

    @RequestMapping(value = "/testdata/1/generate/5796e83c-f5fa-4730-9915-a47bfcecad6d", method = RequestMethod.POST)
    @ResponseBody
    public void generateTestData1() throws Exception {
        generateTestData(1);
    }

    private void generateTestData(int number) throws Exception {

        String projectName = "";
        String userContributor = "";
        String userAdmin = "";
        String projectTitle = "";
        String taskName = "";

        switch (number) {
            case 5:
                projectName = "school";
                userContributor = "schoolkid";
                userAdmin = "teacher";
                projectTitle = "Class";
                taskName = "Homework";
                break;
            case 1:
                projectName = "ase";
                userContributor = "student";
                userAdmin = "tutor";
                projectTitle = "Project";
                taskName = "Task";
                break;
        }

        // create principal
        User admin = new User("admin@" + projectName + ".com", "1234qwer");
        admin.setFirstName("Admin");
        admin.setLastName(projectName);
        admin.setAvatar("img/avatars/4.png");
        userService.writeUser(admin);

        // create 10.000 students
        List<User> students = new ArrayList<User>();
        for(int i = 0; i < 100; i++) {
            User currentUser = new User(userContributor + _userCounter + "@mail.com", "1234qwer");
            currentUser.setFirstName("user");
            currentUser.setLastName(userContributor);
            currentUser.setAvatar("img/avatars/" + _userCounter % 30 + ".png");

            userService.writeUser(currentUser);
            students.add(currentUser);
            _userCounter++;
        }

        List<User> teachers = new ArrayList<User>();
        // create 1.000 teachers
        for(int i = 0; i < 10; i++) {

            User currentUser = new User(userAdmin + _userCounter + "@mail.com", "1234qwer");
            currentUser.setFirstName("user");
            currentUser.setLastName(userAdmin);
            currentUser.setAvatar("img/avatars/" + _userCounter % 30 + ".png");

            userService.writeUser(currentUser);
            teachers.add(currentUser);
            _userCounter++;
        }

        String workingDir = System.getProperty("user.dir");
        Path path = Paths.get(workingDir, "src", "main", "resources", "dsl", "userstory_" + number + ".xml");
        String dsl = readFile(path, StandardCharsets.UTF_8);
        dsl = dsl.replaceAll("\t", "");
        dsl = dsl.replaceAll("\n", "");
        dsl = dsl.replaceAll("\r", "");

        dslService.writeDslTemplate(new DslTemplate("userStory_" + number, "dsl for a user story", dsl, admin.getUserID()), "create");

        // create 1.000 class projects for every teacher
        for(int i = 0; i < 10; i++) {
            Project project = new Project(i+1, projectTitle + " " + (i+1), projectTitle + " number " + (i+1));

            // 1 task (homeworks) for every student
            for(int j = 0; j < 10; j++) {
                LinkedList<User> users = new LinkedList<User>();
                users.add(students.get((i * 10) + j));

                Task task = new Task("Open",
                        "single_task",
                        taskName + " " + (i + j),
                        "Do this and do that",
                        "task",
                        new Date(),
                        new Date(),
                        project.getProjectID(),
                        teachers.get(i).getUserID(),
                        new LinkedList<TaskState>(Arrays.asList(new TaskState(0, "im on it", 0))),
                        new LinkedList<Subtask>(Arrays.asList(new Subtask("subtask title", "description", 0, "Open", 50, new Date(), new Date(), 1))),
                        users);

                project.addTask(task);
                //taskService.writeTask(i+1, task);
            }

            issueService.writeIssue(new Issue("Clean Room", "The room is dirty"), i+1, students.get((i * 10)).getUserID());

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

    static String readFile(Path path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, encoding);
    }

    private int _userCounter = 1;

    private String getMailByName(String username) {
        return username + "@" + "mail" + ".com";
    }
}
