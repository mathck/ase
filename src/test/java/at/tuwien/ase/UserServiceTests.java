package at.tuwien.ase;

/**
 * Created by mathc_000 on 22-Nov-15.
 */
/*
import at.tuwien.ase.dao.task.UserDAO;
import at.tuwien.ase.junit.AppConfig;
import at.tuwien.ase.model.user.User;
import at.tuwien.ase.services.UserService;
import at.tuwien.ase.services.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class UserServiceTests {

    @Test
    public void whenUpdateUser_insertNewOneAndCheckAttributes() {

        // Arrange
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        UserService userService = new UserServiceImpl(userDAO);
        String uID = "0", email = "1", password = "2", firstName = "3", lastName = "4", avatar = "5";
        User oUser = new User("zergling", "garados");
        when(userDAO.findByID(uID)).thenReturn(oUser);
        when(userDAO.insertUser(Mockito.any(User.class))).thenReturn(oUser);

        // Act
        User user = userService.updateUser(uID, email, password, firstName, lastName, avatar);

        // Assert
        verify(userDAO, atLeastOnce()).findByID(uID);
        verify(userDAO, atLeastOnce()).insertUser(Mockito.any(User.class));

        assertEquals(email, user.getEmail());
        assertNotEquals(password, user.getPassword());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(avatar, user.getAvatar());
    }
}*/