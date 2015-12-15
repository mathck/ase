package at.tuwien.ase.services.impl;

import at.tuwien.ase.model.Level;
import at.tuwien.ase.services.LevelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LevelServiceImpl implements LevelService {

    private static final Logger logger = LogManager.getLogger(LevelServiceImpl.class);


    public Level getLevelByXp(String levelSource, int xp) {
        int currentLevel = computeLevel(xp);
        return new Level(levelSource, xp, currentLevel, computeNextLevelRatio(xp), currentLevel + 1);
    }

    // based on http://gamedev.stackexchange.com/a/110437
    // todo Mateusz: optimize
    private int computeLevel(int xp) {
        return (int) ((1 + Math.sqrt(1 + 8 * xp / 50)) / 2);
    }

    private double computeNextLevelRatio(int currentXp) {
        double xpNeededForLevelN = getXpNeededForLevelN(computeLevel(currentXp));
        double xpNeededForLevelNplus1 = getXpNeededForLevelN(computeLevel(currentXp) + 1);
        double xpLevelGap = xpNeededForLevelNplus1 - xpNeededForLevelN;

        return ((currentXp - xpNeededForLevelN) / xpLevelGap) * 100;
    }

    private int getXpNeededForLevelN(int N) {
        return (sum(N-1)) * 50;
    }

    public int sum(int num)
    {
        return (num * (num + 1)) / 2;
    }
}