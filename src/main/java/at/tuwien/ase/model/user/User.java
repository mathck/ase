package at.tuwien.ase.model.user;

import at.tuwien.ase.controller.PasswordEncryption;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Created by Tomislav Nikic on 05/11/2015.
 */
public class User {
	// Info
	private String uID;
	private String firstName;
	private String lastName;
	private String avatar;

	// Lists
	private LinkedList<String> projectList = null;

	// Security
	private byte[] password;
	private byte[] salt;

	// Constructors
	// @author Tomislav Nikic
	public User() {
	}

	public User(String uID, String password) {
		this.uID = uID;
		this.salt = PasswordEncryption.generateSalt();
		this.password = PasswordEncryption.getEncryptedPassword(password, salt);
	}

	// Getter and setter for user ID (email)
	// @author Tomislav Nikic
	public String getUserID() {
		return uID;
	}

	public void setUserID(String email) {
		this.uID = email;
	}

	// Getter and setter for first name
	// @author Tomislav Nikic
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String givenName) {
		this.firstName = givenName;
	}

	// Getter and setter for last name
	// @author Tomislav Nikic
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	// Getter and setter for avatar
	// @author Tomislav Nikic
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	// Getter and setter for password
	// @author Tomislav Nikic
	public byte[] getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (salt == null) {
			salt = PasswordEncryption.generateSalt();
		}
		this.password = PasswordEncryption.getEncryptedPassword(password, salt);
	}

	public void setPasswordEnc(byte[] password) {
		this.password = password;
	}

	// Getter and setter for salt
	// @author Tomislav Nikic
	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	// Getter and setter for project list
	// @author Tomislav Nikic
	public LinkedList<String> getProjectList() {
		return projectList;
	}

	public void setProjectList(LinkedList<String> projectList) {
		this.projectList = projectList;
	}

}
