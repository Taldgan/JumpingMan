package application.model;

import javafx.scene.paint.Color;

/**
 * Moving variant of the Obstacle class. Contains methods for moving the platform back and forth on top of the normal Obstacle methods.
 * @author Thomas White
 *
 */
public class MovingObstacle extends Obstacle {

	//Position variables
	private double dx, dy, moveX, endX, startX, moved;
	
	//Movement booleans
	private boolean moving = true;
	private boolean right = true;

	/**
	 * MovingObstacle constructor. Sets width/height/color and number of units to move back and forth
	 * @param w - width of the moving platform
	 * @param h - height of the moving platform
	 * @param c - color of the moving platform
	 * @param moveX - number of units for the platform to move back and forth
	 */
	public MovingObstacle(int w, int h, Color c, int moveX) {
		super(w, h, c);
		this.moveX = moveX;
		this.moved = 0;
	}

	/**
	 * MovingObstacle constructor. Sets width/height/color and number of units to move back and forth, as well as 'grass' color
	 * @param w - width of the moving platform
	 * @param h - height of the moving platform
	 * @param c - color of the moving platform 'ground'
	 * @param cTop - color of the moving platform 'grass'
	 * @param moveX - number of units for the platform to move back and forth
	 */
	public MovingObstacle(int w, int h, Color c, Color cTop, int moveX) {
		super(w, h, c, cTop);
	}
	
	/**
	 * Gets delta x of moving platform
	 * @return double - delta x of moving platform 
	 */
	public double getdx() {
		return dx;
	}
	
	/**
	 * Sets movement direction to left
	 */
	public void moveLeft() {
		setdx(-1);
	}

	/**
	 * Sets movement direction to right
	 */
	public void moveRight() {
		setdx(1);
	}
	
	/**
	 * Sets dx based off of horizontal movement boolean
	 */
	public void moveHoriz() {
		if(right) {
			setdx(1);
		}
		else {
			setdx(-1);
		}
	}
	
	/**
	 * Sets movement of platform to up
	 */
	public void moveUp() {
		setdy(-1);
	}

	/**
	 * Sets movement of platform to down
	 */
	public void moveDown() {
		setdy(1);
	}

	/**
	 * Move the platform. If it moves the set distance it was supposed to, swap directions.
	 */
	public void move() {
		moveHoriz();
		if(moved != endX-startX && moved != startX-endX) {
			moved += getdx();
			setX(getX() + getdx());
			setY(getY() + getdy());
		}
		else {
			moved = 0;
			swapDirection();
		}
	}
	
	/**
	 * Swap the direction of the platform.
	 */
	public void swapDirection() {
		if(right)
			right = false;
		else
			right = true;
	}

	/**
	 * Sets the delta x of the platform 
	 * @param dx - the delta x of the platform
	 */
	public void setdx(double dx) {
		this.dx = dx;
	}

	/**
	 * Gets delta y of moving platform
	 * @return double - delta y of moving platform 
	 */
	public double getdy() {
		return dy;
	}

	/**
	 * Sets delta y of moving platform
	 * @param dy - delta y of moving platform 
	 */
	public void setdy(double dy) {
		this.dy = dy;
	}

	/**
	 * Returns true if the platform is moving
	 * @return boolean - true if the platform is moving
	 */
	public boolean isMoving() {
		return this.moving;
	}

	/**
	 * Sets whether or not the platform is moving
	 * @param moving - Sets whether or not the platform is moving
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	/**
	 * Sets the starting and ending positions of the moving platform
	 * @param x - starting position of the platform, used to calculate ending position of the platform
	 */
	public void setStartEndX(int x) {
		this.startX = x;
		this.endX = startX+moveX;
	}
	
	/**
	 * Returns true if the platform is moving right.
	 * @return boolean - whether or not the platform is moving right
	 */
	public boolean movingRight() {
		return this.right;
	}

}
