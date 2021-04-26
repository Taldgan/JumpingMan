package application;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputFunctions {
	
	public InputFunctions() {
		
	}
	
	public void keyPressed (KeyEvent event, Character character) {
		
		KeyCode key = event.getCode();
		
		if (key == KeyCode.ESCAPE) {
			System.exit(0);
		}

		if (key == KeyCode.RIGHT && !character.getCollideRight()) {
			character.setdx(5);
			//character.setColor(Color.BLACK);
			character.walking = true;
		}
		if (key == KeyCode.LEFT && !character.getCollideLeft()) {
			character.setdx(-5);
			character.walking = true;
		}
		if (key == KeyCode.SPACE || key == KeyCode.UP) {
			if (!character.getJumping() || character.getCollide()) {
				character.setGroundLvl(character.gety());
				character.setdy(-5);
				character.jumping = true;
			}
		}
	}
	
	public void keyReleased(KeyEvent event, Character character) {
		
		KeyCode key = event.getCode();
		
		if (key == KeyCode.RIGHT) {
			character.walking = false;
			character.setdx(0);
		}
		if (key == KeyCode.LEFT) {
			character.walking = false;
			character.setdx(0);
		}
		
	}
}
