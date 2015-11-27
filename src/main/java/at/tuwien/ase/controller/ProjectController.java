package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.model.project.UserRole;
import at.tuwien.ase.services.ProjectService;
import at.tuwien.ase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
@RestController
public class ProjectController
{

	// @author Tomislav Nikic
	@Autowired
	private ProjectService ps;
	@Autowired
	private UserService us;

	@Autowired
	private GenericRestExceptionHandler genericRestExceptionHandler;

	// @author Tomislav Nikic
	@RequestMapping(value = "/workspace/projects", method = RequestMethod.GET)
	@ResponseBody
	public Project getProject(@RequestParam("pID") int pID)
	{
		return ps.getByID(pID);
	}

	// @author Tomislav Nikic
	@RequestMapping(value = "/workspace/projects", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public int createProject(@RequestBody Project project)
	{
		project.setCreationTime(new Timestamp(new Date().getTime()));
		project.setUpdateTime(project.getCreationTime());
		return ps.writeProject(project);
	}

	// @author Tomislav Nikic
	@RequestMapping(value = "/workspace/projects", method = RequestMethod.PATCH, consumes = "application/json")
	@ResponseBody
	public void updateProject(@RequestParam("pID") int pID, @RequestBody Project project)
	{
		ps.updateProject(pID, project);
	}

	// @author Tomislav Nikic
	@RequestMapping(value = "/workspace/projects/user", method = RequestMethod.GET)
	@ResponseBody
	public LinkedList<Project> getProjectsFromUser(@RequestParam("uID") String uID)
	{
		return ps.getAllProjectsFromUser(uID);
	}

	// @author Tomislav Nikic
	@RequestMapping(value = "/workspace/projects/all", method = RequestMethod.GET)
	@ResponseBody
	public LinkedList<Project> getAllProjects()
	{
		return ps.getAllProjects();
	}

	// @author Tomislav Nikic
	@RequestMapping(value = "/workspace/projects/add", method = RequestMethod.PUT)
	@ResponseBody
	public void addUserToProject(@RequestBody UserRole user)
	{
		ps.addUser(user.getProject(), user.getUser(), user.getRole());
	}

}
