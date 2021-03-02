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
		
		if (key == KeyCode.RIGHT) {
			character.setdx(2);
			character.walking = true;
		}
		if (key == KeyCode.LEFT) {
			character.setdx(-2);
			character.walking = true;
		}
		if (key == KeyCode.SPACE) {
			character.setdy(-5);
			character.jumping = true;
		}
	}
	
	public void keyReleased(KeyEvent event, Character character) {
		
		KeyCode key = event.getCode();
		
		if (key == KeyCode.RIGHT) {
			character.setdx(0);
			character.walking = false;
		}
		if (key == KeyCode.LEFT) {
			character.setdx(0);
			character.walking = false;
		}
		//if (key == KeyCode.SPACE) {
		//	character.setdy(0);
		//}
	}

}
