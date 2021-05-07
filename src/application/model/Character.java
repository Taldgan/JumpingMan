package application.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Character class, creates a Circular character object, with various methods for controlling its movement and state.
 * Works as a base for the Enemy and MainCharacter classes.
 * 
 * @author Gabriel Pastelero, Caleb Kopecky, Thomas White
 *
 */
public class Character {

	private boolean dead;
	private boolean winning = false;
	private boolean jumping, walking;
	private double dx, dy, platdx = 0, platdy = 0;
	boolean onMovingPlat = false;
	private double x, y, minY = 280;
	private double prevX, prevY, prevTranslateX, prevTranslateY, size, radius;
	private double groundLevel;
	Color color;
	Circle character = new Circle(x, y, size, color);
	private boolean collide, collideRight, collideLeft, collideTop, collideBottom;
	private boolean dir;
	private double collisionTimeDelta = 0, startTime = 0;

	/**
	 * Character constructor. Sets x/y position, radius, color, initial direction and ground level for the character.
	 * @param x - x position of the Character's spawn
	 * @param y - y position of the Character's spawn
	 * @param size - radius of the Character's circle
	 * @param color - color of the Character
	 */
	public Character(double x, double y, double size, Color color) {
		dead = false;
		jumping = false;
		walking = false;
		collide = false;
		collideLeft = false;
		collideRight = false;
		radius = size;
		groundLevel = 280;
		dir = true;
		setx(x);
		sety(y);
		setSize(size);
		setColor(color);
		setCharacter(x, y, size, color);
	}

	/**
	 * Moves the Character based off of it's dx/dy, setting previous positions before movement in case of a collision.
	 */
	public void move() {
		prevX = this.x;
		prevY = this.y;
		prevTranslateX = this.getCharacter().getTranslateX();
		prevTranslateY = this.getCharacter().getTranslateY();
		this.character.setTranslateX(this.character.getTranslateX() + getdx() + getPlatdx());
		setx(this.character.getCenterX() + this.character.getTranslateX());
		this.character.setTranslateY(this.character.getTranslateY() + getdy() + getPlatdy());
		sety(this.character.getCenterY() + this.character.getTranslateY());
	}

	/**
	 * Returns the graphical Circle of the character
	 * @return - graphical Circle of the character
	 */
	public Circle getCharacter() {
		return this.character;
	}

	/**
	 * Sets the circle of the Character to a position (and color)
	 * @param x - x position to set the Character to
	 * @param y  - y position to set the Character to
	 * @param size - radius of the Character
	 * @param color - color to set the Character to
	 */
	public void setCharacter(double x, double y, double size, Color color) {
		this.character.setCenterX(x);
		this.character.setCenterY(y);
		this.character.setRadius(size);
		this.character.setFill(color);
	}

	/**
	 * Swaps horizontal direction boolean of character
	 */
	public void swapDir() {
		if (dir)
			dir = false;
		else
			dir = true;
	}

	/**
	 * Returns x position of the Character
	 * @return - x position of the Character
	 */
	public double getx() {
		return this.x;
	}

	/**
	 * Sets x position of the Character
	 * @return - x position of the Character
	 */
	public void setx(double x) {
		this.x = x;
	}

	/**
	 * Returns y position of the Character
	 * @return - y position of the Character
	 */
	public double gety() {
		return this.y;
	}

	/**
	 * Sets y position of the Character
	 * @return - y position of the Character
	 */
	public void sety(double y) {
		this.y = y;
	}

	/**
	 * Sets minimum y position of the Character
	 * @return - minimum y position of the Character
	 */
	public void setMinY(double y) {
		this.minY = y;
	}

	/**
	 * Returns the size of the character
	 * @return - size of the character
	 */
	public double getSize() {
		return this.size;
	}

	/**
	 * Sets the size of the character
	 * @param size - the size of the character
	 */
	public void setSize(double size) {
		this.size = size;
	}

