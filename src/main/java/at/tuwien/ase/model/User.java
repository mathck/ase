package at.tuwien.ase.model;

import at.tuwien.ase.services.impl.PasswordEncryption;

import java.util.LinkedList;

/**
 * A model class describing the user object. It is containing a list of projects as well
 * as the level object
 *
 * @author Tomislav Nikic
 * @version 1.0, 13.12.2015
 */
public class User {

    private String userID;
    private String firstName;
    private String lastName;
    private String avatar;
    private Level level;

    private LinkedList<String> projectList = null;

    private byte[] password;
    private byte[] salt;

    public User() {}

    /**
     * Constructor for creating a user object and encrypting the given password with the
     * automatically generated salt.
     *
     * @param userID The user email that is stored in the database.
     * @param password A string password written in plain text.
     */
    public User(String userID, String password) {
        this.userID = userID;
        this.salt = PasswordEncryption.generateSalt();
        this.password = PasswordEncryption.getEncryptedPassword(password, salt);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String email) {
        this.userID = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String givenName) {
        this.firstName = givenName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public byte[] getPassword() {
        return password;
    }

    /**
     * Setting the password as a simple string and saving it as a byte array after
     * encrypting it using the PasswordEncryption class.
     *
     * @param password A string password written in plain text.
     */
    public void setPassword(String password) {
        if (password != null) {
            if (salt == null) {
                salt = PasswordEncryption.generateSalt();
            }
            this.password = PasswordEncryption.getEncryptedPassword(password, salt);
        } else {
            this.password = null;
            this.salt = null;
        }
    }

    public void setPasswordEnc(byte[] password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public LinkedList<String> getProjectList() {
        return projectList;
    }

    public void setProjectList(LinkedList<String> projectList) {
        this.projectList = projectList;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

}
