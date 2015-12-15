package at.tuwien.ase;

/**
 * Created by mathc_000 on 22-Nov-15.
 */
import at.tuwien.ase.dao.IssueDAO;
import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.junit.AppConfig;
import at.tuwien.ase.model.*;
import at.tuwien.ase.services.*;
import at.tuwien.ase.services.impl.IssueServiceImpl;
import at.tuwien.ase.services.impl.ProjectServiceImpl;
import at.tuwien.ase.services.impl.TaskServiceImpl;
import at.tuwien.ase.services.impl.UserServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.LinkedList;

import static org.mockito.Mockito.*;

// Bei Problemen bitte pom.xml Rechtsklick -> Maven -> Reimport
// Im Notfall auskommentieren und ruhig pushen

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class ProjectServiceTests {

    static UserDAO userDAO;
    static ProjectDAO projectDAO;
    static SubtaskDAO subtaskDAO;
    static IssueDAO issueDAO;
    static TaskDAO taskDAO;
    static IssueService issueService;
    static TaskService taskService;
    static ProjectService projectService;

    @BeforeClass
    public static void setUpClass() {
        //executed only once, before the first test
        userDAO = Mockito.mock(UserDAO.class);
        projectDAO = Mockito.mock(ProjectDAO.class);
        subtaskDAO = Mockito.mock(SubtaskDAO.class);
        issueDAO = Mockito.mock(IssueDAO.class);
        taskDAO = Mockito.mock(TaskDAO.class);
        issueService = new IssueServiceImpl(issueDAO, taskDAO);
        taskService = new TaskServiceImpl(taskDAO);
        projectService = new ProjectServiceImpl(projectDAO, issueDAO, taskDAO, userDAO, subtaskDAO, issueService, taskService);

        when(issueDAO.getNewID()).thenReturn(50);
        when(taskDAO.getNewID()).thenReturn(50);
    }

    // @author: Mateusz Czernecki
    @Test
    public void whenProjectIsDeleted_DeleteAllTasksAndIssuesToo() {

        // Arrange
        final int pID = 0;
        Project project = new Project(0, "title", "desc");
        final Issue issue = new Issue(); issue.setId(4);
        final Task task = new Task(); task.setId(5);
        project.addIssue(issue);
        project.addTask(task);
        when(projectDAO.findByID(pID)).thenReturn(project);
        when(userDAO.loadAllByProject(pID)).thenReturn(new LinkedList<UserRole>() {{add(new UserRole("a", pID, "ADMIN"));}});
        when(issueDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Issue>() {{add(issue);}});
        when(taskDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Task>() {{add(task);}});

        // Act
        projectService.deleteProject(pID);

        // Assert
        verify(taskDAO, atLeastOnce()).removeTaskByID(task.getId());
        verify(issueDAO, atLeastOnce()).removeIssueByID(issue.getId());
        verify(projectDAO, atLeastOnce()).removeProject(pID);
    }

    // @author: Mateusz Czernecki
    @Test
    public void writeProject_working() {

        // Arrange
        final int pID = 0;
        Project project = new Project(0, "title", "desc");
        final Issue issue = new Issue(); issue.setId(4);
        final Task task = new Task(); task.setId(5);
        project.addIssue(issue);
        project.addTask(task);
        when(projectDAO.findByID(pID)).thenReturn(project);
        when(userDAO.loadAllByProject(pID)).thenReturn(new LinkedList<UserRole>() {{add(new UserRole("a", pID, "ADMIN"));}});
        when(issueDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Issue>() {{add(issue);}});
        when(taskDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Task>() {{add(task);}});

        // Act
        projectService.writeProject(project);

        // Assert
        verify(projectDAO, times(1)).insertProject(project);
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void writeProject_invalidTitle() {

        // Arrange
        final int pID = 0;
        Project project = new Project(0, "t", "desc");
        final Issue issue = new Issue(); issue.setId(4);
        final Task task = new Task(); task.setId(5);
        project.addIssue(issue);
        project.addTask(task);
        when(projectDAO.findByID(pID)).thenReturn(project);
        when(userDAO.loadAllByProject(pID)).thenReturn(new LinkedList<UserRole>() {{add(new UserRole("a", pID, "ADMIN"));}});
        when(issueDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Issue>() {{add(issue);}});
        when(taskDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Task>() {{add(task);}});

        // Act
        projectService.writeProject(project);

        // Assert
        verify(projectDAO, times(0)).insertProject(project);
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void writeProject_invalidDesc() {

        // Arrange
        final int pID = 0;
        Project project = new Project(0, "title", "d");
        final Issue issue = new Issue(); issue.setId(4);
        final Task task = new Task(); task.setId(5);
        project.addIssue(issue);
        project.addTask(task);
        when(projectDAO.findByID(pID)).thenReturn(project);
        when(userDAO.loadAllByProject(pID)).thenReturn(new LinkedList<UserRole>() {{add(new UserRole("a", pID, "ADMIN"));}});
        when(issueDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Issue>() {{add(issue);}});
        when(taskDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Task>() {{add(task);}});

        // Act
        projectService.writeProject(project);

        // Assert
        verify(projectDAO, times(0)).insertProject(project);
    }

    // @author: Mateusz Czernecki
    @Test
    public void updateProject_working() {

        // Arrange
        final int pID = 0;
        Project project = new Project(0, "title", "desc");
        final Issue issue = new Issue(); issue.setId(4);
        final Task task = new Task(); task.setId(5);
        project.addIssue(issue);
        project.addTask(task);
        when(projectDAO.findByID(pID)).thenReturn(project);
        when(userDAO.loadAllByProject(pID)).thenReturn(new LinkedList<UserRole>() {{add(new UserRole("a", pID, "ADMIN"));}});
        when(issueDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Issue>() {{add(issue);}});
        when(taskDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Task>() {{add(task);}});

        // Act
        projectService.updateProject(pID, project);

        // Assert
        verify(projectDAO, times(1)).updateProject(pID, project);
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void updateProject_invalidTitle() {

        // Arrange
        final int pID = 0;
        Project project = new Project(0, "t", "desc");
        final Issue issue = new Issue(); issue.setId(4);
        final Task task = new Task(); task.setId(5);
        project.addIssue(issue);
        project.addTask(task);
        when(projectDAO.findByID(pID)).thenReturn(project);
        when(userDAO.loadAllByProject(pID)).thenReturn(new LinkedList<UserRole>() {{add(new UserRole("a", pID, "ADMIN"));}});
        when(issueDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Issue>() {{add(issue);}});
        when(taskDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Task>() {{add(task);}});

        // Act
        projectService.updateProject(pID, project);

        // Assert
        verify(projectDAO, times(0)).updateProject(pID, project);
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void updateProject_invalidDesc() {

        // Arrange
        final int pID = 0;
        Project project = new Project(0, "title", "d");
        final Issue issue = new Issue(); issue.setId(4);
        final Task task = new Task(); task.setId(5);
        project.addIssue(issue);
        project.addTask(task);
        when(projectDAO.findByID(pID)).thenReturn(project);
        when(userDAO.loadAllByProject(pID)).thenReturn(new LinkedList<UserRole>() {{add(new UserRole("a", pID, "ADMIN"));}});
        when(issueDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Issue>() {{add(issue);}});
        when(taskDAO.loadAllByProject(pID)).thenReturn(new LinkedList<Task>() {{add(task);}});

        // Act
        projectService.updateProject(pID, project);

        // Assert
        verify(projectDAO, times(0)).updateProject(pID, project);
    }
}