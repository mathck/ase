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
import at.tuwien.ase.services.ProjectService;
import at.tuwien.ase.services.impl.ProjectServiceImpl;
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

    // @author: Mateusz Czernecki
    @Test
    public void whenProjectIsDeleted_DeleteAllTasksAndIssuesToo() {

        // Arrange
        final int pID = 0;
        ProjectDAO projectDAO = Mockito.mock(ProjectDAO.class);
        IssueDAO issueDAO = Mockito.mock(IssueDAO.class);
        TaskDAO taskDAO = Mockito.mock(TaskDAO.class);
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        SubtaskDAO subtaskDAO = Mockito.mock(SubtaskDAO.class);
        ProjectService projectService = new ProjectServiceImpl(projectDAO, issueDAO, taskDAO, userDAO, subtaskDAO);
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
}