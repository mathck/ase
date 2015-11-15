package at.tuwien.ase.model.user;

import at.tuwien.ase.controller.PasswordEncryption;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * Created by Tomislav on 05/11/2015.
 */
public class User implements Serializable {

    private String firstname;
    private String lastname;
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
        return firstname;
    }
    public void setName(String givenName) {
        this.firstname = givenName;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void toFile() {
        try {
            PrintWriter out = new PrintWriter(email + ".txt");
            out.println("email: " + email);
            out.println("firstname: " + firstname);
            out.println("lastname: " + lastname);
            out.println("encrypted password: " + password.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
