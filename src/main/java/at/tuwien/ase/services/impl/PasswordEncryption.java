package at.tuwien.ase.services.impl;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 * Password encryption class. Used for encrypting, salt creation and authenticating.
 *
 * @author Tomislav Nikic
 * @version 1.0, 14.12.2015
 */
public class PasswordEncryption {

    /**
     * Generates a salt using SHA1PRNG (Secure Random).
     *
     * @return A byte array containing the generated salt.
     */
    public static byte[] generateSalt() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Encrypts the given String using the generated or stored salt.
     *
     * @param password The given password as plain text
     * @param salt The generated or loaded salt.
     * @return A byte array containing the encrypted password.
     */
    public static byte[] getEncryptedPassword(String password, byte[] salt) {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
            return f.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypts the password and compares it with the already encrypted password provided.
     *
     * @param attemptedPassword The password in plain text.
     * @param encryptedPassword The current password encrypted and stored as byte[].
     * @param salt The generated salt as a byte[],
     * @return A boolean value, being true if the password is correct, otherwise being false.
     * @throws NoSuchAlgorithmException Thrown if the algorithm needed for the encryption is not
     *          available in the local VM.
     * @throws InvalidKeySpecException Thrown if the encryption with the provided salt fails.
     */
    public static boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }


}