	/**
	 * Returns the color of the Character
	 * @return - the color of the Character
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Sets the color of the Character
	 * @return - the color of the Character
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Gets the delta x of the Character
	 * @return - the delta x of the Character
	 */
	public double getdx() {
		return this.dx;
	}

	/**
	 * Sets the delta x of the Character
	 * @parm dx - the delta x of the Character
	 */
	public void setdx(double dx) {
		// Set speed cap to 5 or -5
		if (dx > 5)
			dx = 5;
		else if (dx < -5)
			dx = -5;
		this.dx = dx;
	}

	/**
	 * Sets the ground level of the Character
	 * @param y - the ground level of the Character
	 */
	public void setGroundLevel(double y) {
		groundLevel = y;
	}

	/**
	 * Gets the delta y of the Character
	 * @return dx - the delta y of the Character
	 */
	public double getdy() {
		return this.dy;
	}

	/**
	 * Sets the delta y of the Character
	 * @param - the delta y of the Character
	 */
	public void setdy(double dy) {
		this.dy = dy;
	}

	/**
	 * Sets whether or not the Character is jumping
	 * @param b - whether or not the Character is jumping
	 */
	public void setJumping(boolean b) {
		this.jumping = b;
	}

	/**
	 * Gets whether or not the Character is jumping
	 * @return boolean - whether or not hte character is jumping
	 */
	public boolean getJumping() {
		return this.jumping;
	}

	/**
	 * Sets whether or not the Character is walking
	 * @param b - whether or not the Character is walking
	 */
	public void setWalking(boolean b) {
		this.walking = b;
	}

	/**
	 * Sets whether or not the Character is walking
	 * @return boolean - whether or not the Character is walking
	 */
	public boolean getWalking() {
		return this.walking;
	}

	/**
	 * Gets whether or not the Character is colliding
	 * @return boolean - whether or not the Character is colliding
	 */
	public boolean getCollide() {
		return collide;
	}
	
	/**
	 * Returns true if 'dir' is true and Character is therefore facing the right direction
	 * @return boolean - returns true if Character is facing the right direction
	 */
	public boolean movingRight() {
		return this.dir;
	}

	/**
	 * Sets whether or not the Character is colliding, initializes timer for last collision if true
	 * @param collide - whether or not the Character is colliding
	 */
	public void setCollide(boolean collide) {
		if(collide) {
			startTime = System.currentTimeMillis();
		}
		else {
			collisionTimeDelta = System.currentTimeMillis() - startTime;
		}
		this.collide = collide;
	}

	/**
	 * Returns the last time Character collided
	 * @return
	 */
	public double getCollisionDelta() {
		return this.collisionTimeDelta;
	}

	/**
	 * Returns true if left side of player is colliding with an object
	 * @return - left side of player is colliding with an object
	 */
	public boolean getCollideLeft() {
		return collideLeft;
	}

	/**
	 * Set whether or not left side of player is colliding with an object
	 * @param collide - whether or not left side of player is colliding with an object
	 */
	public void setCollideLeft(boolean collide) {
		this.collideLeft = collide;
	}

	/**
	 * Returns true if right side of player is colliding with an object
	 * @return - right side of player is colliding with an object
	 */
	public boolean getCollideRight() {
		return collideRight;
	}

	/**
	 * Set whether or not right side of player is colliding with an object
	 * @param collide - whether or not right side of player is colliding with an object
	 */
	public void setCollideRight(boolean collide) {
		this.collideRight = collide;
	}

	/**
	 * Set whether or not top side of player is colliding with an object
	 * @param collide - whether or not top side of player is colliding with an object
	 */
	public void setCollideTop(boolean b) {
		this.collideTop = b;
	}

	/**
	 * Returns true if top side of player is colliding with an object
	 * @return - top side of player is colliding with an object
	 */
	public boolean getCollideTop() {
		return this.collideTop;
	}

	/**
	 * Returns true if bottom side of player is colliding with an object
	 * @return - bottom side of player is colliding with an object
	 */
	public void setCollideBottom(boolean b) {
		this.collideBottom = b;
	}

