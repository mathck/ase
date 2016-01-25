package at.tuwien.ase.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A model class describing the user-project-role relation object. It contains a
 * string containing the role of the user in this particular project.
 *
 * @author Tomislav Nikic
 * @version 1.0, 13.12.2015
 */
public class UserRole {

    private int projectID;
    @NotNull
    @Size(min = 2)
    private String userID;
    @NotNull
    @Size(min = 7)
    private String role;

    public UserRole() {}

    /**
     * Constructor for creating a user-project-role relation object. It connects these
     * three parts by using the corresponding id.
     *
     * @param userID The user email that is stored in the database.
     * @param projectID The project ID that is created during creation.
     * @param role The role of the user, specified in userID, in the project,
     *             specified in projectID.
     */
    public UserRole(String userID, int projectID, String role) {
        this.userID = userID;
        this.projectID = projectID;
        this.role = role;
    }


    public int getProject() {
        return projectID;
    }

    public void setProject(int pID) {
        this.projectID = pID;
    }

    public String getUser() {
        return userID;
    }

    public void setUser(String uID) {
        this.userID = uID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
