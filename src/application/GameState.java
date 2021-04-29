package application;

public class GameState {
	enum State {
		MAINMENU, LEVEL1, LEVEL2, PAUSE, YOUDIED, YOUWON
	}
	public static State gameState;	
	
	public void setState(State state) {
		GameState.gameState = state;
	}
}
