package at.tuwien.ase.model;

/**
 * Created by DanielHofer on 05.12.2015.
 */
public class LoginToken {

    private String token;

    public LoginToken(){

    }

    public LoginToken(String token){
        this.token=token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
