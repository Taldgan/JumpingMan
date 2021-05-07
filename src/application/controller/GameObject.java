package application.controller;

import java.io.IOException;

import application.Main;
import application.model.Character;
import application.model.Enemies;
import application.model.FloatLabel;
import application.model.InputFunctions;
import application.model.Level;
import application.model.LevelManager;
import application.model.MainCharacter;
import application.model.MovingObstacle;
import application.model.Obstacle;
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
			checkCollision(LevelManager.mainGuy);
			checkMovingCollision(LevelManager.mainGuy);
			updateEnemies();
		}
		updateLabels();
	}


	@FXML
	public void mainMenu(ActionEvent e) {
		StateManager.prevMenu = State.GAMEOVER;
		StateManager.gameState = State.MAINMENU;
		LevelManager.loadLevel();
	}

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
	
	@FXML
	public void playAgain(ActionEvent e) {
		StateManager.gameState = State.PLAYING;
		LevelManager.mainGuy.setDead(false);
	}

	@FXML
	public void exitGame() {
		System.exit(0);
	}

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

	public void checkCollision(Character c)
	{
		//character bound variables for readability
		double charTop = c.gety()-c.getCharacter().getRadius();
		double charBot = c.gety()+c.getCharacter().getRadius();
		double charLeft = c.getx()+c.getCharacter().getRadius();
		double charRight = c.getx()-c.getCharacter().getRadius();
		double charRad = c.getCharacter().getRadius();

		for(Obstacle obstacle : LevelManager.allStaticObjects) {
			if(obstacle.collide(c.getx(), c.gety(), charRad, charRad)) {
				//Win if on last obstacle
				if(obstacle.getColor() == Color.WHITESMOKE && c instanceof MainCharacter) {
					double poleScore = Math.abs(LevelManager.mainGuy.gety()-800);
					Score.finalScore += poleScore;
					FloatLabel scoreLabel = new FloatLabel("+" + (int) poleScore, 80, -80, LevelManager.mainGuy.getx()-40, LevelManager.mainGuy.gety(), (int) (poleScore/3.5));
					scoreLabel.setStartEndXY(scoreLabel.getTranslateX(), scoreLabel.getTranslateY());
					LevelManager.scoreLabels.add(scoreLabel);
					LevelManager.level.getChildren().add(LevelManager.scoreLabels.get(LevelManager.scoreLabels.size()-1));
					LevelManager.mainGuy.setWinPlatX((int) obstacle.getX());
					nextLevel();
				}
				double diff;
				//On top of the platform
				if(charBot-12 <= obstacle.getY() && c.getdy() >= 0)
				{
					diff = LevelManager.level.getTranslateY() + (c.gety() - c.getPrevY());
					c.setCollide(true);
					if(c instanceof MainCharacter)
					{
						LevelManager.mainGuy.setGroundLevel(c.gety());
						LevelManager.mainGuy.setdy(0);
						LevelManager.mainGuy.setJumping(false);
						LevelManager.mainGuy.getCharacter().setTranslateY(obstacle.getPlat().getY()-LevelManager.groundLevel+5);
						LevelManager.mainGuy.getHat().setTranslateY(obstacle.getPlat().getY()-LevelManager.groundLevel+5);
					}
				}
				//Left of platform collision:
				if(charLeft <= obstacle.getX()+15 && !(charBot-12 <= obstacle.getY()+10)) {

					c.setCollideRight(true);
					diff = LevelManager.level.getTranslateX() + (c.getx() - c.getPrevX());
					if(c instanceof MainCharacter)
					{
						LevelManager.mainGuy.setx(LevelManager.mainGuy.getPrevX());
						LevelManager.mainGuy.getCharacter().setTranslateX(LevelManager.mainGuy.getPrevTranslateX());
						LevelManager.mainGuy.getHat().setTranslateX(LevelManager.mainGuy.getHatPrevTranslateX());
						LevelManager.level.setTranslateX(diff);
					}
					//Swap enemy direction when touching an obstacle.
					else if(!(c instanceof MainCharacter)) //Dont ask how, dont ask why. But it just works.
					{
						c.swapDir();
					}

				}
				//Right of platform collision:
				else if(charRight >= obstacle.getX()+obstacle.getWidth()-15 && !(charBot-12 <= obstacle.getY()+10)) {
					c.setCollideLeft(true);
					diff = LevelManager.level.getTranslateX() + (c.getx() - c.getPrevX());
					if(c instanceof MainCharacter)
					{
						LevelManager.mainGuy.setx(c.getPrevX());
						LevelManager.mainGuy.getCharacter().setTranslateX(LevelManager.mainGuy.getPrevTranslateX());
						LevelManager.mainGuy.getHat().setTranslateX(LevelManager.mainGuy.getHatPrevTranslateX());
						LevelManager.level.setTranslateX(diff);
					}
					else if(!(c instanceof MainCharacter))
					{
						c.swapDir();
					}

				}
				//If under the platform:
				else if(charTop >= obstacle.getY() && c.getdy() <= 0)
				{
					LevelManager.mainGuy.setCollideTop(true);
					
					// width == height means its a prize box
					if (obstacle instanceof PointBox) {
						((PointBox) obstacle).getHit();
					}
					diff = LevelManager.level.getTranslateY() + (c.gety() - c.getPrevY());

					c.setdy(1);
					if(c instanceof MainCharacter)
					{
						LevelManager.mainGuy.sety(c.getPrevY());
						LevelManager.mainGuy.getCharacter().setTranslateY(LevelManager.mainGuy.getPrevTranslateY());
						LevelManager.mainGuy.getHat().setTranslateY(LevelManager.mainGuy.getPrevHatTranslateY());
					}
				}
			}
			else {
				c.setCollide(false);
				LevelManager.mainGuy.setCollideLeft(false);
				LevelManager.mainGuy.setCollideRight(false);
				LevelManager.mainGuy.setCollideTop(false);
			}
		}
		
	}
	
	public void checkMovingCollision(Character c) {
		//character bound variables for readability
		double charBot = c.gety()+c.getCharacter().getRadius();
		double charRad = c.getCharacter().getRadius();
		//moving platforms... add velocity to player
		for(MovingObstacle obstacle : LevelManager.allMovingObjects) {

			if(obstacle.collide(c.getx(), c.gety(), charRad, charRad)) {
				//On top of the platform
				if(charBot-12 <= obstacle.getY() && c.getdy() >= 0)
				{
					c.setCollide(true);
					if(c instanceof MainCharacter)
					{
						LevelManager.mainGuy.setGroundLevel(LevelManager.mainGuy.gety());
						LevelManager.mainGuy.setdy(0);
						LevelManager.mainGuy.setPlatdx(obstacle.getdx());
						LevelManager.mainGuy.setPlatdy(obstacle.getdy());
						LevelManager.mainGuy.getCharacter().setTranslateY(c.getCharacter().getTranslateY() + obstacle.getdy());
						LevelManager.mainGuy.getHat().setTranslateY(LevelManager.mainGuy.getHat().getTranslateY() + obstacle.getdy());
						LevelManager.mainGuy.setJumping(false);
						LevelManager.mainGuy.setOnMovingPlat(true);
					}
				}
			}
			else {
				c.setCollide(false);
			}
			if(c.getCollisionDelta() > 100) {
				c.setPlatdx(0);
				c.setPlatdy(0);
				c.setOnMovingPlat(false);
			}
		}
	}
	

	//Method for enemies to turn around if they are next to a hole.
	public void groundCheck(Enemies e, String holes)
	{
		for(int x = 0; x < holes.length(); x++)
		{
			if(holes.charAt(x) != e.getZoneCode())
			{
				double holeRight = LevelManager.tileWidth*(x+1);
				double holeLeft = LevelManager.tileWidth*x;
				double offset = 9;
				if(e.getx() >= holeLeft-offset && e.getx() <= holeRight+offset && e.getColor() != Color.DARKMAGENTA)
				{
					e.swapDir();
					break;
				}
			}
		}
	}

	public int findNearestHole(String holes)
	{
		int pos = (int)LevelManager.mainGuy.getx()/(LevelManager.tileWidth-1); //position in string
		for(int x = pos; x >= 0; x--)
		{
			if(holes.charAt(x) != '0')
			{
				int spawnPoint = (x+1)*LevelManager.tileWidth-20;
				return spawnPoint;
			}
		}
		return 250; //Should never reach here.
	}


	//Update methods

	public void updateMC() {
		if(LevelManager.mainGuy.isWinning()) {
			LevelManager.mainGuy.animateWin();
		}
		if(LevelManager.mainGuy.isDead()) {
			LevelManager.mainGuy.animateDeath();
		}
		if(LevelManager.mainGuy.gety() > 800 && StateManager.gameState != State.DYING  && StateManager.gameState != State.WINNING) {
			LevelManager.mainGuy.setDead(true);
			LevelManager.mainGuy.deathByHole(LevelManager.level,findNearestHole(LevelManager.groundString));
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
			checkCollision(LevelManager.enemyList.get(x)); 
			groundCheck(LevelManager.enemyList.get(x), LevelManager.groundString); //Swap enemy direction when close to a hole.

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

	public void nextLevel() {
		StateManager.currentLevel = Level.values()[StateManager.currentLevel.ordinal()+1];
		Sounds.sPlayer.playSFX(4);
		Sounds.sPlayer.stopSong();
		LevelManager.mainGuy.setWinning(true);
		LevelManager.mainGuy.setWinACount(510);
		StateManager.gameState = State.WINNING;
	}

	public void updatePointBoxes() {
		for(PointBox pBox : LevelManager.pointBoxList) {
			if(pBox.isAnimating()) {
				pBox.animateCube();
			}
		}
	}
}
