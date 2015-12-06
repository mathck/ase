package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.UserRole;
import at.tuwien.ase.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
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
    private GenericRestExceptionHandler genericRestExceptionHandler;

    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/projects", method = RequestMethod.GET)
    @ResponseBody
    public Project getProject(@RequestParam("pID") int pID) throws EmptyResultDataAccessException {
        return ps.getByID(pID);
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/projects", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public int createProject(@RequestBody Project project) {
        project.setCreationTime(new Timestamp(new Date().getTime()));
        project.setUpdateTime(project.getCreationTime());
        return ps.writeProject(project);
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/projects", method = RequestMethod.PATCH, consumes = "application/json")
    @ResponseBody
    public void updateProject(@RequestParam("pID") int pID, @RequestBody Project project) {
        ps.updateProject(pID, project);
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/projects/user", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Project> getProjectsFromUser(@RequestParam("uID") String uID) throws EmptyResultDataAccessException {
        return ps.getAllProjectsFromUser(uID);
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/projects/all", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Project> getAllProjects() throws EmptyResultDataAccessException {
        return ps.getAllProjects();
    }

    // @author Tomislav Nikic
    @RequestMapping(value = "/workspace/projects/add", method = RequestMethod.PUT)
    @ResponseBody
    public void addUserToProject(@RequestBody UserRole user) {
        ps.addUser(user.getProject(), user.getUser(), user.getRole());
    }

    @RequestMapping(value = "/workspace/projects/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public void removeUserFromProject(@RequestParam("uID") String uID, @RequestParam("pID") int pID) {
        ps.removeUser(pID, uID);
    }

}
