package at.tuwien.ase.model;

/**
 * Created by mathc_000 on 11-Dec-15.
 */
// @author : Mateusz Czernecki
public class Level {

    private String levelSource; // for what is the level computed?: Project, OverallUserLevel, ... ?
    private int currentXp;
    private int currentLevel;
    private double xpBar; // 0 - 100
    private int nextLevel;

    public Level(String levelSource, int currentXp, int currentLevel, double xpBar, int nextLevel) {
        this.levelSource = levelSource;
        this.currentXp = currentXp;
        this.currentLevel = currentLevel;
        this.xpBar = xpBar;
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
