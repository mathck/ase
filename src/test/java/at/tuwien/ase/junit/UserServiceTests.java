package at.tuwien.ase.junit;

/**
 * Created by mathc_000 on 22-Nov-15.
 */
import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.data_generator.AppConfig;
import at.tuwien.ase.model.Level;
import at.tuwien.ase.model.User;
import at.tuwien.ase.services.LevelService;
import at.tuwien.ase.services.UserService;
import at.tuwien.ase.services.impl.UserServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

// Bei Problemen bitte pom.xml Rechtsklick -> Maven -> Reimport
// Im Notfall auskommentieren und ruhig pushen

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class UserServiceTests {

    static UserDAO userDAO;
    static ProjectDAO projectDAO;
    static SubtaskDAO subtaskDAO;
    static LevelService levelService;
    static UserService userService;

    @BeforeClass
    public static void setUpClass() {
        //executed only once, before the first test
        userDAO = Mockito.mock(UserDAO.class);
        projectDAO = Mockito.mock(ProjectDAO.class);
        subtaskDAO = Mockito.mock(SubtaskDAO.class);
        levelService = Mockito.mock(LevelService.class);
        userService = new UserServiceImpl(userDAO, projectDAO, subtaskDAO, levelService);
    }

    // @author: Mateusz Czernecki
    @Test
    public void updateUser_insertNewOneAndCheckAttributes() {

        // Arrange
        String uID = "mathck@gmail.com", email = "a1025504@gmail.com", password = "2asd2123";
        User oUser = new User("hund@gmail.com", "garados");
        oUser.setFirstName("Rambabaaaa");
        oUser.setLastName("Pitt");
        oUser.setAvatar("18.png");
        when(userDAO.findByID(uID)).thenReturn(oUser);
        when(userDAO.findByID(email)).thenReturn(new User(email, password));
        User updatedUser = new User(email, password);
        updatedUser.setFirstName("Rambabaaaa");
        updatedUser.setLastName("Pitt");
        updatedUser.setAvatar("18.png");
        when(levelService.getLevelByXp("User", 0)).thenReturn(new Level("asd", 0, 1, 0, 2));

        // Act
        userService.writeUser(oUser);
        userService.updateUser(uID, updatedUser);

        // Assert
        User user = userService.getByID(email);

        verify(userDAO, atLeastOnce()).findByID(email);
        verify(userDAO, atLeastOnce()).insertUser(Mockito.any(User.class));

        assertNotNull(user);
        assertNotEquals(password, user.getPassword());
        // mockito reset todo
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void writeUser_invalidUserId() {
        // Arrange
        String uID = "0";
        User oUser = new User("mat@@admin.com", "garados");
        oUser.setFirstName("Brad");
        oUser.setLastName("Pitt");
        oUser.setAvatar("18.png");
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.writeUser(oUser);

        // Assert
        // IllegalArgumentException
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void writeUser_invalidFirstname() {
        // Arrange
        String uID = "0";
        User oUser = new User("zergling@domain.com", "garados");
        oUser.setFirstName("s");
        oUser.setLastName("Pitt");
        oUser.setAvatar("18.png");
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.writeUser(oUser);

        // Assert
        // IllegalArgumentException
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void writeUser_invalidLastname() {
        // Arrange
        String uID = "0";
        User oUser = new User("zergling@domain.com", "garados");
        oUser.setFirstName("Brad");
        oUser.setLastName("");
        oUser.setAvatar("18.png");
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.writeUser(oUser);

        // Assert
        // IllegalArgumentException
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void writeUser_invalidAvatar() {
        // Arrange
        String uID = "0";
        User oUser = new User("zergling@domain.com", "garados");
        oUser.setFirstName("Brad");
        oUser.setLastName("Pitt");
        oUser.setAvatar(null);
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.writeUser(oUser);

        // Assert
        // IllegalArgumentException
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void writeUser_invalidPassword() {
        // Arrange
        String uID = "0";
        User oUser = new User("zergling@domain.com", "password");
        oUser.setPassword(null);
        oUser.setFirstName("Brad");
        oUser.setLastName("Pitt");
        oUser.setAvatar("18.png");
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.writeUser(oUser);

        // Assert
        // IllegalArgumentException
    }

    // @author: Mateusz Czernecki
    @Test
    public void updateUser_working() {
        // Arrange
        String uID = "0";
        User oUser = new User("zergling@domain.com", "password");
        oUser.setFirstName("Brad");
        oUser.setLastName("Pitt");
        oUser.setAvatar("18.png");
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.updateUser(uID, oUser);

        // Assert
        verify(userDAO, times(1)).updateUser(uID, oUser);
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void updateUser_invalidFirstname() {
        // Arrange
        String uID = "0";
        User oUser = new User("zergling@domain.com", "password");
        oUser.setFirstName("B");
        oUser.setLastName("Pitt");
        oUser.setAvatar("18.png");
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.updateUser(uID, oUser);

        // Assert
        verify(userDAO, times(0)).updateUser(uID, oUser);
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void updateUser_invalidLastname() {
        // Arrange
        String uID = "0";
        User oUser = new User("zergling@domain.com", "password");
        oUser.setFirstName("Brad");
        oUser.setLastName("P");
        oUser.setAvatar("18.png");
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.updateUser(uID, oUser);

        // Assert
        verify(userDAO, times(0)).updateUser(uID, oUser);
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void updateUser_invalidUserId() {
        // Arrange
        String uID = "0";
        User oUser = new User("zergling", "password");
        oUser.setFirstName("Brad");
        oUser.setLastName("Pitt");
        oUser.setAvatar("18.png");
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.updateUser(uID, oUser);

        // Assert
        verify(userDAO, times(0)).updateUser(uID, oUser);
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void updateUser_invalidAvatar() {
        // Arrange
        String uID = "0";
        User oUser = new User("zergling@domain.com", "password");
        oUser.setFirstName("Brad");
        oUser.setLastName("Pitt");
        oUser.setAvatar("");
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.updateUser(uID, oUser);

        // Assert
        verify(userDAO, times(0)).updateUser(uID, oUser);
    }

    // @author: Mateusz Czernecki
    @Test(expected = IllegalArgumentException.class)
    public void updateUser_invalidPassword() {
        // Arrange
        String uID = "0";
        User oUser = new User("zergling@domain.com", "password");
        oUser.setPassword(null);
        oUser.setFirstName("Brad");
        oUser.setLastName("Pitt");
        oUser.setAvatar("");
        when(userDAO.findByID(uID)).thenReturn(oUser);

        // Act
        userService.updateUser(uID, oUser);

        // Assert
        verify(userDAO, times(0)).updateUser(uID, oUser);
    }
}