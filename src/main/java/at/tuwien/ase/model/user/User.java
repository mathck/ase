package at.tuwien.ase.model.user;

import at.tuwien.ase.controller.PasswordEncryption;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 05/11/2015.
 */
public class User {

    // Info
    private String firstName;
    private String lastName;
    private String email;

    // Front-end variable
    private String avatar;

    // Lists
    private LinkedList<String> projectList;

    // Security
    private byte[] password;
    private byte[] salt;

    // @author Tomislav Nikic
    public User(String email, String password) {
        this.email = email;
        salt = PasswordEncryption.generateSalt();
        this.password = PasswordEncryption.getEncryptedPassword(password, salt);
    }

    // Getter and setter for Email
    // @author Tomislav Nikic
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for First Name
    // @author Tomislav Nikic
    public String getFirstName() {
        return firstName;
    }
    public void setName(String givenName) {
        this.firstName = givenName;
    }

    // Getter and setter for Last Name
    // @author Tomislav Nikic
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter and setter for Avatar
    // @author Tomislav Nikic
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // Getter and setter for Password
    // @author Tomislav Nikic
    public byte[] getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = PasswordEncryption.getEncryptedPassword(password, salt);
    }

    // Get all projects
    // @author Tomislav Nikic
    public LinkedList<String> getAllProjects() {
        return projectList;
    }

    // Save to file for testing
    // @author Tomislav Nikic
    public void toFile() {
        try {
            PrintWriter out = new PrintWriter(email + ".txt");
            out.println("email: " + email);
            out.println("firstName: " + firstName);
            out.println("lastName: " + lastName);
            out.println("encrypted password: " + password.toString());
            out.println("projects: " + projectList.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
