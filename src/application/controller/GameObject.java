package application.controller;

import java.io.IOException;

import application.Main;
import application.model.Collision;
import application.model.FloatLabel;
import application.model.InputFunctions;
import application.model.Level;
import application.model.LevelManager;
import application.model.MovingObstacle;
import application.model.PointBox;
import application.model.Score;
import application.model.Sounds;
import application.model.State;
import application.model.StateManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * 
 * @author Thomas White, Caleb Kopecky, Gabriel Pastelero
 * 
 * The GameObject class acts as the controller for the GUI elements, updating core game variables and objects as needed based on user input.
 * This includes updating positions of all dynamic objects in the game (platforms, characters, labels, etc), managing
 * collision detection and resulting movement for all of the character objects, as well as updating the gameState/level information.
 *
 */
public class GameObject extends InputFunctions{
	
	@FXML 
	private Label finalLabel;

	private Parent view = null;
	private double lastTime = System.currentTimeMillis();
	private double delta;
	private double gravity = 1;
	
	private Collision col = new Collision();

	/**
	 * Assigns the passed in StackPane's contents to a new view, based off of game State.
	 * @param primaryStage - the stage, refreshed after re-assigning it's contents
	 * @param root - the StackPane that has it's contents updated
	 * @throws IOException
	 */
	public void render(Stage primaryStage, StackPane root) throws IOException {
		Parent gameView = LevelManager.level;
		if(view != null) {
			root.getChildren().remove(view);
		}
		//Switch to assign displayed content based off of gameState
		switch(StateManager.gameState) {
		case MAINMENU:
			view = FXMLLoader.load(getClass().getResource("/application/view/MainMenu.fxml"));
			this.finalLabel.setText("");
			break;
		//Don't want to hide game content with the pause screen, so only set pause label
		case PAUSE:
			LevelManager.pauseLabel.setTranslateX(LevelManager.mainGuy.getCharacter().getTranslateX()+400);
			LevelManager.level.getChildren().add(LevelManager.pauseLabel);
			view = LevelManager.level;
			this.finalLabel.setText("");
			break;
		//Dying/Winning are animation states, only stop playing the background song and continue as normal with 'playing'
		case DYING:
		case WINNING:
			Sounds.sPlayer.stopSong();
		//Re-assigns and re-add/remove necessary labels (lives/score, etc), sets primary view to the level
		case PLAYING:
			LevelManager.infoLabel.setTranslateX(LevelManager.mainGuy.getCharacter().getTranslateX());
			if(!LevelManager.level.getChildren().contains(LevelManager.infoLabel)) 
				LevelManager.level.getChildren().add(LevelManager.infoLabel);
			if(!LevelManager.level.getChildren().contains(LevelManager.lifeCounter)) 
				LevelManager.level.getChildren().add(LevelManager.lifeCounter);
			if(StateManager.gameState == State.PLAYING)
				Sounds.sPlayer.playSong(0);
			if(LevelManager.level.getChildren().contains(LevelManager.pauseLabel))
				LevelManager.level.getChildren().remove(LevelManager.pauseLabel);
			view = LevelManager.level;
			this.finalLabel.setText("");
			break;
		//Shows the 'YouDied' screen, as well as setting the final label position to show lives left
		case YOUDIED:
			view = FXMLLoader.load(getClass().getResource("/application/view/YouDied.fxml"));
			this.finalLabel.setText("" + LevelManager.lifeCount);
			this.finalLabel.setTranslateX(425);
			this.finalLabel.setTranslateY(-188);
			break;
		//Shows the 'GameOver' screen, as well as setting the final label position to show your final score
		case GAMEOVER:
			Sounds.sPlayer.stopSong();
			view = FXMLLoader.load(getClass().getResource("/application/view/GameOver.fxml"));
			this.finalLabel.setText("" + Score.finalScore);
			this.finalLabel.setTranslateX(230);
			this.finalLabel.setTranslateY(-195);
			break;
		//Shows next level screen, 
		case NEXTLEVEL:
			view = FXMLLoader.load(getClass().getResource("/application/view/NextLevel.fxml"));
			break;
		case YOUWON:
			view = FXMLLoader.load(getClass().getResource("/application/view/YouWon.fxml"));
			this.finalLabel.setTranslateX(230);
			this.finalLabel.setTranslateY(-195);
			this.finalLabel.setText("" + Score.finalScore);
			break;
		}
		//Ensure no duplicate children added to StackPane
		if(StateManager.gameState == State.MAINMENU && !(root.getChildren().contains(gameView) && !(StateManager.prevMenu == State.GAMEOVER)))
			root.getChildren().add(gameView);
		if(!root.getChildren().contains(view))
			root.getChildren().add(view);
		this.finalLabel.toFront();
		primaryStage.show();
	}
	
	
	/**
	 * Processes player keyboard input
	 */
	public void processInput() {
		Main.mainScene.setOnKeyPressed(e ->{
			keyPressed(e, LevelManager.mainGuy);
		});

		Main.mainScene.setOnKeyReleased(e ->{
			keyReleased(e, LevelManager.mainGuy);
		});

	}

