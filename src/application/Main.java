package application;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	private boolean menuLoaded = false;
	private boolean gameLoaded = false;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setWidth(1000);
		primaryStage.setHeight(800);
		primaryStage.setTitle("Jumping Man");
		StateManager.gameState = State.MAINMENU;
		StateManager.currentLevel = Level.LEVEL1;
		try {
			GameObject game = new GameObject();

			game.render(primaryStage);
			AnimationTimer timer = new AnimationTimer() {
				@Override
				public void handle(long arg0) {
					if(StateManager.gameState == State.PLAYING || StateManager.gameState == State.DYING || StateManager.gameState == State.WINNING) {
						game.update();
						if(!gameLoaded) {
							try {
								game.render(primaryStage);
								gameLoaded = true;
								menuLoaded = false;
							} catch (IOException e) {
								e.printStackTrace();
							}
							game.processInput();
						}
					}
					else if(!menuLoaded || StateManager.prevMenu == State.GAMEOVER) {
						if(StateManager.prevMenu == State.GAMEOVER)
							StateManager.prevMenu = State.MAINMENU;
						try {
							game.render(primaryStage);
						} catch (IOException e) {
							e.printStackTrace();
						}
						menuLoaded = true;
						gameLoaded = false;
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