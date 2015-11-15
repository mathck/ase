package at.tuwien.ase.model.user;

import java.io.Serializable;

/**
 * Created by Tomislav on 10/11/2015.
 */
public class RegistrationUnit implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public User createUser() {
        User newUser = new User(email, password);
        newUser.setName(firstName);
        newUser.setLastname(lastName);
        return newUser;
    }

}
