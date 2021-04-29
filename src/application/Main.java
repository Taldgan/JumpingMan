package application;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			GameObject game = new GameObject();

			game.render(primaryStage);
			AnimationTimer timer = new AnimationTimer() {

				@Override
public void handle(long arg0) {
	if(StateManager.gameState != State.MAINMENU) {
		game.update();
		try {
			game.render(primaryStage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game.processInput();
	}
}
	};
			timer.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
