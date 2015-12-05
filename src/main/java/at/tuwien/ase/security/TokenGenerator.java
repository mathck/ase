package at.tuwien.ase.security;

import at.tuwien.ase.model.LoginToken;

import java.security.SecureRandom;

/**
 * Created by mathc_000 on 25-Nov-15.
 */
public class TokenGenerator {

    // @author Mateusz Czernecki
    // http://stackoverflow.com/a/30771209/2880465
    public static LoginToken createNewToken() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String token = bytes.toString();

        return new LoginToken(token);
    }
}
