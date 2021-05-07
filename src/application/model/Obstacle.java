package application.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle implements Collidable {
	
	//GUI vars
	private Rectangle platform, platTop;
	private Color initColor;

	//Position vars
	private double width, height, x, y;

	//Collisin vars
	private boolean collided;
	private boolean collidable = true;
	
	/**
	 * Obstacle constructor. Sets width/height/color of Obstacle.
	 * @param w - width of Obstacle
	 * @param h - height of Obstacle
	 * @param c - color of Obstacle
	 */
	public Obstacle(int w, int h, Color c) {
		this.platform = new Rectangle(w, h, c);
		this.platTop = new Rectangle(w, h*0.2, c);
		this.width = w;
		this.height = h;
	}

	/**
	 * Obstacle constructor. Sets width/height/ground color/grass color of Obstacle.
	 * @param w - width of Obstacle
	 * @param h - height of Obstacle
	 * @param c - 'ground' color of Obstacle
	 * @param cTop - 'grass' color of Obstacle
	 */
	public Obstacle(int w, int h, Color c, Color cTop) {
		this.platform = new Rectangle(w, h, c);
		this.platTop = new Rectangle(w, 5, cTop);
		this.width = w;
		this.height = h;
		this.initColor = c;
	}
	
	/**
	 * Returns true if bounds in the parameters collide with this object.
	 * @return boolean - bounds in the parameters collide with this object
	 */
	public boolean collide(double x, double y, double w, double h) {
		if(!collidable) 
			return false;
			
		if((x+w>= this.x && x-w <= this.x+width) && (y+h >= this.y && y-h <= this.y+height)) {
			setColliding(true);
			return true;
		}
		else {
			setColliding(false);
			return false;
		}
	}
	
	//Getters/Setters...

	/**
	 * Sets whether or not this object is collidable.
	 * @param isCollidable - whether or not this object is collidable
	 */
	public void setCollidable(boolean isCollidable) {
		this.collidable = isCollidable;
	}
	
	/**
	 * Returns whether or not this object has collided before
	 * @return boolean - whether or not this object has collided before
	 */
	public boolean hasCollided() {
		return collided;
	}
	
	/**
	 * Sets whether or not this object is currently colliding
	 * @param b - whether or not this object is currently colliding
	 */
	public void setColliding(boolean b) {
		this.collided = b;
	}

	/**
	 * Gets whether or not this object is collidable
	 * @return boolean - whether or not this object is collidable
	 */
	public boolean isCollidable() {
		return collidable;
	}
	
	/**
	 * Gets width of this obstacle
	 * @return double - width of this Obstacle
	 */
	public double getWidth() {
		return this.width;
	}
	
	/**
	 * Gets height of this obstacle
	 * @return double - height of this Obstacle
	 */
	public double getHeight() {
		return this.height;
	}

	/**
	 * Gets x position of this obstacle
	 * @return double - x position of this Obstacle
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Gets y position of this obstacle
	 * @return double - y position of this Obstacle
	 */
	public double getY() {
		return this.y;	
	}
	
	/**
	 * Gets Rectangle of this obstacle
	 * @return Rectangle - Rectangle of this Obstacle
	 */
	public Rectangle getPlat() {
		return this.platform;
	}

	/**
	 * Gets 'Grass' Rectangle of this obstacle
	 * @return Rectangle - 'Grass' Rectangle of this Obstacle
	 */
	public Rectangle getPlatTop() {
		return this.platTop;
	}
	
	/**
	 * Sets the width of this Obstacle
	 * @param w - the width of this Obstacle
	 */
	public void setWidth(double w) {
		this.width = w;
	}

	/**
	 * Sets the height of this Obstacle
	 * @param h - the height of this Obstacle
	 */
	public void setHeight(double h) {
		this.height = h;
	}

	/**
	 * Sets the x position of this Obstacle
	 * @param x - the x position of this Obstacle
	 */
	public void setX(double x) {
		this.x = x;
		this.platform.setX(x);
		this.platTop.setX(x);
	}

	/**
	 * Sets the y position of this Obstacle
	 * @param y - the y position of this Obstacle
	 */
	public void setY(double y) {
		this.y = y;
		this.platform.setY(y);
		this.platTop.setY(y);
	}

	/**
	 * Gets the Color of this Obstacle
	 * @return Color - the Color of this Obstacle
	 */
	public Color getColor() {
		return initColor;
	}
}
