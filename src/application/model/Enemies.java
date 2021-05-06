package application.model;

import java.util.Random;

import javafx.scene.paint.Color;
public class Enemies extends Character implements Collidable
{
	
	private boolean dir;
	private double initialX, initialY;
	private int zoneCode, groundLevel;
	@SuppressWarnings("unused")
	private boolean isCollidable;

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
		//If collision with something, or I guess it reached the end of a platform, move to the right.
		
	}
	
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

	public int getRNG(int max)
	{
		Random rng = new Random();
		return rng.nextInt(max);
	}
	
	public void swapDir()
	{
		if(dir)
			dir = false;
		else
			dir = true;
	}
	
	public boolean collide(double x, double y, double w, double h)
	{
		if((getx() - getRadius() <= x && getx()+getRadius() >= x) && (gety()+getRadius() >= y && gety()-getRadius() <= y))
			return true;
		else
			return false;
	}

	public void setCollidable(boolean isCollidable)
	{
		this.isCollidable = isCollidable;
	}

	public boolean isCollidable()
	{
		return true;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public double getInitialX()
	{
		return initialX;
	}
	
	public void setInitialX(double x)
	{
		initialX = x;
	}
	
	public double getInitY()
	{
		return initialY;
	}
	
	public int getZoneCode()
	{
		return zoneCode;
	}
	
	public int getGL()
	{
		return groundLevel;
	}
	
}
