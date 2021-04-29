package application;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputFunctions {
	
	public InputFunctions() {
		
	}
	
	public void keyPressed (KeyEvent event, Character character) {
		
		KeyCode key = event.getCode();
		
		if (key == KeyCode.ESCAPE) {
			//System.exit(0);
			StateManager.gameState = State.MAINMENU;
		}

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
			}
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