	public void update() {
		updateMC();
		if(!LevelManager.mainGuy.isDead() && !LevelManager.mainGuy.isWinning()) {
			updateMovPlats();
			updatePointBoxes();
			col.checkCollision(LevelManager.mainGuy);
			col.checkMovingCollision(LevelManager.mainGuy);
			updateEnemies();
		}
		updateLabels();
	}


	/**
	 * 
	 * @param e -> event listener for clicks on the main menu button
	 * 
	 * sends user to the main menu
	 */
	@FXML
	public void mainMenu(ActionEvent e) {
		StateManager.prevMenu = State.GAMEOVER;
		StateManager.gameState = State.MAINMENU;
		LevelManager.loadLevel();
	}

	/**
	 * starts a new game upon click of the new game button
	 */
	@FXML 
	public void newGame() {
		if(LevelManager.lifeCount == 0) {
			LevelManager.lifeCount = 3;
			StateManager.currentLevel = Level.LEVEL1;
			LevelManager.gameOver();
		}
		LevelManager.mainGuy.setDead(false);
		StateManager.gameState = State.PLAYING;
	}
	
	/**
	 * 
	 * @param e -> event listener for clicks on the play again button
	 */
	@FXML
	public void playAgain(ActionEvent e) {
		StateManager.gameState = State.PLAYING;
		LevelManager.mainGuy.setDead(false);
	}

	@FXML
	public void exitGame() {
		System.exit(0);
	}

	/**
	 * calculates change in time used for FPS and the game loop
	 * @return -> the change in time between frames
	 */
	public double calculate() {
		double current = System.currentTimeMillis();
		delta += (current-lastTime);
		lastTime = current;
		//frameCount++;

		if (delta > 0.15) {
			delta = 0.15;
			//frameRate = String.format("FPS %s", frameCount);
			//frameCount = 0;
		}
		return delta;
	}

	//Update methods

	// updates the main character according to
	// a win, a death, collision, jumping, walking, etc
	public void updateMC() {
		if(LevelManager.mainGuy.isWinning()) {
			LevelManager.mainGuy.animateWin();
		}
		if(LevelManager.mainGuy.isDead()) {
			LevelManager.mainGuy.animateDeath();
		}
		if(LevelManager.mainGuy.gety() > 800 && StateManager.gameState != State.DYING  && StateManager.gameState != State.WINNING) {
			LevelManager.mainGuy.setDead(true);
			LevelManager.mainGuy.deathByHole(LevelManager.level,col.findNearestHole(LevelManager.groundString));
			Sounds.sPlayer.playSFX(1);
		}
		//If LevelManager.mainGuy is not touching top of platform, he must be jumping/falling
		if(!LevelManager.mainGuy.getCollide() && LevelManager.mainGuy.getCollisionDelta() > 100) {
			LevelManager.mainGuy.setJumping(true);
		}

		//If he is jumping or walking, update his movement to match, also prevent max fall speed from exceeding 6.5
		if (LevelManager.mainGuy.getWalking() || LevelManager.mainGuy.getJumping() || LevelManager.mainGuy.getOnMovingPlat()) {
			LevelManager.mainGuy.move();
			LevelManager.level.setTranslateX(LevelManager.level.getTranslateX() - LevelManager.mainGuy.getdx() - LevelManager.mainGuy.getPlatdx());
			if (LevelManager.mainGuy.getJumping() && LevelManager.mainGuy.getdy() < 6.5) {
				LevelManager.mainGuy.setdy(LevelManager.mainGuy.getdy() + (gravity*calculate()));
			}
		}
	}

