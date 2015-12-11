package at.tuwien.ase.services;

import at.tuwien.ase.model.Level;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserRole;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 16.11.2015.
 */
public interface LevelService
{

	/**
	 * @author Mateusz Czernecki
	 * Provide xp and receive Level Object that should be sent to Frontend
     * @param levelSource for what is the level computed?: use "+ProjectId+" or "OverallUserLevel"
	 * @param xp
	 * @return
     */
	Level getLevelByXp(String levelSource, int xp);
}