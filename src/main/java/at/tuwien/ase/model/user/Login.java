package at.tuwien.ase.model.user;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * Created by Andreas on 13.11.2015.
 */
public class Login implements Serializable {
    private String email;
    private String password;

    public Login(){}

    public Login (String email, String password){
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public void toFile() {
        try {
            PrintWriter out = new PrintWriter("login - " + email + ".txt");
            out.println("email: " + email);
            out.println("encrypted password: " + password.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
