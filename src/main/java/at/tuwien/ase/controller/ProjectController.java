package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.UserRole;
import at.tuwien.ase.services.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * The controller implementation for project management.
 *
 * @author Tomislav Nikic
 * @version 1.0, 14.12.2015
 */
@RestController
@RequestMapping("/api/")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    /**
     *
     * @param pID
     * @param uID
     * @return
     * @throws EmptyResultDataAccessException
     */
    @RequestMapping(value = "workspace/projects", method = RequestMethod.GET)
    @ResponseBody
    public Project getProject(@RequestParam("pID") int pID, @RequestParam("uID") String uID) throws Exception {
        return projectService.getByID(pID, uID);
    }

    /**
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "workspace/projects", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public JsonStringWrapper createProject(@RequestBody Project project) throws Exception {
        return projectService.writeProject(project);
    }

    /**
     *
     * @param pID
     * @param project
     */
    @RequestMapping(value = "workspace/projects", method = RequestMethod.PATCH, consumes = "application/json")
    @ResponseBody
    public void updateProject(@RequestParam("pID") int pID, @RequestBody Project project) throws Exception {
        projectService.updateProject(pID, project);
    }

    /**
     *
     * @param uID
     * @return
     * @throws EmptyResultDataAccessException
     */
    @RequestMapping(value = "workspace/projects/user", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Project> getProjectsFromUser(@RequestParam("uID") String uID) throws EmptyResultDataAccessException {
        return projectService.getAllProjectsFromUser(uID);
    }

    /**
     *
     * @return
     * @throws EmptyResultDataAccessException
     */
    @RequestMapping(value = "workspace/projects/all", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Project> getAllProjects() throws EmptyResultDataAccessException {
        return projectService.getAllProjects();
    }

    /**
     *
     * @param user
     */
    @RequestMapping(value = "workspace/projects/add", method = RequestMethod.PUT)
    @ResponseBody
    public void addUserToProject(@RequestBody UserRole user) throws Exception {
        projectService.addUser(user.getProject(), user.getUser(), user.getRole());
    }

    /**
     *
     * @param uID
     * @param pID
     */
    @RequestMapping(value = "workspace/projects/remove/{pID}", method = RequestMethod.DELETE)
    @ResponseBody
    public void removeUserFromProject(@RequestParam("uID") String uID, @PathVariable("pID") int pID) {
        projectService.removeUser(pID, uID);
    }

}
