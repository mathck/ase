package at.tuwien.ase.model;

/**
 * Created by Andreas on 13.11.2015.
 */
public class LoginUnit {

    private String email;
    private String password;

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public Login createLogin() {
        return new Login(email, password);
    }
}
