package at.tuwien.ase.dao.task.impl;

import at.tuwien.ase.dao.task.ProjectDAO;
import at.tuwien.ase.model.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 20/11/2015.
 */
@Repository
public class ProjectDAOImpl implements ProjectDAO {

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }

    public Project insertProject(Project project) {
        return null;
    }

    public boolean removeProject(String pID) {
        return false;
    }

    public Project findByID(String pID) {
        return null;
    }

    public LinkedList<Project> loadAll() {
        return null;
    }

    public LinkedList<Project> loadAllByUser(String uID) {
        return null;
    }

}
