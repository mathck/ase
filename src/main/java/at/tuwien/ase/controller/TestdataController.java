package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.User;
import at.tuwien.ase.services.ProjectService;
import at.tuwien.ase.services.TaskService;
import at.tuwien.ase.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

        // create 10000 students
        List<User> students = new ArrayList<User>();
        for(int i = 0; i < 10000; i++) {
            User currentUser = new User("user" + _userCounter, "1234qwer");
            currentUser.setFirstName("user");
            currentUser.setLastName("" + _userCounter);
            currentUser.setAvatar("img/avatars/" + _userCounter % 30 + ".png");

            userService.writeUser(currentUser);
            students.add(currentUser);
            _userCounter++;
        }

        List<User> teachers = new ArrayList<User>();
        // create 1000 teachers
        for(int i = 0; i < 1000; i++) {

            User currentUser = new User("user" + _userCounter, "1234qwer");
            currentUser.setFirstName("user");
            currentUser.setLastName("" + _userCounter);
            currentUser.setAvatar("img/avatars/" + _userCounter % 30 + ".png");

            userService.writeUser(currentUser);
            teachers.add(currentUser);
            _userCounter++;
        }

        // create 1000 class projects for every teacher
        for(int i = 0; i < 1000; i++) {
            Project project = new Project(i+1, "Class " + (i+1), "Class project for class " + (i+1));

            // 5 tasks (homeworks) for every class
            /*
            for(int j = 0; j < 5; j++)
                project.addTask(new Task("Open",
                        "Homework " + (i+j),
                        "Do this and do that",
                        "task",
                        new Date(),
                        new Date(),
                        project.getProjectID(),
                        teachers.get(i).getUserID()));
            //*/

            // 1 admin and 1 teacher for every class
            project.addUser(admin.getUserID(), "ADMIN");
            project.addUser(teachers.get(i).getUserID(), "ADMIN");

            // 100 students for every class
            for(int k = i * 100; k < ((i * 100) + 100); k++) {
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
