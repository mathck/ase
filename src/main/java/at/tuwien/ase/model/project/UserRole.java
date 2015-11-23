package at.tuwien.ase.model.project;

import at.tuwien.ase.model.user.User;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
public class UserRole {

    private String projectID;
    private User user;
    private Role role;

    //Default Constructor - necessary for REST POST Functionality!
    public UserRole() {}

    // @author Tomislav Nikic
    public UserRole(User user, String projectID, Role role) {
        this.user = user;
        this.projectID = projectID;
        this.role = role;
    }

    // Getter and setter for Project
    // @author Tomislav Nikic
    public String getProject() {
        return projectID;
    }
    public void setProject(String projectID) {
        this.projectID = projectID;
    }

    // Getter and setter for User
    // @author Tomislav Nikic
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    // Getter and setter for Role
    // @author Tomislav Nikic
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

}
