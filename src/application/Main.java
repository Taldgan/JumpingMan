package application;

import java.io.IOException;

import application.controller.GameObject;
import application.model.Level;
import application.model.State;
import application.model.StateManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

	private boolean menuLoaded = false;
	private boolean gameLoaded = false;

	//Scenes
	private StackPane root;
	public static Scene mainScene;
	
	//For use of instanced controller
	private GameObject game;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			root = fxmlLoader.load(getClass().getResource("/application/view/Main.fxml").openStream());
			mainScene = new Scene(root, 1000, 800);
			primaryStage.setWidth(1000);
			primaryStage.setHeight(800);
			primaryStage.setTitle("Jumping Man");
			primaryStage.setScene(mainScene);
			StateManager.gameState = State.MAINMENU;
			StateManager.currentLevel = Level.LEVEL1;
			game = fxmlLoader.getController();

			game.render(primaryStage, root, fxmlLoader);
			AnimationTimer timer = new AnimationTimer() {
				@Override
				public void handle(long arg0) {
					if(StateManager.gameState == State.PLAYING || StateManager.gameState == State.DYING || StateManager.gameState == State.WINNING) {
						game.update();
						if(!gameLoaded) {
							try {
								game.render(primaryStage, root, fxmlLoader);
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
							game.render(primaryStage, root, fxmlLoader);
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