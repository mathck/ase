package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.UserRole;
import at.tuwien.ase.services.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.LinkedList;
import java.util.Set;

/**
 * The controller implementation for project management.
 *
 * @author Tomislav Nikic
 * @version 1.0, 14.12.2015
 */
@RestController
public class ProjectController {

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

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
    @RequestMapping(value = "/workspace/projects", method = RequestMethod.GET)
    @ResponseBody
    public Project getProject(@RequestParam("pID") int pID, @RequestParam("uID") String uID) throws Exception {
        return projectService.getByID(pID, uID);
    }

    /**
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "/workspace/projects", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public JsonStringWrapper createProject(@RequestBody Project project) throws Exception {
        Set<ConstraintViolation<Project>> constraintViolations = validator.validate(project);
        if(constraintViolations.size() == 0) {
            return projectService.writeProject(project);
        } else {
            throw new ValidationException("Data has failed validation test!");
        }
    }

    /**
     *
     * @param pID
     * @param project
     */
    @RequestMapping(value = "/workspace/projects", method = RequestMethod.PATCH, consumes = "application/json")
    @ResponseBody
    public void updateProject(@RequestParam("pID") int pID, @RequestBody Project project) throws Exception {
        Set<ConstraintViolation<Project>> constraintViolations = validator.validate(project);
        if(constraintViolations.size() == 0) {
            projectService.updateProject(pID, project);
        } else {
            throw new ValidationException("Data has failed validation test!");
        }
    }

    /**
     *
     * @param uID
     * @return
     * @throws EmptyResultDataAccessException
     */
    @RequestMapping(value = "/workspace/projects/user", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Project> getProjectsFromUser(@RequestParam("uID") String uID) throws EmptyResultDataAccessException {
        return projectService.getAllProjectsFromUser(uID);
    }

    /**
     *
     * @return
     * @throws EmptyResultDataAccessException
     */
    @RequestMapping(value = "/workspace/projects/all", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Project> getAllProjects() throws EmptyResultDataAccessException {
        return projectService.getAllProjects();
    }

    /**
     *
     * @param user
     */
    @RequestMapping(value = "/workspace/projects/add", method = RequestMethod.PUT)
    @ResponseBody
    public void addUserToProject(@RequestBody UserRole user) throws Exception {
        Set<ConstraintViolation<UserRole>> constraintViolations = validator.validate(user);
        if(constraintViolations.size() == 0) {
            projectService.addUser(user.getProject(), user.getUser(), user.getRole());
        } else {
            throw new ValidationException("Data has failed validation test!");
        }
    }

    /**
     *
     * @param uID
     * @param pID
     */
    @RequestMapping(value = "/workspace/projects/remove/{pID}", method = RequestMethod.DELETE)
    @ResponseBody
    public void removeUserFromProject(@RequestParam("uID") String uID, @PathVariable("pID") int pID) {
        projectService.removeUser(pID, uID);
    }

}
