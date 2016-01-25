package at.tuwien.ase.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by mathc_000 on 11-Dec-15.
 */
// @author : Mateusz Czernecki
public class Level {

    private String levelSource; // for what is the level computed?: Project, OverallUserLevel, ... ?
    @NotNull
    private int currentXp;
    @NotNull
    @Min(1)
    private int currentLevel;
    @NotNull
    @Min(0)
    @Max(100)
    private double xpBar; // 0 - 100
    @NotNull
    private int nextLevel;

    public Level(String levelSource, int currentXp, int currentLevel, double xpBar, int nextLevel) {
        this.levelSource = levelSource;
        this.currentXp = currentXp;
        this.currentLevel = currentLevel;
        this.xpBar = xpBar;
        this.nextLevel = nextLevel;
    }

    public String getLevelSource() {
        return levelSource;
    }

    public void setLevelSource(String levelSource) {
        this.levelSource = levelSource;
    }

    public int getCurrentXp() {
        return currentXp;
    }

    public void setCurrentXp(int currentXp) {
        this.currentXp = currentXp;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public double getXpBar() {
        return xpBar;
    }

    public void setXpBar(double xpBar) {
        this.xpBar = xpBar;
    }

    public int getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(int nextLevel) {
        this.nextLevel = nextLevel;
    }

    @Override
    public String toString() {
        return "Level{" +
                "levelSource=" + levelSource +
                ", currentXp='" + currentXp + '\'' +
                ", currentLevel='" + currentLevel + '\'' +
                ", xpBar='" + xpBar + '\'' +
                ", nextLevel='" + nextLevel +
                '}';
    }
}
