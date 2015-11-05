package at.tuwien.ase.service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 * Created by Tomislav on 05/11/2015.
 */
public class PasswordEncryption {

    public static byte[] generateSalt() {
        SecureRandom random=null;
        try {random=SecureRandom.getInstance("SHA1PRNG");}
        catch (NoSuchAlgorithmException e) {e.printStackTrace();}
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] getEncryptedPassword(String password, byte[] salt) {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
            return f.generateSecret(spec).getEncoded();
        }
        catch (Exception e ) {e.printStackTrace();}
        return null;
    }

    public static boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }


}
