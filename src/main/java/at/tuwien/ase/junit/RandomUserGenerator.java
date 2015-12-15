package at.tuwien.ase.junit;

import at.tuwien.ase.junit.testdata.Firstnames;
import at.tuwien.ase.junit.testdata.Lastnames;
import at.tuwien.ase.model.LoginToken;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by mathc_000 on 25-Nov-15.
 */

// @author Mateusz Czernecki
public class RandomUserGenerator {

    public static String getRandomFirstname() throws ArrayIndexOutOfBoundsException {
        return Firstnames.data[new Random().nextInt(Firstnames.data.length)];
    }

    public static String getRandomLastname() throws ArrayIndexOutOfBoundsException {
        return Lastnames.data[new Random().nextInt(Lastnames.data.length)];
    }

    public static String getMailByName(String firstname, String lastname, String domain) throws ArrayIndexOutOfBoundsException {
        return firstname + "." + lastname + getRandomBirthDate() + "@" + domain + ".com";
    }

    public static String getRandomAvatar() {
        return "img/avatars/" + new Random().nextInt(30) + ".png";
    }


    private static String getRandomBirthDate() {
        return (new Random().nextInt(100) + 1900) + "";
    }
}
