package application;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputFunctions {
	Sounds s;
	public InputFunctions() {
		s = new Sounds();
	}
	
	public void keyPressed (KeyEvent event, Character character) {
		
		KeyCode key = event.getCode();
		
		if (key == KeyCode.ESCAPE) {
			if(StateManager.gameState == State.PAUSE) 
				StateManager.gameState = StateManager.currLevel;
			else
				StateManager.gameState = State.PAUSE;

		}
		
		if(key == KeyCode.Q && StateManager.gameState == State.PAUSE)
			System.exit(0);

		if ((key == KeyCode.RIGHT || key == KeyCode.L) && !character.getCollideRight()) {
			character.setdx(5);
			character.walking = true;
		}
		if ((key == KeyCode.LEFT || key == KeyCode.H) && !character.getCollideLeft()) {
			character.setdx(-5);
			character.walking = true;
		}
		if (key == KeyCode.SPACE || key == KeyCode.UP || key == KeyCode.K) {
			if (!character.getJumping() || character.getCollide()) {
				character.setGroundLvl(character.gety());
				character.setdy(-5.5); //Changed from 5, to accomodate raising the platforms
				character.jumping = true;
				s.playSFX(0);
			}
		}
		
		//temporary
		if (key == KeyCode.W) {
			character.setdy(-5);
		}
	}
	
	public void keyReleased(KeyEvent event, Character character) {
		
		KeyCode key = event.getCode();
		
		if (key == KeyCode.RIGHT || key == KeyCode.L) {
			character.walking = false;
			character.setdx(0);
		}
		if (key == KeyCode.LEFT || key == KeyCode.H) {
			character.walking = false;
			character.setdx(0);
		}
		
	}
}