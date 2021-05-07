package application;

import java.io.IOException;

import application.controller.GameObject;
import application.model.Level;
import application.model.LevelManager;
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

	private StackPane root;
	public static Scene mainScene;
	
	//For use of instanced controller
	private GameObject game;

	/**
	 * Overridden JavaFX start method, initializes GUI variables and begins the AnimationTimer-based game loop .
	 *	@param primaryStage - the frame of the window being created
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			//Setup stage
			//Initialize fxmlLoader in a non-static context. This allows us to use the FXML-instanced controller outside of the fxml file
			FXMLLoader fxmlLoader = new FXMLLoader();
			root = fxmlLoader.load(getClass().getResource("/application/view/Main.fxml").openStream());
			mainScene = new Scene(root, 1000, 800);
			primaryStage.setWidth(1000);
			primaryStage.setHeight(800);
			primaryStage.setTitle("Jumping Man");
			primaryStage.setScene(mainScene);
			//Starting state should be menu, starting level should be level 1
			StateManager.gameState = State.MAINMENU;
			StateManager.currentLevel = Level.LEVEL1;
			//load level early for dynamic main menu
			LevelManager.loadLevel();
			//Assign GameObject game to same instance as the one controlling the fxml files
			game = fxmlLoader.getController();
			game.render(primaryStage, root);

			//Begin the game loop
			AnimationTimer timer = new AnimationTimer() {
				@Override
				public void handle(long arg0) {
					if(StateManager.gameState == State.MAINMENU)
						game.update();
					//only process input if playing/dying/winning, render game if not already loaded
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
					//render menus if not already loaded
					else if(!menuLoaded || StateManager.prevMenu == State.GAMEOVER) {
						if(StateManager.prevMenu == State.GAMEOVER) {
							StateManager.prevMenu = State.MAINMENU;
							StateManager.currentLevel = Level.LEVEL1;
							game.newGame();
							StateManager.gameState = State.MAINMENU;
						}
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