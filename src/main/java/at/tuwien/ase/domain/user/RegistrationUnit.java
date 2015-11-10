package at.tuwien.ase.domain.user;

import java.io.Serializable;

/**
 * Created by Tomislav on 10/11/2015.
 */
public class RegistrationUnit implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User createUser() {
        User newUser = new User(email, password);
        newUser.setName(firstName);
        newUser.setSurname(lastName);
        return newUser;
    }

}
