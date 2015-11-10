package at.tuwien.ase.domain.user;

import at.tuwien.ase.service.PasswordEncryption;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * Created by Tomislav on 05/11/2015.
 */
public class User implements Serializable {

    private String givenName;
    private String surname;
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
        return givenName;
    }
    public void setName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void toFile() {
        try {
            PrintWriter out = new PrintWriter(email + ".txt");
            out.println("email: " + email);
            out.println("give name: " + givenName);
            out.println("surname: " + surname);
            out.println("encrypted password: " + password.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
