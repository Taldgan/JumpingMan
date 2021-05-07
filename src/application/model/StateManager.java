package application.model;

/**
 * Manages the state of the game. Holds enmus for the previous menu, the current level, and the current state of the game.
 * @author Thomas White
 *
 */
public class StateManager {
	public static State prevMenu;
	public static State gameState;
	public static Level currentLevel;
}
