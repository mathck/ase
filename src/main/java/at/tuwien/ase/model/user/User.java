package at.tuwien.ase.model.user;

import at.tuwien.ase.controller.PasswordEncryption;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * Created by Tomislav on 05/11/2015.
 */
public class User implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private byte[] password;
    private byte[] salt;

    public User(String email, String password) {
        this.email = email;
        salt = PasswordEncryption.generateSalt();
        this.password = PasswordEncryption.getEncryptedPassword(password, salt);
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = PasswordEncryption.getEncryptedPassword(password, salt);
    }

    public String getName() {
        return firstName;
    }
    public void setName(String givenName) {
        this.firstName = givenName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void toFile() {
        try {
            PrintWriter out = new PrintWriter(email + ".txt");
            out.println("email: " + email);
            out.println("firstName: " + firstName);
            out.println("lastName: " + lastName);
            out.println("encrypted password: " + password.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
