package application.model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Class containing functions for processing user input. Can alter player movement and gameState
 * @author Thomas White, Caleb Kopecky, Gabriel Pastelero
 */
public class InputFunctions {

	/**
	 * Receives keyboard input and then processes it, doing various actions .
	 * @param event - key event object, allows gathering the key pressed
	 * @param character - character's movement to alter based off of key
	 */
	public void keyPressed (KeyEvent event, Character character) {

		KeyCode key = event.getCode();

		//Pause game if playing & press escape, otherwise resume
		if (key == KeyCode.ESCAPE) {
			if(StateManager.gameState == State.PAUSE) 
				StateManager.gameState = State.PLAYING;
			else if(StateManager.gameState == State.PLAYING)
				StateManager.gameState = State.PAUSE;

		}

		//Quit if paused and Q pressed
		if(key == KeyCode.Q && StateManager.gameState == State.PAUSE)
			System.exit(0);

		//If 'playing', move player left/right or jump based off of input
		if(StateManager.gameState == State.PLAYING) {
			if ((key == KeyCode.D || key == KeyCode.RIGHT || key == KeyCode.L) && !character.getCollideRight()) {
				character.setdx(5);
				character.setWalking(true);
			}
			if ((key == KeyCode.A || key == KeyCode.LEFT || key == KeyCode.H) && !character.getCollideLeft()) {
				character.setdx(-5);
				character.setWalking(true);
			}
			if (key == KeyCode.SPACE || key == KeyCode.UP || key == KeyCode.K || key == KeyCode.W) {
				if (!character.getJumping() || character.getCollide()) {
					character.setGroundLevel(character.gety());
					character.setdy(-6.5); 
					character.setJumping(true);
					Sounds.sPlayer.playSFX(0);
				}
			}
		}

	}

	/**
	 * 
	 * @param event -> determines which key was pressed
	 * @param character -> the character object
	 */
	//If key is released, stop respective movement
	public void keyReleased(KeyEvent event, Character character) {

		KeyCode key = event.getCode();

		if (key == KeyCode.RIGHT || key == KeyCode.L) {
			character.setWalking(false);
			character.setdx(0);
		}
		if (key == KeyCode.LEFT || key == KeyCode.H) {
			character.setWalking(false);
			character.setdx(0);
		}

	}
}