package at.tuwien.ase.model;

/**
 * Created by mathc_000 on 11-Dec-15.
 */
// @author : Mateusz Czernecki
public class Level {

    private int currentXp;
    private int currentLevel;
    private int nextLevel;

    public Level(int currentXp, int currentLevel, int nextLevel) {
        this.currentXp = currentXp;
        this.currentLevel = currentLevel;
        this.nextLevel = nextLevel;
    }

    @Override
    public String toString() {
        return "Level{" +
                "currentXp=" + currentXp +
                ", currentLevel='" + currentLevel + '\'' +
                ", nextLevel='" + nextLevel +
                '}';
    }
}
