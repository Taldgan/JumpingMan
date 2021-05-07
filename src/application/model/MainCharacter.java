package application.model;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;


/**
 * Main Character class. Creates a Character that has a life count, a hat, and animation states.
 * Contains methods for animating a 'win' (sliding down flagpole), animating a death, and
 * methods for respawning a Main Character depending on how they died.
 * @author Thomas White, Gabriel Pastelero, Caleb Kopecky
 *
 */
public class MainCharacter extends Character {

	private double predeathX, predeathY, predeathTranslateX, predeathTranslateY;
	private double hatPrevTranslateX, hatPrevTranslateY;
	private Group hat;
	private int winPlatX;

	private int lives;

	private boolean animatingWinG = false;
	private boolean animating = false;
	private int deathAnimationCount = 150, winAnimationCount = 0;


	/**
	 * MainCharacter constructor. Adds a 'lives' integer to the base Character constructor.
	 * @param x - x position of the MainCharacter's spawn
	 * @param y - y position of the MainCharacter's spawn
	 * @param size - radius of the MainCharacter
	 * @param color - color of the MainCharacter
	 * @param lives - number of lives the MainCharacter starts with
	 */
	public MainCharacter(double x, double y, double size, Color color, int lives) {
		super(x, y, size, color);
		this.lives = lives;
		setx(x);
		sety(y);
		setSize(size);
		setColor(color);
		setCharacter(x, y, size, color);
		//Create hat
		Ellipse hatBase = new Ellipse();
		Ellipse hatFront = new Ellipse();
		hatBase.setRadiusX(size);
		hatBase.setRadiusY(size/2);
		hatBase.setTranslateX(x+4);
		hatBase.setTranslateY(y-14);
		hatBase.setFill(Color.BLACK);
		hatFront.setRadiusX(size/1.2);
		hatFront.setRadiusY(size/2.5);
		hatFront.setTranslateX(x+9);
		hatFront.setTranslateY(y-7);
		hatFront.setFill(Color.web("0x33232D"));
		Label j = new Label("J");
		j.setTextFill(Color.WHITE);
		j.setTranslateX(hatFront.getTranslateX());
		j.setTranslateY(hatFront.getTranslateY()-9);
		//Finish hat and add it to hat group
		hat = new Group(hatBase, hatFront, j);
	}

	/**
	 * If player is killed by an enemy, assign it's previous x and y positions pre-death for the animateDeath method's use, and decrement the life counter.
	 */
	public void deathByEnemy() {
		//Set location pre-death
		predeathX = getx();
		predeathY = gety();
		predeathTranslateX = character.getTranslateX();
		predeathTranslateY = character.getTranslateY();
		//Should always run, decrement life value in LevelManager and decrease visual life counter
		if (isDead()) {
			setLives(getLives() - 1);
			LevelManager.lifeCount--;
			if(getLives() >= 0)
				LevelManager.lifeCounter.getChildren().remove(getLives());
			StateManager.gameState = State.DYING;
		}
	}

	/**
	 * If player died by falling in a hole, decrement the life counter and respawn it at the nearest 'safe' ground (x left of hole, y top of ground left of hole)
	 * 
	 * @param group - the level group the character is respawning at, used to make sure background is scrolled properly to the respawn point
	 * @param respawnX - X location to respawn the character at. Helpful for determining tile # and y offset of that tile (for char y respawn)
	 */
	public void deathByHole(Group group, int respawnX) {
		int oldX = (int) getx();
		setx(respawnX);
		int newX = (int) getx();
		this.getCharacter().setTranslateX(this.getCharacter().getTranslateX() - (oldX-newX));
		this.getHat().setTranslateX(this.getHat().getTranslateX() - (oldX-newX));
		int safeTile = respawnX/LevelManager.tileWidth;
		character.setTranslateY(-1*LevelManager.groundOffsets.get(safeTile));
		hat.setTranslateY(-1*LevelManager.groundOffsets.get(safeTile));
		group.setTranslateX(group.getTranslateX() + (oldX-newX));
		predeathX = respawnX;
		if (isDead()) {
			setdy(0);
			setdx(0);
			setLives(getLives() - 1);
			LevelManager.lifeCount--;
			if(getLives() != -1)
				LevelManager.lifeCounter.getChildren().remove(getLives());
			if(getLives() <= 0) {
				StateManager.gameState = State.GAMEOVER;
			}
			else
				StateManager.gameState = State.YOUDIED;
		}
	}

	/**
	 * Sets the win animation tick count
	 * @param count - the win animation count
	 */
	public void setWinACount(int count) {
		this.winAnimationCount = count;
	}

