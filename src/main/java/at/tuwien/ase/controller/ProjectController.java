package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.model.project.Role;
import at.tuwien.ase.model.user.User;
import at.tuwien.ase.services.ProjectService;
import at.tuwien.ase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
@RestController
public class ProjectController {

    // Creating service objects
    // @author Tomislav Nikic
    @Autowired
    private ProjectService pm;
    @Autowired
    private UserService us;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    // Creating service objects
    // @author Tomislav Nikic

    // Returning test json for testing and insight on format
    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/test", method = RequestMethod.GET)
    @ResponseBody
    public Project test() {
        Project testProject = pm.createProject("testID", "testName", "testDescription");
        User testUser = us.addUser("testEmail@test.com", "testPassword");
        testProject.addUser(testUser, Role.ADMIN);
        return testProject;
    }

    // Returning the project if project id is provided
    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Project getProject(@PathVariable("id") String id) {
        return pm.getByID(id);
    }

    // Saves provided project and returns a copy if successful
    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Project createProject(@RequestBody Project project) {
        pm.saveProject(project);
        return project;
    }

    // Updates project with provided project and returns new project (set null if no change)
    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace", method = RequestMethod.PATCH, consumes = "application/json")
    @ResponseBody
    public Project updateProject(@RequestBody Project project) {
        pm.deleteProject(project.getId());
        pm.saveProject(project);
        return project;
    }

}
