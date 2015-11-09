package at.tuwien.ase.domain.user;

import at.tuwien.ase.service.PasswordEncryption;

/**
 * Created by Tomislav on 05/11/2015.
 */
public abstract class User {
    private String name;
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
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
}