	/**
	 * Animates a flag-pole slide animation for the player. Player dynamically slides down the flag-pole until he hits the ground. 
	 * When he does, he waits until jumping just before the "Next LeveL" screen pops up. Properly sets up GUI and level state once 
	 * the animation ends.
	 */
	public void animateWin() {
		//Stop player from moving left/right
		setdx(0);
		//While animation is running...
		if(winAnimationCount > 0) {
			//At the beginning, move player position to the middle of the flag pole
			if(winAnimationCount >= 509) {
				character.setTranslateX(winPlatX-225);
				hat.setTranslateX(winPlatX-225);
			}
			//Stop player from falling while animation is running
			setdy(0);
			//Every 15 ticks, swap the player direction to make it move back and forth around the flag pole
			if(winAnimationCount % 15 == 0) {
				swapDir();
			}
			//While player is above the ground, move him down and left/right
			if(gety()+character.getRadius() <= LevelManager.groundLevel && !animatingWinG) {
				if(movingRight()) {
					character.setTranslateX(character.getTranslateX() + 3);
					hat.setTranslateX(hat.getTranslateX() + 3);
				}
				else {
					character.setTranslateX(character.getTranslateX() - 3);
					hat.setTranslateX(hat.getTranslateX() - 3);
				}
				character.setTranslateY(character.getTranslateY() + 2.5);
				hat.setTranslateY(hat.getTranslateY() + 2.5);
			}
			//Once he hits the ground, wait til the end of the animation, then hop
			else {
				if(winAnimationCount == 40) {
					animatingWinG = true;
					Sounds.sPlayer.playSFX(0);
				}
				else if(winAnimationCount < 20) {
					character.setTranslateY(character.getTranslateY() + 2.5);
					hat.setTranslateY(hat.getTranslateY() + 2.5);
				}
				else if(winAnimationCount < 40) {
					character.setTranslateY(character.getTranslateY() - 2.5);
					hat.setTranslateY(hat.getTranslateY() - 2.5);
				}
			}
			winAnimationCount--;
		}
		//Once animation is over, set up the GUI and StateManager states to either next level, or you win
		else {
			if(StateManager.currentLevel == Level.END) {
				LevelManager.lifeCount = 3;
				StateManager.gameState = State.YOUWON;
				StateManager.currentLevel = Level.LEVEL1;
				LevelManager.levelOver();
			}
			else {
				StateManager.gameState = State.NEXTLEVEL;
				LevelManager.levelOver();
			}
			LevelManager.loadLevel();
		}
	}

	/**
	 * Animates the player dying. Player rises up (hit reaction), then falls quickly through the ground. His hat follows behind him.
	 * Once the animation ends, properly set up the GUI and game state depending on life count of the player.
	 */
	public void animateDeath() {
		//Beginning of animation, raise up the player with his hat
		if(deathAnimationCount >= 120) {
			this.getCharacter().setTranslateY(this.getCharacter().getTranslateY()-0.5);
			this.getHat().setTranslateY(this.getHat().getTranslateY()-0.5);
			deathAnimationCount--;
		}
		//Slightly after, player begins falling
		else if(deathAnimationCount > 110) {
			this.getCharacter().setTranslateY(this.getCharacter().getTranslateY()+8);
			deathAnimationCount--;
		}
		//Then hat begins falling with the player
		else if(deathAnimationCount > 0) {
			this.getCharacter().setTranslateY(this.getCharacter().getTranslateY()+8);
			this.getHat().setTranslateY(this.getHat().getTranslateY()+8);
			deathAnimationCount--;
		}
		//When animation is over, set up the GUI and game state depending on player life count
		else {
			this.deathAnimationCount = 150;
			this.animating = false;
			if(!animating) {
				setdx(0);
				setdy(0);
				this.getCharacter().setTranslateX(predeathTranslateX);
				this.getCharacter().setTranslateY(predeathTranslateY);
				setx(predeathX);
				sety(predeathY);
				if(getLives() > 0) 
					StateManager.gameState = State.YOUDIED;
				else {
					StateManager.gameState = State.GAMEOVER;
					StateManager.currentLevel = Level.LEVEL1;
				}
			}
		}
	}

	/**
	 * Moves the player based off of his dx/dy & platform dx/dy.
	 * Same as Character.move(), just with the addition of the hat.
	 */
	public void move() {
		super.move();
		hatPrevTranslateX = this.getHat().getTranslateX();
		hatPrevTranslateY = this.getHat().getTranslateY();
		hat.setTranslateX(this.hat.getTranslateX() + getdx() + getPlatdx());
		hat.setTranslateY(this.hat.getTranslateY() + getdy() + getPlatdy());
	}

	/**
	 * Gets the MainCharacter's hat as a group
	 * @return Group - MainCharacter's hat as a group
	 */
	public Group getHat() {
		return this.hat;
	}

	/**
	 * Gets the previous translateX value of the MainCharacter's hat before it moved.
	 * @return double - the hat's previous translateX before it moved.
	 */
	public double getHatPrevTranslateX() {
		return hatPrevTranslateX;
	}

	/**
	 * Gets the previous translateY value of the MainCharacter's hat before it moved.
	 * @return double - the hat's previous translateY before it moved.
	 */
	public double getPrevHatTranslateY() {
		return hatPrevTranslateY;
	}

	/**
	 * Returns true if the player is in an animation state
	 * @return animating - true if the player is in an animation state
	 */
	public boolean isAnimating() {
		return animating;
	}

	/**
	 * Sets whether or not the player is in an animation state
	 * @param animating - whether or not the player is in an animation state
	 */
	public void setAnimating(boolean animating) {
		this.animating = animating;
	}

	/**
	 * Get x location of the winning platform the player touched
	 * @return - the x location of the winning platform the player touched 
	 */
	public int getWinPlatX() {
		return this.winPlatX;
	}

	/**
	 * Sets the x location of the winning platform the player touched
	 * @return - the x location of the winning platform the player touched 
	 */
	public void setWinPlatX(int x) {
		this.winPlatX = x;
	}

	/**
	 * Sets the number of lives the player has left
	 * @param lives - the number of lives the player has left
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * Gets the number of lives the player has left
	 * @param lives - the number of lives the player has left
	 */
	public int getLives() {
		return this.lives;
	}

}
