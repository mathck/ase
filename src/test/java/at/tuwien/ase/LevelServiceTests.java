package at.tuwien.ase;

/**
 * Created by mathc_000 on 22-Nov-15.
 */

import at.tuwien.ase.dao.*;
import at.tuwien.ase.junit.AppConfig;
import at.tuwien.ase.model.*;
import at.tuwien.ase.services.IssueService;
import at.tuwien.ase.services.LevelService;
import at.tuwien.ase.services.ProjectService;
import at.tuwien.ase.services.TaskService;
import at.tuwien.ase.services.impl.IssueServiceImpl;
import at.tuwien.ase.services.impl.LevelServiceImpl;
import at.tuwien.ase.services.impl.ProjectServiceImpl;
import at.tuwien.ase.services.impl.TaskServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

// Bei Problemen bitte pom.xml Rechtsklick -> Maven -> Reimport
// Im Notfall auskommentieren und ruhig pushen

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class LevelServiceTests {

    static LevelService levelService;

    @BeforeClass
    public static void setUpClass() {
        //executed only once, before the first test
        levelService = new LevelServiceImpl();
    }

    // @author: Mateusz Czernecki
    @Test
    public void getLevelByXp_0() {
        // Arrange
        String context = "context";

        // Act
        Level level = levelService.getLevelByXp(context, 0);

        // Assert
        assertEquals(1, level.getCurrentLevel());
        assertEquals(0, level.getCurrentXp());
        assertEquals(context, level.getLevelSource(), context);
        assertEquals(2, level.getNextLevel());
        assertEquals(0, (int) level.getXpBar());
    }

    // @author: Mateusz Czernecki
    @Test
    public void getLevelByXp_25() {
        // Arrange
        String context = "context";

        // Act
        Level level = levelService.getLevelByXp(context, 25);

        // Assert
        assertEquals(level.getCurrentLevel(), 1);
        assertEquals(level.getCurrentXp(), 25);
        assertEquals(level.getLevelSource(), context);
        assertEquals(level.getNextLevel(), 2);
        assertEquals(50, level.getXpBar(), 1);
    }

    // @author: Mateusz Czernecki
    @Test
    public void getLevelByXp_50() {
        // Arrange
        String context = "context";

        // Act
        Level level = levelService.getLevelByXp(context, 50);

        // Assert
        assertEquals(2, level.getCurrentLevel());
        assertEquals(50, level.getCurrentXp());
        assertEquals(context, level.getLevelSource(), context);
        assertEquals(3, level.getNextLevel());
        assertEquals(0, (int) level.getXpBar());
    }
}