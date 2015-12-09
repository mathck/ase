package at.tuwien.ase;

/**
 * Created by mathc_000 on 22-Nov-15.
 */
import at.tuwien.ase.dao.ProjectDAO;
import at.tuwien.ase.dao.SubtaskDAO;
import at.tuwien.ase.dao.UserDAO;
import at.tuwien.ase.junit.AppConfig;
import at.tuwien.ase.model.User;
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
        ProjectDAO projectDAO = Mockito.mock(ProjectDAO.class);
        SubtaskDAO subtaskDAO = Mockito.mock(SubtaskDAO.class);
        UserService userService = new UserServiceImpl(userDAO, projectDAO, subtaskDAO);
        String uID = "0", email = "1", password = "2";
        User oUser = new User("zergling", "garados");
        when(userDAO.findByID(uID)).thenReturn(oUser);
        when(userDAO.findByID(email)).thenReturn(new User(email, password));
        //when(userDAO.insertUser(Mockito.any(User.class))).thenReturn(oUser);

        // Act
        userService.writeUser(oUser);
        userService.updateUser(uID, new User(email, password));

        // Assert
        User user = userService.getByID(email);

        verify(userDAO, atLeastOnce()).findByID(email);
        verify(userDAO, atLeastOnce()).insertUser(Mockito.any(User.class));

        assertNotNull(user);
        assertNotEquals(password, user.getPassword());
    }
}