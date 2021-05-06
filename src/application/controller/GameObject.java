package application.controller;

import application.model.Character;
import application.model.Enemies;
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
import application.view.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class GameObject extends InputFunctions{
	
	@FXML Label finalScore;

	double lastTime = System.currentTimeMillis();
	double delta;
	double gravity = 1;

	public void processInput() {

		Main.gameScene.setOnKeyPressed(e ->{
			keyPressed(e, LevelManager.mainGuy);
		});

		Main.gameScene.setOnKeyReleased(e ->{
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
			updateLabels();
		}
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
			LevelManager.gameOver();
		}
		LevelManager.loadLevel();
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
				if(obstacle.getColor() == Color.WHITESMOKE && c instanceof MainCharacter) { //If you wanna change the color for the winning platform, then make sure to change it in the spawn method too
					
					//might need to move this somewhere else but idrk where else it would work
					//System.out.println("pole score: " + Math.abs(LevelManager.mainGuy.gety()-800));
					Score.finalScore += Math.abs(LevelManager.mainGuy.gety()-800);
					
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
					LevelManager.mainGuy.setdx(0);
				}
				else {
					LevelManager.mainGuy.setdy(-3);
					Score.finalScore += 300;
					
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
		LevelManager.infoLabel.setText("Level " + StateManager.currentLevel.ordinal() + "\n\nScore: " + Score.finalScore);
		LevelManager.infoLabel.setTranslateX(LevelManager.mainGuy.getCharacter().getTranslateX()+40);
		LevelManager.lifeCounter.setTranslateY(LevelManager.infoLabel.getTranslateY()+65);
		LevelManager.lifeCounter.setTranslateX(LevelManager.infoLabel.getTranslateX());
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
			pBox.floatLabels();
			if(pBox.isAnimating()) {
				pBox.animateCube();
			}
		}
	}
}
