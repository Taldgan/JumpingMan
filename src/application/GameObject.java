package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameObject extends InputFunctions{

	//Scenes
	BorderPane root;
	Scene menuScene;
	Scene gameScene;
	Scene deadScene;
	Scene winScene;
	Scene gameOverScene;

	double lastTime = System.currentTimeMillis();
	double delta;
	double gravity = 1;

	public void processInput() {

		this.gameScene.setOnKeyPressed(e ->{
			keyPressed(e, LevelManager.mainGuy);
		});

		this.gameScene.setOnKeyReleased(e ->{
			keyReleased(e, LevelManager.mainGuy);
		});

	}

	public void update() {
		updateMC();
		updateMovPlats();
		updatePointBoxes();
		checkCollision(LevelManager.mainGuy);
		checkMovingCollision(LevelManager.mainGuy);
		updateEnemies();
		updateLabels();
	}


	@FXML
	public void mainMenu(ActionEvent e) {
		StateManager.prevMenu = State.GAMEOVER;
		StateManager.gameState = State.MAINMENU;
	}

	@FXML 
	public void newGame(ActionEvent event) {
		if(LevelManager.lifeCount == 0) {
			LevelManager.lifeCount = 3;
			StateManager.currentLevel = Level.LEVEL1;
		}
		LevelManager.loadLevel();
		LevelManager.mainGuy.play();
		LevelManager.mainGuy.setDead(false);
		StateManager.gameState = State.PLAYING;
	}

	@FXML
	public void playAgain(ActionEvent e) {
		LevelManager.mainGuy.play();
		StateManager.gameState = State.PLAYING;
		LevelManager.mainGuy.setDead(false);
	}

	@FXML
	public void exitGame() {
		System.exit(0);
	}

	public void render(Stage primaryStage) throws IOException {
		Parent view;
		switch(StateManager.gameState) {
		case MAINMENU:
			view = FXMLLoader.load(getClass().getResource("/application/MainMenu.fxml"));
			this.menuScene = new Scene(view);
			primaryStage.setScene(this.menuScene);
			Sounds.sPlayer.stopSong();
			break;
		case PAUSE:
			LevelManager.pauseLabel.setTranslateX(LevelManager.mainGuy.getCharacter().getTranslateX()+400);
			LevelManager.level.getChildren().add(LevelManager.pauseLabel);
			break;
		case PLAYING:
			LevelManager.infoLabel.setTranslateX(LevelManager.mainGuy.getCharacter().getTranslateX());
			if(!LevelManager.level.getChildren().contains(LevelManager.infoLabel)) 
				LevelManager.level.getChildren().add(LevelManager.infoLabel);
			if(!LevelManager.level.getChildren().contains(LevelManager.lifeCounter)) 
				LevelManager.level.getChildren().add(LevelManager.lifeCounter);
			Sounds.sPlayer.playSong(0);
			this.root = new BorderPane(LevelManager.level);
			this.gameScene = new Scene(root);
			if(LevelManager.level.getChildren().contains(LevelManager.pauseLabel))
				LevelManager.level.getChildren().remove(LevelManager.pauseLabel);
			primaryStage.setScene(this.gameScene);
			break;
		case YOUDIED:
			Sounds.sPlayer.stopSong();
			Sounds.sPlayer.playSFX(1);
			view = FXMLLoader.load(getClass().getResource("/application/YouDied.fxml"));
			this.deadScene = new Scene(view);
			primaryStage.setScene(this.deadScene);
			break;
		case GAMEOVER:
			Sounds.sPlayer.stopSong();
			view = FXMLLoader.load(getClass().getResource("/application/GameOver.fxml"));
			this.gameOverScene = new Scene(view);
			primaryStage.setScene(this.gameOverScene);
			break;
		case NEXTLEVEL:
			view = FXMLLoader.load(getClass().getResource("/application/NextLevel.fxml"));
			this.winScene = new Scene(view);
			primaryStage.setScene(this.winScene);
			break;
		case YOUWON:
			view = FXMLLoader.load(getClass().getResource("/application/YouWon.fxml"));
			this.winScene = new Scene(view);
			primaryStage.setScene(this.winScene);
			break;
		}
		primaryStage.show();
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
				if(obstacle.getColor() == Color.WHITESMOKE) { //If you wanna change the color for the winning platform, then make sure to change it in the spawn method too
					nextLevel();
				}
				double diff;
				//On top of the platform
				if(charBot-12 <= obstacle.getY() && c.getdy() >= 0)
				{
					diff = LevelManager.level.getTranslateY() + (c.gety() - c.getPrevY());
					c.setCollide(true);
					if(c.getColor() == Color.RED)
					{
						c.setGroundLvl(c.gety());
						c.setdy(0);
						c.setJumping(false);
						c.getCharacter().setTranslateY(obstacle.getPlat().getY()-LevelManager.groundLevel+5);
					}
				}
				//Left of platform collision:
				if(charLeft <= obstacle.getX()+15 && !(charBot-12 <= obstacle.getY()+10)) {

					c.setCollideRight(true);
					diff = LevelManager.level.getTranslateX() + (c.getx() - c.getPrevX());
					if(c.getColor() == Color.RED)
					{
						c.setx(c.getPrevX());
						c.getCharacter().setTranslateX(LevelManager.mainGuy.getPrevTranslateX());
						LevelManager.level.setTranslateX(diff);
					}
					//Swap enemy direction when touching an obstacle.
					else if(c.getColor() != Color.RED && obstacle.getColor() == null) //Dont ask how, dont ask why. But it just works.
					{
						c.swapDir();
					}

				}
				//Right of platform collision:
				else if(charRight >= obstacle.getX()+obstacle.getWidth()-15 && !(charBot-12 <= obstacle.getY()+10)) {
					c.setCollideLeft(true);
					diff = LevelManager.level.getTranslateX() + (c.getx() - c.getPrevX());
					if(c.getColor() == Color.RED)
					{
						c.setx(c.getPrevX());
						c.getCharacter().setTranslateX(LevelManager.mainGuy.getPrevTranslateX());
						LevelManager.level.setTranslateX(diff);
					}
					else if(c.getColor() != Color.RED && obstacle.getColor() == null)
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
					if(c.getColor() == Color.RED)
					{
						c.sety(c.getPrevY());
						c.getCharacter().setTranslateY(LevelManager.mainGuy.getPrevTranslateY());
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
					if(c.getColor() == Color.RED)
					{
						c.setGroundLvl(c.gety());
						c.setdy(0);
						c.setPlatdx(obstacle.getdx());
						c.setPlatdy(obstacle.getdy());
						c.getCharacter().setTranslateY(c.getCharacter().getTranslateY() + obstacle.getdy());
						c.setJumping(false);
						c.setOnMovingPlat(true);
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
		int pos = (int)LevelManager.mainGuy.getx()/LevelManager.tileWidth-1; //position in string
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
		if(LevelManager.mainGuy.getDead() || LevelManager.mainGuy.gety() > 800)
			LevelManager.mainGuy.dead(LevelManager.level,findNearestHole(LevelManager.groundString));
		//If LevelManager.mainGuy is not touching top of platform, he must be jumping/falling
		if(!LevelManager.mainGuy.getCollide() && LevelManager.mainGuy.getCollisionDelta() > 100) {
			LevelManager.mainGuy.setJumping(true);
		}

		//If he is jumping or walking, update his movement to match, also prevent max fall speed from exceeding 6.5
		if (LevelManager.mainGuy.walking || LevelManager.mainGuy.jumping || LevelManager.mainGuy.getOnMovingPlat()) {
			LevelManager.mainGuy.move();
			LevelManager.level.setTranslateX(LevelManager.level.getTranslateX() - LevelManager.mainGuy.getdx() - LevelManager.mainGuy.getPlatdx());
			if (LevelManager.mainGuy.jumping && LevelManager.mainGuy.getdy() < 6.5) {
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
					LevelManager.mainGuy.setdx(0);
				}
				else {
					LevelManager.mainGuy.setdy(-3);
					
				}
				LevelManager.enemyList.get(x).getCharacter().setFill(Color.YELLOW);
				LevelManager.enemyList.get(x).getCharacter().setCenterY(-1000);
				LevelManager.enemyList.remove(x);

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
		LevelManager.infoLabel.setText("Level " + StateManager.currentLevel.ordinal());
		LevelManager.infoLabel.setTranslateX(LevelManager.mainGuy.getCharacter().getTranslateX()+40);
		LevelManager.lifeCounter.setTranslateY(LevelManager.infoLabel.getTranslateY()+65);
		LevelManager.lifeCounter.setTranslateX(LevelManager.infoLabel.getTranslateX());
	}

	public void win() {
		System.out.println("Score: " + LevelManager.mainGuy.score.calculateScore(LevelManager.mainGuy.getLives()));
		LevelManager.lifeCount = 3;
		StateManager.gameState = State.YOUWON;
		StateManager.currentLevel = Level.LEVEL1;
	}
	
	public void nextLevel() {
		if(Level.values()[StateManager.currentLevel.ordinal()+1] != Level.END) {
			StateManager.currentLevel = Level.values()[StateManager.currentLevel.ordinal()+1];
			StateManager.gameState = State.NEXTLEVEL;
			LevelManager.mainGuy.score.stop(LevelManager.mainGuy.getLives());
		}
		else {
			win();
		}
	}

	public void updatePointBoxes() {
		for(PointBox pBox : LevelManager.pointBoxList) {
			pBox.floatLabels();
		}
	}
}