	public void updateEnemies() {
		//=====================================================
		//Update enemies
		for(int x = 0; x < LevelManager.enemyList.size();x++)
		{
			//Blue enemies jump
			if(LevelManager.enemyList.get(x).getColor() == Color.BLUE)
			{
				if(LevelManager.enemyList.get(x).getJumping())
					LevelManager.enemyList.get(x).setdy(gravity*calculate()+LevelManager.enemyList.get(x).getdy());

				if(LevelManager.enemyList.get(x).getJumping() && LevelManager.enemyList.get(x).gety() > LevelManager.enemyList.get(x).getInitY())
				{
					LevelManager.enemyList.get(x).setdy(0); //down
					LevelManager.enemyList.get(x).setJumping(false);
				}
				int ran = LevelManager.enemyList.get(x).getRNG(1000);
				if(ran >= 0 && ran < 15) //Random chance (15/1000) that an enemy will jump. I think this is per frame, so it's still quite a lot.
				{
					LevelManager.enemyList.get(x).enemyJump();
				}

			}
			//Dark Magenta enemies on platforms
			if(LevelManager.enemyList.get(x).getColor() == Color.DARKMAGENTA)
			{
				//Swap directions if they're about to move off of their platform. Platform size is 90 rn, so they move 70 pixels left or right
				//then swap.
				if(LevelManager.enemyList.get(x).getx() >= LevelManager.enemyList.get(x).getInitialX()+LevelManager.tileWidth-20 || LevelManager.enemyList.get(x).getx() <= LevelManager.enemyList.get(x).getInitialX()-LevelManager.tileWidth+20)
				{
					LevelManager.enemyList.get(x).swapDir();
					LevelManager.enemyList.get(x).setInitialX(LevelManager.enemyList.get(x).getx());
				}
			}
			LevelManager.enemyList.get(x).enemyMove();
			//Check collision with obstacles/platforms
			col.checkCollision(LevelManager.enemyList.get(x)); 
			col.groundCheck(LevelManager.enemyList.get(x), LevelManager.groundString); //Swap enemy direction when close to a hole.

			//Check collision with the player
			if(LevelManager.enemyList.get(x).collide(LevelManager.mainGuy.getx(),LevelManager.mainGuy.gety(),LevelManager.mainGuy.getRadius(),LevelManager.mainGuy.getRadius()))
			{
				if(LevelManager.mainGuy.getdy() <= 0) {
					LevelManager.mainGuy.setDead(true);
					Sounds.sPlayer.playSFX(1);
					LevelManager.mainGuy.setAnimating(true);
					LevelManager.mainGuy.deathByEnemy();
					System.out.println("FinalLabel text: " + this.finalLabel.getText());
					LevelManager.mainGuy.setdx(0);
				}
				else {
					LevelManager.mainGuy.setdy(-3);
					Score.finalScore += 300;
					FloatLabel scoreLabel = new FloatLabel("+300", 20, -20, LevelManager.enemyList.get(x).getx(), LevelManager.enemyList.get(x).gety()-50);
					scoreLabel.setStartEndXY(scoreLabel.getTranslateX(), scoreLabel.getTranslateY());
					LevelManager.scoreLabels.add(scoreLabel);
					LevelManager.level.getChildren().add(LevelManager.scoreLabels.get(LevelManager.scoreLabels.size()-1));
							
				}
				LevelManager.enemyList.get(x).getCharacter().setFill(Color.YELLOW);
				LevelManager.enemyList.get(x).getCharacter().setCenterY(-1000);
				LevelManager.enemyList.remove(x);
				Sounds.sPlayer.playSFX(3);
			}
			else
				LevelManager.enemyList.get(x).getCharacter().setFill(LevelManager.enemyList.get(x).getColor());

		}

	}

	public void updateMovPlats() {
		for(MovingObstacle obstacle : LevelManager.allMovingObjects) {
			if(obstacle.isMoving()) {
				obstacle.move();
			}
		}
	}

	public void updateLabels() {
		if(StateManager.gameState == State.PLAYING) {
			LevelManager.infoLabel.setText("Level " + StateManager.currentLevel.ordinal() + "\n\nScore: " + Score.finalScore);
			LevelManager.infoLabel.setTranslateX(LevelManager.mainGuy.getCharacter().getTranslateX()+40);
			LevelManager.lifeCounter.setTranslateY(LevelManager.infoLabel.getTranslateY()+65);
			LevelManager.lifeCounter.setTranslateX(LevelManager.infoLabel.getTranslateX());
		}
		LevelManager.floatLabels();
	}

	public void updatePointBoxes() {
		for(PointBox pBox : LevelManager.pointBoxList) {
			if(pBox.isAnimating()) {
				pBox.animateCube();
			}
		}
	}
}
