package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.dao.task.IssueDAO;
import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.model.project.UserRole;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.services.ProjectService;

import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
@Service
public class ProjectServiceImpl implements ProjectService
{

	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private IssueDAO issueDAO;
	@Autowired
	private TaskDAO taskDAO;
	@Autowired
	private UserDAO userDAO;

	private static final Logger logger = LogManager.getLogger(ProjectServiceImpl.class);

	public ProjectServiceImpl()
	{

	}

	public ProjectServiceImpl(ProjectDAO projectDAO, IssueDAO issueDAO, TaskDAO taskDAO, UserDAO userDAO)
	{
		this.projectDAO = projectDAO;
		this.issueDAO = issueDAO;
		this.taskDAO = taskDAO;
		this.userDAO = userDAO;
	}

	public int writeProject(Project project)
	{
		logger.debug("Creating project with id " + project.getProjectID());
		int id = projectDAO.insertProject(project);
		if (project.getAllUser() != null && !project.getAllUser().isEmpty())
		{
			for (UserRole user : project.getAllUser())
			{
				projectDAO.addUserToProject(user.getUser(), user.getProject(), user.getRole());
			}
		}
		return id;
	}

	public void deleteProject(int pID)
	{
		Project project = projectDAO.findByID(pID);
		for (Task task : project.getAllTasks())
		{
			taskDAO.removeTask(task.getId());
		}
		for (Issue issue : project.getAllIssues())
		{
			issueDAO.removeIssue(issue.getId());
		}
		for (UserRole user : project.getAllUser())
		{
			projectDAO.removeUserFromProject(user.getUser(), project.getProjectID());
		}
		projectDAO.removeProject(pID);
	}

	public void updateProject(int pID, Project project)
	{
		projectDAO.updateProject(pID, project);
	}

	public Project getByID(int pID)
	{
		return projectDAO.findByID(pID);
	}

	public LinkedList<Project> getAllProjects()
	{
		LinkedList<Project> list = projectDAO.loadAll();
		for (Project iteration : list)
		{
			iteration.setAllUser(userDAO.loadAllByProject(iteration.getProjectID()));
		}
		return list;
	}

	public LinkedList<Project> getAllProjectsFromUser(String uID)
	{
		return projectDAO.loadAllByUser(uID);
	}

	public void addUser(int pID, String uID, String role)
	{
		projectDAO.addUserToProject(uID, pID, role);
	}

}
