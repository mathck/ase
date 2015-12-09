package at.tuwien.ase;

/**
 * Created by mathc_000 on 22-Nov-15.
 */
import at.tuwien.ase.dao.IssueDAO;
import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.junit.AppConfig;
import at.tuwien.ase.model.Project;
import at.tuwien.ase.model.Issue;
import at.tuwien.ase.model.Task;
import at.tuwien.ase.services.ProjectService;
import at.tuwien.ase.services.impl.ProjectServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class ProjectServiceTests {

    @Test
    public void whenProjectIsDeleted_DeleteAllTasksAndIssuesToo() {

        // Arrange
        int pID = 0;
        ProjectDAO projectDAO = Mockito.mock(ProjectDAO.class);
        IssueDAO issueDAO = Mockito.mock(IssueDAO.class);
        TaskDAO taskDAO = Mockito.mock(TaskDAO.class);
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        ProjectService projectService = new ProjectServiceImpl(projectDAO, issueDAO, taskDAO, userDAO);
        Project project = new Project(0, "title", "desc");
        Issue issue = new Issue(); issue.setId(4);
        Task task = new Task(); task.setId(5);
        project.addIssue(issue);
        project.addTask(task);
        when(projectDAO.findByID(pID)).thenReturn(project);

        // Act
        projectService.deleteProject(pID);

        // Assert
        verify(taskDAO, atLeastOnce()).removeTaskByID(task.getId());
        verify(issueDAO, atLeastOnce()).removeIssueByID(issue.getId());
        verify(projectDAO, atLeastOnce()).removeProject(pID);
    }
}