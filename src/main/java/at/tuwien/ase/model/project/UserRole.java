package at.tuwien.ase.model.project;

import at.tuwien.ase.model.user.User;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
public class UserRole {

    private String projectID;
    private String uID;
    private Role role;

    //Default Constructor - necessary for REST POST Functionality!
    public UserRole() {}

    // @author Tomislav Nikic
    public UserRole(String uID, String projectID, Role role) {
        this.uID = uID;
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
    public String getUser() {
        return uID;
    }
    public void setUser(String uID) {
        this.uID = uID;
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
