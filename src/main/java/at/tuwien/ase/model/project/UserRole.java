package at.tuwien.ase.model.project;

/**
 * Created by Tomislav Nikic on 16/11/2015.
 */
public class UserRole {

    private int pID;
    private String uID;
    private String role;

    //Default Constructor - necessary for REST POST Functionality!
    public UserRole() {}

    // @author Tomislav Nikic
    public UserRole(String uID, int pID, String role)
    {
        this.uID = uID;
        this.pID = pID;
        this.role = role;
    }

    // Getter and setter for Project
    // @author Tomislav Nikic
    public int getProject()
    {
        return pID;
    }

    public void setProject(int pID)
    {
        this.pID = pID;
    }

    // Getter and setter for User
    // @author Tomislav Nikic
    public String getUser()
    {
        return uID;
    }

    public void setUser(String uID)
    {
        this.uID = uID;
    }

    // Getter and setter for Role
    // @author Tomislav Nikic
    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

}
