package at.tuwien.ase.services.impl;

import at.tuwien.ase.dao.IssueDAO;
import at.tuwien.ase.dao.TaskDAO;
import at.tuwien.ase.model.Issue;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Level;
import at.tuwien.ase.model.User;
import at.tuwien.ase.services.IssueService;
import at.tuwien.ase.services.LevelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;

@Service
public class LevelServiceImpl implements LevelService {

    private static final Logger logger = LogManager.getLogger(LevelServiceImpl.class);


    public Level getLevelByXp(int xp) {
        int currentLevel = computerLevel(xp);

        return new Level(xp, currentLevel, currentLevel + 1);
    }

    // based on http://gamedev.stackexchange.com/a/110437
    // todo Mateusz: optimize
    private int computerLevel(int xp) {
        return (int) ((1 + Math.sqrt(1 + 8 * xp / 50)) / 2);
    }
}