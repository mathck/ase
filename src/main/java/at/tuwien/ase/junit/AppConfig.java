package at.tuwien.ase.junit;

/**
 * Created by mathc_000 on 22-Nov-15.
 */
import at.tuwien.ase.dao.task.IssueDAO;
import at.tuwien.ase.dao.task.ProjectDAO;
import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.dao.task.UserDAO;
import at.tuwien.ase.dao.task.impl.IssueDAOImpl;
import at.tuwien.ase.dao.task.impl.ProjectDAOImpl;
import at.tuwien.ase.dao.task.impl.TaskDAOImpl;
import at.tuwien.ase.dao.task.impl.UserDAOImpl;
import at.tuwien.ase.services.ProjectService;
import at.tuwien.ase.services.UserService;
import at.tuwien.ase.services.impl.ProjectServiceImpl;
import at.tuwien.ase.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    @Qualifier("dataSource")
    public DataSource getDataSource() {
        return new DataSourceMock();
    }

    @Bean
    @Qualifier("userDAO")
    public UserDAO getUserDao() {
        return new UserDAOImpl();
    }

    @Bean
    @Qualifier("projectDAO")
    public ProjectDAO getProjectDao() {
        return new ProjectDAOImpl();
    }

    @Bean
    @Qualifier("issueDAO")
    public IssueDAO getIssueDao() {
        return new IssueDAOImpl();
    }

    @Bean
    @Qualifier("taskDAO")
    public TaskDAO getTaskDao() {
        return new TaskDAOImpl();
    }

    @Bean
    @Qualifier("projectService")
    public ProjectService getProjectService() {
        return new ProjectServiceImpl();
    }

    @Bean
    @Qualifier("userService")
    public UserService getUserService() {
        return new UserServiceImpl();
    }
}