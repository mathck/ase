package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.model.project.Role;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.model.user.User;
import at.tuwien.ase.services.ProjectService;
import at.tuwien.ase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
@RestController
public class ProjectController {

    // @author Tomislav Nikic
    @Autowired
    private ProjectService ps;
    @Autowired
    private UserService us;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/projects/test", method = RequestMethod.GET)
    @ResponseBody
    public Project test() throws Exception {
        Project testProject = new Project("testID", "testName", "testDescription");
        User testUser = new User("testEmail@test.com", "testPassword");
        testProject.addUser(testUser, Role.ADMIN);
        return testProject;
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/projects/{pID}", method = RequestMethod.GET)
    @ResponseBody
    public Project getProject(@PathVariable("pID") String pID) {
        return ps.getByID(pID);
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/projects/{uID}", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Project createProject(@PathVariable("uID") String uID, @RequestParam(value = "role") Role role, @RequestBody Project project) {
        ps.writeProject(project);
        ps.addUser(project.getId(), us.getByID(uID), role);
        return ps.getByID(project.getId());
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/projects", method = RequestMethod.PATCH, consumes = "application/json")
    @ResponseBody
    public Project updateProject(@RequestBody Project project) {
        ps.deleteProject(project.getId());
        return ps.writeProject(project);
    }

}
