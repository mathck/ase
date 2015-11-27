package at.tuwien.ase.model.project;

import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
public class Project
{

	// Info
	private int pID;
	private String title;
	private String description;

	// Time
	private Timestamp creationTime;
	private Timestamp updateTime;

	// Lists
	private LinkedList<UserRole> userList;
	private LinkedList<Task> taskList;
	private LinkedList<Issue> issueList;

	// Constructors
	// @author Tomislav Nikic
	public Project()
	{
	}

	public Project(int pID, String title, String description)
	{
		this.pID = pID;
		this.title = title;
		this.description = description;

		Date time = new Date();
		creationTime = new Timestamp(time.getTime());
		updateTime = creationTime;

		userList = new LinkedList<UserRole>();
		taskList = new LinkedList<Task>();
		issueList = new LinkedList<Issue>();
	}

	// Getter and setter for project ID
	// @author Tomislav Nikic
	public int getProjectID()
	{
		return pID;
	}

	public void setProjectID(int pID)
	{
		this.pID = pID;
	}

	// Getter and setter for title
	// @author Tomislav Nikic
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	// Getter and setter for description
	// @author Tomislav Nikic
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	// Add/delete a user to/from the list
	// @author Tomislav Nikic
	public void addUser(String uID, String role)
	{
		UserRole newUser = new UserRole(uID, this.pID, role);
		userList.add(newUser);
	}

	public void deleteUser(String uID)
	{
		for (UserRole iteration : userList)
		{
			if (iteration.getUser() == uID)
				userList.remove(iteration);
		}
	}

	public void setAllUser(LinkedList<UserRole> userList)
	{
		this.userList = userList;
	}

	// Reading lists
	// @author Tomislav Nikic
	public LinkedList<UserRole> getAllUser()
	{
		return userList;
	}

	public UserRole getUserRole(String uID)
	{
		for (UserRole iterator : userList)
			if (iterator.getUser() == uID)
				return iterator;
		return null;
	}

	// Add/delete a task to/from the list
	// @author Tomislav Nikic
	public void addTask(Task task)
	{
		taskList.add(task);
	}

	public void deleteTask(int id)
	{
		for (Task iterator : taskList)
			if (iterator.getId() == id)
				taskList.remove(iterator);
	}

	public void setAllTasks(LinkedList<Task> taskList)
	{
		this.taskList = taskList;
	}

	// Get list of all tasks
	// @author Tomislav Nikic
	public LinkedList<Task> getAllTasks()
	{
		return taskList;
	}

	// Add/delete a issue to/from the list
	// @author Tomislav Nikic
	public void addIssue(Issue issue)
	{
		issueList.add(issue);
	}

	public void deleteIssue(int id)
	{
		for (Issue iterator : issueList)
			if (iterator.getId() == id)
				issueList.remove(iterator);
	}

	public void setAllIssues(LinkedList<Issue> issueList)
	{
		this.issueList = issueList;
	}

	// Get list of all issues
	// @author Tomislav Nikic
	public LinkedList<Issue> getAllIssues()
	{
		return issueList;
	}

	public Timestamp getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime)
	{
		this.creationTime = creationTime;
	}

	public Timestamp getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}
}
