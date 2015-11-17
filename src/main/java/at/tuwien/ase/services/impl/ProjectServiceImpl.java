package at.tuwien.ase.services.impl;

import at.tuwien.ase.model.project.Project;
import at.tuwien.ase.services.ProjectService;

import java.util.LinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LogManager.getLogger(ProjectServiceImpl.class);

    @Override
    public Project createProject(String id, String title, String description) {
        Project project = new Project(id, title, description);
        //TODO Write to db
        return project;
    }

    @Override
    public boolean saveProject(Project project) {
        //TODO Write to db
        return true;
    }

    @Override
    public boolean deleteProject(String id) {
        //TODO Remove from db
        return true;
    }

    public Project getByID(String id) {
        //TODO Load from db
        return null;
    }

    @Override
    public LinkedList<Project> getAllProjects() {
        //TODO Fetch all from db
        return null;
    }

    @Override
    public LinkedList<Project> getAllProjectsFromUser(String email) {
        //TODO Fetch user projects from db
        return null;
    }



}
