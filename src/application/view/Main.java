package application.view;

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
	BorderPane root;
	Scene menuScene;
	public static Scene gameScene;
	Scene deadScene;
	Scene winScene;
	Scene gameOverScene;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setWidth(1000);
		primaryStage.setHeight(800);
		primaryStage.setTitle("Jumping Man");
		StateManager.gameState = State.MAINMENU;
		StateManager.currentLevel = Level.LEVEL1;
		try {
			GameObject game = new GameObject();

			render(primaryStage);
			AnimationTimer timer = new AnimationTimer() {
				@Override
				public void handle(long arg0) {
					if(StateManager.gameState == State.PLAYING || StateManager.gameState == State.DYING || StateManager.gameState == State.WINNING) {
						game.update();
						if(!gameLoaded) {
							try {
								render(primaryStage);
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
							render(primaryStage);
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
	
	public void render(Stage primaryStage) throws IOException {
		Parent view;
		switch(StateManager.gameState) {
		case MAINMENU:
			view = FXMLLoader.load(getClass().getResource("/application/view/MainMenu.fxml"));
			this.menuScene = new Scene(view);
			primaryStage.setScene(this.menuScene);
			break;
		case PAUSE:
			LevelManager.pauseLabel.setTranslateX(LevelManager.mainGuy.getCharacter().getTranslateX()+400);
			LevelManager.level.getChildren().add(LevelManager.pauseLabel);
			break;

		case DYING:
		case WINNING:
			Sounds.sPlayer.stopSong();
		case PLAYING:
			LevelManager.infoLabel.setTranslateX(LevelManager.mainGuy.getCharacter().getTranslateX());
			if(!LevelManager.level.getChildren().contains(LevelManager.infoLabel)) 
				LevelManager.level.getChildren().add(LevelManager.infoLabel);
			if(!LevelManager.level.getChildren().contains(LevelManager.lifeCounter)) 
				LevelManager.level.getChildren().add(LevelManager.lifeCounter);
			if(StateManager.gameState == State.PLAYING)
				Sounds.sPlayer.playSong(0);
			this.root = new BorderPane(LevelManager.level);
			this.gameScene = new Scene(root);
			if(LevelManager.level.getChildren().contains(LevelManager.pauseLabel))
				LevelManager.level.getChildren().remove(LevelManager.pauseLabel);
			primaryStage.setScene(this.gameScene);
			break;
		case YOUDIED:
			view = FXMLLoader.load(getClass().getResource("/application/view/YouDied.fxml"));
			this.deadScene = new Scene(view);
			primaryStage.setScene(this.deadScene);
			break;
		case GAMEOVER:
			Sounds.sPlayer.stopSong();
			view = FXMLLoader.load(getClass().getResource("/application/view/GameOver.fxml"));
			this.gameOverScene = new Scene(view);
			primaryStage.setScene(this.gameOverScene);
			break;
		case NEXTLEVEL:
			view = FXMLLoader.load(getClass().getResource("/application/view/NextLevel.fxml"));
			this.winScene = new Scene(view);
			primaryStage.setScene(this.winScene);
			break;
		case YOUWON:
			view = FXMLLoader.load(getClass().getResource("/application/view/YouWon.fxml"));
			this.winScene = new Scene(view);
			primaryStage.setScene(this.winScene);
			break;
		}
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}