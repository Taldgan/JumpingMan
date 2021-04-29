package application;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	private boolean loaded = true;

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
							loaded = false;
						} catch (IOException e) {
							e.printStackTrace();
						}
						game.processInput();
					}
					else if(!loaded){
						try {
							game.render(primaryStage);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						loaded = true;
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
