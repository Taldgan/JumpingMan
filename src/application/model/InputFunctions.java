package application.model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputFunctions {

	public void keyPressed (KeyEvent event, Character character) {

		KeyCode key = event.getCode();

		if (key == KeyCode.ESCAPE) {
			if(StateManager.gameState == State.PAUSE) 
				StateManager.gameState = State.PLAYING;
			else if(StateManager.gameState == State.PLAYING)
				StateManager.gameState = State.PAUSE;

		}

		if(key == KeyCode.Q && StateManager.gameState == State.PAUSE)
			System.exit(0);

		//Only get movement input if in 'playing' state
		if(StateManager.gameState == State.PLAYING) {
			if ((key == KeyCode.RIGHT || key == KeyCode.L) && !character.getCollideRight()) {
				character.setdx(5);
				character.setWalking(true);
			}
			if ((key == KeyCode.LEFT || key == KeyCode.H) && !character.getCollideLeft()) {
				character.setdx(-5);
				character.setWalking(true);
			}
			if (key == KeyCode.SPACE || key == KeyCode.UP || key == KeyCode.K) {
				if (!character.getJumping() || character.getCollide()) {
					character.setGroundLevel(character.gety());
					character.setdy(-6.5); 
					character.setJumping(true);
					Sounds.sPlayer.playSFX(0);
				}
			}
		}

	}

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