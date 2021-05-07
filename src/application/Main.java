package application;

import java.io.IOException;

import application.controller.GameObject;
import application.model.Level;
import application.model.LevelManager;
import application.model.Sounds;
import application.model.State;
import application.model.StateManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private boolean menuLoaded = false;
	private boolean gameLoaded = false;

	//Scenes
	private BorderPane root;
	public static Scene mainScene;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = FXMLLoader.load(getClass().getResource("/application/view/Main.fxml"));
			mainScene = new Scene(root, 1000, 800);
			primaryStage.setWidth(1000);
			primaryStage.setHeight(800);
			primaryStage.setTitle("Jumping Man");
			primaryStage.setScene(mainScene);
			StateManager.gameState = State.MAINMENU;
			StateManager.currentLevel = Level.LEVEL1;
			GameObject game = new GameObject();

			game.render(primaryStage, root);
			AnimationTimer timer = new AnimationTimer() {
				@Override
				public void handle(long arg0) {
					if(StateManager.gameState == State.PLAYING || StateManager.gameState == State.DYING || StateManager.gameState == State.WINNING) {
						game.update();
						if(!gameLoaded) {
							try {
								game.render(primaryStage, root);
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
							game.render(primaryStage, root);
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