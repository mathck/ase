package at.tuwien.ase;

/**
 * Created by mathc_000 on 22-Nov-15.
 */

import at.tuwien.ase.dao.task.IssueDAO;
import at.tuwien.ase.dao.task.ProjectDAO;
import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.dao.task.UserDAO;
import at.tuwien.ase.junit.AppConfig;
import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.model.user.User;
import at.tuwien.ase.services.ProjectService;
import at.tuwien.ase.services.UserService;
import at.tuwien.ase.services.impl.ProjectServiceImpl;
import at.tuwien.ase.services.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class ProjectServiceTests {

    @Test
    public void whenProjectIsDeleted_DeleteAllTasksAndIssuesToo() {

        // Arrange
        String pID = "coole id";
        ProjectDAO projectDAO = Mockito.mock(ProjectDAO.class);
        IssueDAO issueDAO = Mockito.mock(IssueDAO.class);
        TaskDAO taskDAO = Mockito.mock(TaskDAO.class);
        ProjectService projectService = new ProjectServiceImpl(projectDAO, issueDAO, taskDAO);
        Project project = new Project("pika", "title", "desc");
        Issue issue = new Issue(); issue.setId(4);
        Task task = new Task(); task.setId(5);
        project.addIssue(issue);
        project.addTask(task);
        when(projectDAO.findByID(pID)).thenReturn(project);

        // Act
        projectService.deleteProject(pID);

        // Assert
        verify(taskDAO, atLeastOnce()).removeTask(task.getId());
        verify(issueDAO, atLeastOnce()).removeIssue(issue.getId());
        verify(projectDAO, atLeastOnce()).removeProject(pID);
    }
}