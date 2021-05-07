package application.model;

import javafx.scene.paint.Color;

/**
 * 
 * @author Caleb Kopecky, Thomas White, Gabriel Pastelero
 * 
 * The Collision class is used in the Controller (GameObject)
 * to detect collision with enemies, obstacles, walls, point boxes,
 * platforms and anything else you shouldn't be able to walk through in the game
 * 
 *
 */

public class Collision {
	
	// this method advances the player to the next level upon touching the white pole at the end of the level
	public void nextLevel() {
		StateManager.currentLevel = Level.values()[StateManager.currentLevel.ordinal()+1];
		Sounds.sPlayer.playSFX(4);
		Sounds.sPlayer.stopSong();
		LevelManager.mainGuy.setWinning(true);
		LevelManager.mainGuy.setWinACount(510);
		StateManager.gameState = State.WINNING;
	}

	/**
	 * @param c -> the Character object
	 * 
	 * determines if the character is touching something in the game
	 * and restricts movement if so
	 * 
	 * also determines if the player has beat a level
	 */
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
	
	/**
	 * 
	 * @param c -> Character object 
	 * 
	 * detects collision with moving objects
	 * restricts movement if there is collision
	 */
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
	

	/**
	 * 
	 * @param e -> The enemy object
	 * @param holes -> string representing the ground. 0 = no ground 
	 */
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

	/**
	 * 
	 * @param holes -> string for the ground: 0 = hole
	 * @return -> the nearest x coordinate for the player to respawn
	 * 				upon falling down a hole
	 */
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
}
