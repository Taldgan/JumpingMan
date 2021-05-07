package application.model;

import java.util.Random;
import javafx.scene.paint.Color;

/**
 * Enemy class. Contains methods extending the base functionality of Character, allowing for enemies that roam the stage. 
 * @author Gabriel Pastelero
 *
 */
public class Enemies extends Character implements Collidable
{
	
	private double initialX, initialY;
	private int zoneCode, groundLevel;

	@SuppressWarnings("unused")
	private boolean isCollidable;
	private boolean dir;

	/**
	 * Enemey constructor. Sets x/y position, radius, color, zonecode, and ground level of Enemies.
	 * @param x - x position of Enemies
	 * @param y - y position  of Enemies
	 * @param size - radius of Enemies
	 * @param color - color of Enemies
	 * @param zoneC - zone code of Enemies
	 * @param gl - ground level of Enemies
	 */
	public Enemies(double x, double y, double size, Color color, int zoneC,int gl) 
	{
		super(x, y, size, color);
		initialX = x;
		initialY = y;
		dir = true;
		isCollidable = true;
		zoneCode = zoneC;
		this.groundLevel = gl;
	}

	/**
	 * Extends Character.move, moving automatically based off of direction boolean.
	 */
	public void enemyMove()
	{
		//Move left if direction is true
		if(dir)
		{
			super.setdx(-.5);
			super.move();
		}
		else
		{
			setdx(.5);
			move();
		}
	}
	
	/**
	 * Makes the enemy jump.
	 */
	public void enemyJump()
	{
		if(!getJumping())
		{
			setJumping(true);
			setdy(-3); //Up
		}
		if(gety() > groundLevel) //Ground level
		{
			setJumping(false);
			setdy(0);
		}
	}

	/**
	 * Returns random integer based off of a max value.
	 * @param max - the maxium value that can be randomly generated
	 * @return int - the random value generated
	 */
	public int getRNG(int max)
	{
		Random rng = new Random();
		return rng.nextInt(max);
	}
	
	/**
	 * Swaps the horizontal direction of the Enemies
	 */
	public void swapDir()
	{
		if(dir)
			dir = false;
		else
			dir = true;
	}
	
	/**
	 * Returns true if bounds in the parameters overlap with this Enemies
	 * @return boolean - bounds in the parameters overlap with this Enemies
	 */
	public boolean collide(double x, double y, double w, double h)
	{
		if((getx() - getRadius() <= x && getx()+getRadius() >= x) && (gety()+getRadius() >= y && gety()-getRadius() <= y))
			return true;
		else
			return false;
	}

	/**
	 * Sets whether or not this Enemies is collidable
	 * @param isCollidable - whether or not this Enemies is collidable
	 */
	public void setCollidable(boolean isCollidable)
	{
		this.isCollidable = isCollidable;
	}

	/**
	 * Gets whether or not this Enemies is collidable
	 * @return boolean - whether or not this Enemies is collidable
	 */
	public boolean isCollidable()
	{
		return true;
	}
	
	/**
	 * Returns the color of this Enemies
	 * @return Color - the color of this Enemies
	 */
	public Color getColor()
	{
		return color;
	}
	
	/**
	 * Returns the initial x value of this Enemies
	 * @return double - the initial x value of this Enemies
	 */
	public double getInitialX()
	{
		return initialX;
	}
	
	/**
	 * Sets the initial x value of this Enemies
	 * @return x - the initial x value of this Enemies
	 */
	public void setInitialX(double x)
	{
		initialX = x;
	}
	
	/**
	 * Returns the initial y value of this Enemies
	 * @return double - the initial y value of this Enemies
	 */
	public double getInitY()
	{
		return initialY;
	}
	
	/**
	 * Returns the zone code of this Enemies
	 * @return int - the zone code of this Enemies
	 */
	public int getZoneCode()
	{
		return zoneCode;
	}
	
	/**
	 * Returns the ground level of this Enemies
	 * @return int - the ground level of this Enemies
	 */
	public int getGL()
	{
		return groundLevel;
	}
	
}
