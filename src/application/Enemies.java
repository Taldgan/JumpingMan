package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.Random;
public class Enemies extends Character implements Collidable
{
	
	private boolean dir;
	private boolean isCollidable;
	private Color initColor;
	private double initialX;
	private double initialY;
	private int zoneCode;
	private int groundLevel;
	public Enemies(double x, double y, double size, Color color, int zoneC,int gl) 
	{
		super(x, y, size, color);
		initialX = x;
		initialY = y;
		dir = true;
		isCollidable = true;
		initColor = color;
		zoneCode = zoneC;
		this.groundLevel = gl;
	}

	
	
	public void enemyMove()
	{
		/*this.character.setTranslateX(this.character.getTranslateX() + getdx());
		setx(this.character.getCenterX() + this.character.getTranslateX());
		this.character.setTranslateY(this.character.getTranslateY() + getdy());
		sety(this.character.getCenterY() + this.character.getTranslateY());*/
		
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
		if((getx() - radius <= x && getx()+radius >= x) && (gety()+radius >= y && gety()-radius <= y))
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
