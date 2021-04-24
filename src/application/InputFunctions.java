package application;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class InputFunctions {
	
	public InputFunctions() {
		
	}
	
	public void keyPressed (KeyEvent event, Character character) {
		
		KeyCode key = event.getCode();
		
		if (key == KeyCode.ESCAPE) {
			System.exit(0);
		}

		if (key == KeyCode.RIGHT) {
			character.setdx(5);
			//character.setColor(Color.BLACK);
			character.walking = true;
		}
		if (key == KeyCode.LEFT) {
			character.setdx(-5);
			character.walking = true;
		}
		if (key == KeyCode.SPACE || key == KeyCode.UP) {
			if (!character.getJumping()) {
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