	/**
	 * Returns true if bottom side of player is colliding with an object
	 * @return - bottom side of player is colliding with an object
	 */
	public boolean getCollideBottom() {
		return this.collideBottom;
	}

	/**
	 * Gets ground level of Character
	 * @return double - ground level of Character
	 */
	public double getgroundLevel() {
		return groundLevel;
	}

	/**
	 * Gets minimum y of Character
	 * @return - minimum y of Character
	 */
	public double getMinY() {
		return this.minY;
	}

	/**
	 * Gets radius of Character
	 * @return - radius of Character
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Gets radius of Character
	 * @param - radius of Character
	 */
	public void setRadius(double r) {
		radius = r;
	}

	/**
	 * Get previous x location of Character
	 * @return - previous x location of Character
	 */
	public double getPrevX() {
		return prevX;
	}

	/**
	 * Sets previous x location of Character
	 * @param - previous x location of Character
	 */
	public void setPrevX(double prevX) {
		this.prevX = prevX;
	}

	/**
	 * Get previous y location of Character
	 * @return - previous y location of Character
	 */
	public double getPrevY() {
		return prevY;
	}

	/**
	 * Sets previous y location of Character
	 * @param - previous y location of Character
	 */
	public void setPrevY(double prevY) {
		this.prevY = prevY;
	}

	/**
	 * Gets previous translateX location of Character
	 * @return - previous translateX location of Character
	 */
	public double getPrevTranslateX() {
		return prevTranslateX;
	}


	/**
	 * Sets previous translateX location of Character
	 * @param - previous translateX location of Character
	 */
	public void setPrevTranslateX(double prevTranslateX) {
		this.prevTranslateX = prevTranslateX;
	}

	/**
	 * Gets previous translateY location of Character
	 * @return - previous translateY location of Character
	 */
	public double getPrevTranslateY() {
		return prevTranslateY;
	}

	/**
	 * Sets previous translateY location of Character
	 * @param - previous translateY location of Character
	 */
	public void setPrevTranslateY(double prevTranslateY) {
		this.prevTranslateY = prevTranslateY;
	}

	/**
	 * Sets whether or not Character is dead
	 * @param dead - whether or not Character is dead
	 */
	public void setDead(Boolean dead) {
		this.dead = dead;
	}

	/**
	 * Returns true if Character is dead
	 * @param dead - whether or not Character is dead
	 */
	public boolean isDead() {
		return this.dead;
	}

	/**
	 * Returns delta x of the platform the Character is currently standing on
	 * @return double - the delta x of the platform the Character is currently standing on
	 */
	public double getPlatdx() {
		return this.platdx;
	}

	/**
	 * Returns delta y of the platform the Character is currently standing on
	 * @return double - the delta y of the platform the Character is currently standing on
	 */
	public double getPlatdy() {
		return this.platdy;
	}

	/**
	 * Sets platdx to delta x of the platform the Character is currently standing on
	 * @param platdx - the delta x of the platform the Character is currently standing on
	 */
	public void setPlatdx(double platdx) {
		this.platdx = platdx;
	}

	/**
	 * Sets platdy to delta y of the platform the Character is currently standing on
	 * @param platdy - the delta y of the platform the Character is currently standing on
	 */
	public void setPlatdy(double platdy) {
		this.platdy = platdy;
	}

	/**
	 * Sets whether or not the Character is on a moving platform
	 * @param b - whether or not the Character is on a moving platform
	 */
	public void setOnMovingPlat(boolean b) {
		this.onMovingPlat = b;
	}

	/**
	 * Gets whether or not the Character is on a moving platform
	 * @return boolean - whether or not the Character is on a moving platform
	 */
	public boolean getOnMovingPlat() {
		return this.onMovingPlat;
	}

	/**
	 * Sets whether or not Character is winning
	 * @param winning - whether or not Character is winning
	 */
	public void setWinning(boolean winning) {
		this.winning = winning;
	}

	/**
	 * Gets whether or not Character is winning
	 * @return boolean - whether or not Character is winning
	 */
	public boolean isWinning() {
		return this.winning;
	}


}
